package org.tim_18.UberApp.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Message;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;

@Data
public class MessageResponseDTO {
    private Integer id;
    private String timeOfSending;
    private Integer senderId;
    private Integer receiverId;
    @Length(max = 500, message = "Max length of message is 500")

    private String message;
    private String type;
    private Integer rideId;

    public MessageResponseDTO() {
    }

    public MessageResponseDTO(Integer id, Date timeOfSending, Integer senderId, Integer receiverId, String message, String type, Integer rideId) {
        this.id             = id;
        this.timeOfSending  = timeOfSending.toString();
        this.senderId       = senderId;
        this.receiverId     = receiverId;
        this.message        = message;
        this.type           = type;
        this.rideId         = rideId;
    }
    public MessageResponseDTO(Message message) {
        this.id                 = message.getId();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = format.format(message.getTime());
        this.timeOfSending      = strDate;
        this.senderId           = message.getSender().getId();
        this.receiverId         = message.getReceiver().getId();
        this.message            = message.getMessage();
        this.type               = message.getMessageType();
        if (message.getRide()==null) {
            this.rideId         = -1;
        } else {
            this.rideId = message.getRide().getId();
        }
    }

    public HashSet<MessageResponseDTO> makeMessageResponseDTOS(Page<Message> messages){
        HashSet<MessageResponseDTO> messageDTOS = new HashSet<>();
        for (Message message:messages) {
            messageDTOS.add(new MessageResponseDTO(message));
        }
        return  messageDTOS;
    }
}
