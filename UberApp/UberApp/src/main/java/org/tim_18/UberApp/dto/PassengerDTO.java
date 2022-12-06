package org.tim_18.UberApp.dto;

import org.tim_18.UberApp.model.Passenger;

public class PassengerDTO {
    private String firstName;
    private String lastName;
    private String imageLink;
    private String telephoneNumber;
    private String email;
    private String address;
    private String password;

    public PassengerDTO() {
    }

    public PassengerDTO(Passenger passenger){
        this(passenger.getFirstName(), passenger.getFirstName(), passenger.getImageLink(), passenger.getTelephoneNumber(), passenger.getEmail(), passenger.getAddress(), passenger.getPassword());
    }

    public PassengerDTO(String firstName, String lastName, String imageLink, String telephoneNumber, String email, String address, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageLink = imageLink;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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
}
