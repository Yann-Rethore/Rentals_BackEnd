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
@   Schema(description = "Représente un utilisateur")
    @JsonInclude(JsonInclude.Include.NON_NULL) // Ignore les champs null lors de la sérialisation
    @Table(name = "users")
    public class UserDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Schema(description = "Identifiant unique de l'utilisateur", example = "1")
        private Long id;

        @Column(name = "name", nullable = false, unique = true)
        @Schema(description = "nom de l'utilisateur", example = "Jean Dupont")
        private String name;

        @Column(name = "email", nullable = false, unique = true)
        @Schema(description = "Adresse e-mail de l'utilisateur", example = "user@example.com")
        private String email;

        @Column(name = "password", nullable = false)
        @Schema(description = "Mot de passe de l'utilisateur", example = "password123")
        private String password;

        @Column(name = "created_at", nullable = false)
        @Schema(description = "Date de création de l'utilisateur", example = "2023-10-01T12:00:00")
        private Timestamp createdAt;

        @Column(name = "updated_at", nullable = false)
        @Schema(description = "Date de mise à jour de l'utilisateur", example = "2023-10-01T12:00:00")
        private Timestamp updatedAt;


    }
