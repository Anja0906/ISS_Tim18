package org.tim_18.UberApp.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.model.Message;
@Data
public class MessageDTO {

    private Integer receiverId;
    @Length(max = 500, message = "Max length of message is 500")

    private String message;
    private String type;
    private Integer rideId;

    public MessageDTO() {}
    public MessageDTO(Integer receiverId, String message, String type, Integer rideId) {
        this.receiverId = receiverId;
        this.message    = message;
        this.type       = type;
        this.rideId     = rideId;
    }
    public MessageDTO(Message message) {
        this.receiverId = message.getReceiver().getId();
        this.message    = message.getMessage();
        this.type       = message.getMessageType();
        this.rideId     = message.getRide().getId();
    }
}
