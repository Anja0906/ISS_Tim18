package org.tim_18.UberApp.dto;


import org.tim_18.UberApp.model.User;

public class UserDTO {
    private Integer id;
    private String name;
    private String surname;
    private String imageLink;
    private String telephoneNumber;
    private String email;
    private String address;
    private String password;
    private boolean blocked;
    private boolean active;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getId(), user.getName(), user.getSurname(), user.getProfilePicture(), user.getTelephoneNumber(), user.getEmail(), user.getAddress(), user.getPassword(), user.isBlocked(), user.isActive());
    }

    public UserDTO(Integer id, String name, String surname, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        this.id              = id;
        this.name            = name;
        this.surname        = surname;
        this.imageLink       = imageLink;
        this.telephoneNumber = telephoneNumber;
        this.email           = email;
        this.address         = address;
        this.password        = password;
        this.blocked         = blocked;
        this.active          = active;
    }

    public UserDTO(String name, String surname, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
    }

    public UserDTO(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password) {

    }

    public UserDTO(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isActive() {
        return active;
    }
}
