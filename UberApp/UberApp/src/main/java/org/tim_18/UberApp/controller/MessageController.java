package org.tim_18.UberApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tim_18.UberApp.model.Message;
import org.tim_18.UberApp.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.findAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Message> getMessageById (@PathVariable("id") Integer id) {
        Message message = messageService.findMessageById(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        Message newMessage = messageService.addMessage(message);
        return new ResponseEntity<>(newMessage, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) {
        Message updateMessage = messageService.updateMessage(message);
        return new ResponseEntity<>(updateMessage, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("id") Integer id) {
        messageService.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
