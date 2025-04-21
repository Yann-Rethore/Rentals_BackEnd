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
@Schema(description = "Représente une location")
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore les champs null lors de la sérialisation
@Table(name = "rentals")
public class RentalDTO {
    @Id
    @Schema(description = "Id de la location")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", nullable = false)
    @Schema(description = "Nom de la location", example = "Appartement T2")
    private String name;

    @Column(name = "surface", nullable = false)
    @Schema(description = "Surface de la location en m²", example = "75.5")
    private Double surface;

    @Column(name = "price", nullable = false)
    @Schema(description = "Prix de la location", example = "1500.0")
    private Double price;

    @Column(name = "picture", nullable = false)
    @Schema(description = "URL de l'image de la location", example = "https://example.com/image.jpg")
    private String picture;

    @Column(name = "description", nullable = false)
    @Schema(description = "Description de la location", example = "Un bel appartement situé au centre-ville.")
    private String description;

    @Column(name = "owner_id", nullable = false)
    @Schema(description = "Identifiant du propriétaire", example = "42")
    private Long owner_id;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "Date de création de la location", example = "2023-01-01T12:00:00")
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Date de mise à jour de la location", example = "2023-01-02T12:00:00")
    private Timestamp updated_at;

}
