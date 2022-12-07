package org.tim_18.UberApp.dto;

public class AdministratorDTO {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String imageLink;

    public AdministratorDTO() {
    }

    public AdministratorDTO(String username, String password, String name, String surname, String imageLink) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.imageLink = imageLink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getname() {
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
