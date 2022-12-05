package org.tim_18.UberApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.UserNotFoundException;
import org.tim_18.UberApp.model.Message;
import org.tim_18.UberApp.repository.MessageRepository;

import java.util.List;

@Service("messageService")
public class MessageService {
    @Autowired
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    public Message findMessageById(Integer id) {
        return messageRepository.findMessageById(id)
                .orElseThrow(() -> new UserNotFoundException("Message by id " + id + " was not found"));
    }

    public void deleteMessage(Integer id) {
        messageRepository.deleteMessageById(id);
    }

    public Message updateMessage(Message message) {
        return messageRepository.save(message);
    }
}
