package com.openclassroom.Rental.Controller;


import com.openclassroom.Rental.DTO.MessageDTO;
import com.openclassroom.Rental.Service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Messages")
public class MessagesController {


    private final MessageService messageService;

    public MessagesController (MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Création et envoie d'un message")
    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody MessageDTO messageData) {



        // Validation des champs requis
        if (messageData.getUser_id() == null || messageData.getMessage() == null || messageData.getRental_id() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Missing required fields\"}");
        }

        try {
            // Enregistrement du message
            messageService.saveMessage(
                    messageData.getUser_id(),
                    messageData.getMessage(),
                    messageData.getRental_id()
            );

            return ResponseEntity.ok("{\"message\": \"M\"Message envoyé avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Échec de l'envoi du message\"}");
        }
    }


}
