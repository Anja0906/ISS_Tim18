package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "administrators")
public class Administrator implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String profilePicture;

    public Administrator() {}

    public Administrator(String username, String password, String name, String surname, String profilePicture) {
        this.username           = username;
        this.password           = password;
        this.name               = name;
        this.surname            = surname;
        this.profilePicture     = profilePicture;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}
    public void setSurname(String surname) {this.surname = surname;}


    public String getProfilePicture() {return profilePicture;}
    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

}