package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "notes")
public class Note implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String message;
    @ManyToOne
    private User user;

    public Note() {
    }

    public Note(Integer id, String message, User user) {
        this.id         = id;
        this.message    = message;
        this.user       = user;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
