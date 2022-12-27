package org.tim_18.UberApp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.tim_18.UberApp.dto.UserDTO;

import java.io.Serializable;
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "users")
public class User implements Serializable{
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
    public User() {}


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
                '}';
    }
}
