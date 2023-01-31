package org.tim_18.UberApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.MessageNotFoundException;
import org.tim_18.UberApp.model.Message;
import org.tim_18.UberApp.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service("messageService")
public class MessageService {
    @Autowired
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {this.messageRepository = messageRepository;}
    public Message addMessage(Message message) {return messageRepository.save(message);}
    public List<Message> findAllMessages() {return messageRepository.findAll();}
    public Message findMessageById(Integer id) {
        return messageRepository.findMessageById(id)
                .orElseThrow(() -> new MessageNotFoundException("Message by id " + id + " was not found"));
    }
    public void saveMessage(Message message){messageRepository.save(message);}
    public void deleteMessage(Integer id) {messageRepository.deleteMessageById(id);}
    public Message updateMessage(Message message) {return messageRepository.save(message);}
    public Page<Message> findMessagesByUserId(Integer id, Pageable pageable) {return messageRepository.findMessagesByUserId(id, pageable);}
}
