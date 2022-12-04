package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "user_activations")
public class UserActivation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    private LocalDate creationDate;
    private int duration;

    public UserActivation(Integer id, User user, LocalDate creationDate, int duration) {
        this.id           = id;
        this.user         = user;
        this.creationDate = creationDate;
        this.duration     = duration;
    }

    public UserActivation() {}

    public LocalDate getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDate creationDate) {this.creationDate = creationDate;}

    public int getDuration() {return duration;}
    public void setDuration(int duration) {this.duration = duration;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

}