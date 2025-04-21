package com.openclassroom.Rental.Service;

import com.openclassroom.Rental.DTO.MessageDTO;
import com.openclassroom.Rental.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messagesRepository;

    public MessageService(MessageRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }


    public void saveMessage(Long user_id, String message, Long rental_id) {
        MessageDTO newMessage = new MessageDTO();
        newMessage.setUser_id(user_id);
        newMessage.setMessage(message);
        newMessage.setRental_id(rental_id);
        newMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        messagesRepository.save(newMessage);
    }
}
