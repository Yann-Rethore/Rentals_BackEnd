package com.openclassroom.Rental.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassroom.Rental.DTO.RentalDTO;
import io.github.cdimascio.dotenv.Dotenv;
import com.openclassroom.Rental.Service.S3Service;
import com.openclassroom.Rental.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassroom.Rental.Service.RentalsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@Tag(name = "Locations")
public class RentalsController {


    private final RentalsService rentalsService;
    private final UserService userService;
    private final S3Service s3Service ;

    public RentalsController(RentalsService rentalsService,  UserService userService, S3Service s3Service) {
        this.rentalsService = rentalsService;
        this.userService = userService;
        this.s3Service = s3Service;
    }

    @Operation(summary = "Créer une location")
    @ApiResponse(responseCode = "200", description = "Liste des locations",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RentalDTO.class)))
    @PostMapping(value = "/rentals", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createRental(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                          @RequestParam("picture")
                                          @io.swagger.v3.oas.annotations.Parameter(description = "Image de la location", required = true) MultipartFile picture,
                                          @RequestParam("name")
                                          @io.swagger.v3.oas.annotations.Parameter(description = "Nom de la location", example = "Appartement T2", required = true) String name,
                                          @RequestParam("surface")
                                          @io.swagger.v3.oas.annotations.Parameter(description = "Surface de la location en m²", example = "75.5", required = true) Double surface,
                                          @RequestParam("price")
                                          @io.swagger.v3.oas.annotations.Parameter(description = "Prix de la location", example = "1500.0", required = true) Double price,
                                          @RequestParam("description") @io.swagger.v3.oas.annotations.Parameter(description = "Description de la location", example = "Un bel appartement situé au centre-ville.", required = true) String description)
    {
        if (picture.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Picture is required\"}");
        }

        String key = "images/" + picture.getOriginalFilename();

        try {

            // Upload the file to S3
            s3Service.uploadFile(picture, key);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to upload picture\"}");
        }

        Dotenv dotenv = Dotenv.load();
        String fileUrl = dotenv.get("AWS_URL") + key;

        // Extraire le jeton
        String token = authorizationHeader.substring(7);
        Long userId = userService.findByEmailAndGetUserID(token);

        rentalsService.createRental(
                name,
                surface,
                price,
                fileUrl,
                description,
                userId
        );

        return ResponseEntity.ok("{\"message\": \"Rental created and image uploaded!\"}");
    }

    @Operation(summary = ("List les locations"))
    @ApiResponse(responseCode = "200", description = "Liste des locations",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RentalDTO.class)))
    @GetMapping("/rentals")
    public ResponseEntity<?> getRentals(@RequestHeader(value = "Authorization", required = false)  String authorizationHeader) {

        List<RentalDTO> rentalsList = rentalsService.getAllRentals();

        if (rentalsList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"No rentals found\"}");
        }

        try {
            // Utilisation de ObjectMapper pour convertir la liste en JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(Map.of("rentals", rentalsList));

            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to process rentals\"}");
        }
    }

    @Operation(summary = "Update d'une location")
    @ApiResponse(responseCode = "200", description = "Liste des locations",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RentalDTO.class)))
    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id,
            @RequestParam("name")
            @io.swagger.v3.oas.annotations.Parameter(description = "Nom de la location", example = "Appartement T2", required = true) String name,
            @RequestParam("surface")
            @io.swagger.v3.oas.annotations.Parameter(description = "Surface de la location en m²", example = "75.5", required = true) Double surface,
            @RequestParam("price")
            @io.swagger.v3.oas.annotations.Parameter(description = "Prix de la location", example = "1500.0", required = true) Double price,
            @RequestParam("description")
    @io.swagger.v3.oas.annotations.Parameter(description = "Description de la location", example = "Un bel appartement situé au centre-ville.", required = true) String description)
    {



        try {
            boolean updated = rentalsService.updateRental(
                    id,
                    name,
                    surface,
                    price,
                    description
            );

            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Rental not found\"}");
            }

            return ResponseEntity.ok("{\"message\": \"Rental updated !\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to update rental\"}");
        }
    }

    @Operation(summary = "Récupérer une location par son ID")
    @ApiResponse(responseCode = "200", description = "Liste des locations",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RentalDTO.class)))
    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRentalById (
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {

        // Récupération de la location
        Optional<RentalDTO> rentalOptional = rentalsService.getRentalById(id);

        if (rentalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Rental not found\"}");
        }

        RentalDTO rental = rentalOptional.get();
        return ResponseEntity.ok(rental);
    }


}
