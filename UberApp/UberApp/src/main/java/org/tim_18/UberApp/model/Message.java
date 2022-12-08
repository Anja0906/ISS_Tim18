package org.tim_18.UberApp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.ALL})
    private User sender;
    @ManyToOne(cascade = {CascadeType.ALL})
    private User receiver;
    private String message;
    private LocalDateTime time;
    private String messageType;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Ride ride;


    public Message() {}

    public Message(Integer id, User sender, User receiver, String message, LocalDateTime time, String messageType, Ride ride) {
        this.id          = id;
        this.sender      = sender;
        this.receiver    = receiver;
        this.message     = message;
        this.time        = time;
        this.messageType = messageType;
        this.ride        = ride;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", messageType='" + messageType + '\'' +
                ", ride=" + ride +
                '}';
    }
}