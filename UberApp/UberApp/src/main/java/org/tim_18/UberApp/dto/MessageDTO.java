package org.tim_18.UberApp.dto;

import org.tim_18.UberApp.model.Message;

public class MessageDTO {
    //{
    //  "receiverId": 123,
    //  "message": "The driver is going on a longer route on purpose",
    //  "type": "RIDE",
    //  "rideId": 123
    //}
    private Integer receiverId;
    private String message;
    private String type;
    private Integer rideId;

    public MessageDTO() {
    }

    public MessageDTO(Integer receiverId, String message, String type, Integer rideId) {
        this.receiverId = receiverId;
        this.message    = message;
        this.type       = type;
        this.rideId     = rideId;
    }

    public MessageDTO(Message message) {
        this.receiverId = message.getId();
        this.message    = message.getMessage();
        this.type       = message.getMessageType();
        this.rideId     = message.getRide().getId();
    }

    public Integer getReceiverId() {return receiverId;}
    public void setReceiverId(Integer receiverId) {this.receiverId = receiverId;}

    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public Integer getRideId() {return rideId;}
    public void setRideId(Integer rideId) {this.rideId = rideId;}
}
