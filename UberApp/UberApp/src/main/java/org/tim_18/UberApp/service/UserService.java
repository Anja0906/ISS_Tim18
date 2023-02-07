package org.tim_18.UberApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.dto.UserDTO;
import org.tim_18.UberApp.dto.UserDTOwithPassword;
import org.tim_18.UberApp.dto.UserDTO;
import org.tim_18.UberApp.dto.UserDTOwithPassword;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Role;
import org.tim_18.UberApp.model.User;
import org.tim_18.UberApp.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("userService")
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleService roleService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = javaMailSender;
    }

    public void register(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setActive(false);

        userRepository.save(user);

        sendVerificationEmail(user, siteURL);
    }

    public void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "uberapptim18@gmail.com";
        String senderName = "UberAppTim18";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName() + " " + user.getSurname());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public void sendEmail(String recipientEmail, String token)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("uberapptim18@gmail.com", "UberAppTim18 Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>"+token+"</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isActive()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setActive(true);
            userRepository.save(user);

            return true;
        }

    }

    public User updateUserFromDto(Integer id, UserDTO userDTO){
        User user = this.findUserById(id);
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setTelephoneNumber(userDTO.getTelephoneNumber());
        user.setBlocked(userDTO.isBlocked());
        return this.updateUser(user);
    }



    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }


    public User findUserById(Integer id) {
        return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User findUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User by email " + email + " was not found"));
    }


    public User save(UserDTOwithPassword userRequest) {
        User u = new User();
        u.setEmail(userRequest.getEmail());
        // pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
        // treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
        u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        u.setName(userRequest.getName());
        u.setSurname(userRequest.getSurname());
        u.setBlocked(false);
        u.setEmail(userRequest.getEmail());
        // u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
        List<Role> roles = roleService.findByName("ROLE_USER");
        u.setRoles(roles);
        return this.userRepository.save(u);
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).get();
        if (user != null) {
            user.setResetPasswordToken(token);
            user.setTimeOfResetPasswordToken(new Date());
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }

    public int generateRandomInt(){
        int min = 100000;
        int max = 999999;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public boolean compareIfCodeIsExpired(Date expiresIn){
        Calendar cal = Calendar.getInstance();
        cal.setTime(expiresIn);
        cal.add(Calendar.MINUTE, 10000); // adding 30 minutes
        expiresIn = cal.getTime();
        Date date = new Date(); // current date
        if(date.after(expiresIn)){
            return true;
        }else {
            return false;
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
    public User save(User u) {
        return this.userRepository.save(u);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

}
