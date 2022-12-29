package org.tim_18.UberApp.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Message;

import java.time.LocalDateTime;
import java.util.HashSet;

@Data
public class MessageResponseDTO {
    private Integer id;
    private LocalDateTime timeOfSending;
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private String type;
    private Integer rideId;

    public MessageResponseDTO() {
    }

    public MessageResponseDTO(Integer id, LocalDateTime timeOfSending, Integer senderId, Integer receiverId, String message, String type, Integer rideId) {
        this.id             = id;
        this.timeOfSending  = timeOfSending;
        this.senderId       = senderId;
        this.receiverId     = receiverId;
        this.message        = message;
        this.type           = type;
        this.rideId         = rideId;
    }
    public MessageResponseDTO(Message message) {
        this.id             = message.getId();
        this.timeOfSending  = message.getTime();
        this.senderId       = message.getSender().getId();
        this.receiverId     = message.getReceiver().getId();
        this.message        = message.getMessage();
        this.type           = message.getMessageType();
        this.rideId         = message.getRide().getId();
    }

    public HashSet<MessageResponseDTO> makeMessageResponseDTOS(Page<Message> messages){
        HashSet<MessageResponseDTO> messageDTOS = new HashSet<>();
        for (Message message:messages) {
            messageDTOS.add(new MessageResponseDTO(message));
        }
        return  messageDTOS;
    }
}
