package org.tim_18.UberApp.dto;


import org.tim_18.UberApp.model.User;

public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
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
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getImageLink(), user.getTelephoneNumber(), user.getEmail(), user.getAddress(), user.getPassword(), user.isBlocked(), user.isActive());
    }

    public UserDTO(Integer id, String firstName, String lastName, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        this.id              = id;
        this.firstName       = firstName;
        this.lastName        = lastName;
        this.imageLink       = imageLink;
        this.telephoneNumber = telephoneNumber;
        this.email           = email;
        this.address         = address;
        this.password        = password;
        this.blocked         = blocked;
        this.active          = active;
    }

    public UserDTO(String firstName, String lastName, String imageLink, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
