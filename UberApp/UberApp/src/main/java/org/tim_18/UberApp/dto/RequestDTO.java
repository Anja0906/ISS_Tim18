package org.tim_18.UberApp.dto;

import jakarta.persistence.Column;
import org.tim_18.UberApp.model.Document;
import org.tim_18.UberApp.model.Request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RequestDTO {
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public RequestDTO() {
    }

    public RequestDTO(Integer userId, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.id = userId;
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
    }

    public RequestDTO(Request request) {
        this.id = request.getId();
        this.name = request.getName();
        this.surname = request.getSurname();
        this.profilePicture = request.getProfilePicture();
        this.telephoneNumber = request.getTelephoneNumber();
        this.email = request.getEmail();
        this.address = request.getAddress();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer userId) {
        this.id = userId;
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

    public ArrayList<RequestDTO> makeRequestDTO(List<Request> requests){
        ArrayList<RequestDTO>requestDTOS = new ArrayList<>();
        for(Request request: requests){
            requestDTOS.add(new RequestDTO(request));
        }
        return requestDTOS;
    }
}
