package com.openclassroom.Rental.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Schema(description = "Représente une message")
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore les champs null lors de la sérialisation
@Table(name = "messages")
public class MessageDTO {

    @Id
    @Schema(description = "Identifiant unique du message", example = "1")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rental_id", nullable = false)
    @Schema(description = "Identifiant de la location associée", example = "101")
    private Long rental_id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "Identifiant de l'utilisateur ayant envoyé le message", example = "42")
    private Long user_id;

    @Column(name = "message", nullable = false)
    @Schema(description = "Contenu du message", example = "Bonjour, je suis intéressé par cette location.")
    private String message;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "Date de création du message", example = "2023-10-01T12:00:00")
    private Timestamp createdAt;

}
