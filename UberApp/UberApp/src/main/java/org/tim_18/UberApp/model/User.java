package org.tim_18.UberApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    @Column(nullable = false)
    private String email;
    private String address;
    @Column(nullable = false)
    private String password;
    private boolean blocked;
    private boolean active;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "time_of_reset_password_token")
    private Date timeOfResetPasswordToken;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    public User() {}

    public User(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, List<Role> roles) {
        this.name            = name;
        this.surname         = surname;
        this.profilePicture  = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email           = email;
        this.address         = address;
        this.password        = password;
        this.blocked         = blocked;
        this.active          = active;
        this.roles           = roles;
    }

    public User(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        this.name            = name;
        this.surname         = surname;
        this.profilePicture  = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email           = email;
        this.address         = address;
        this.password        = password;
        this.blocked         = blocked;
        this.active          = active;
    }

    public User(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active, String verificationCode) {
        this.name            = name;
        this.surname         = surname;
        this.profilePicture  = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email           = email;
        this.address         = address;
        this.password        = password;
        this.blocked         = blocked;
        this.active          = active;
        this.verificationCode = verificationCode;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isBlocked() {
        return blocked;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }


    public boolean isActive() {
        return active;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getTimeOfResetPasswordToken() {
        return timeOfResetPasswordToken;
    }

    public void setTimeOfResetPasswordToken(Date timeOfResetPasswordToken) {
        this.timeOfResetPasswordToken = timeOfResetPasswordToken;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", blocked=" + blocked +
                ", active=" + active +
                ", roles=" + roles.toString() +
                '}';
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }
    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

}
