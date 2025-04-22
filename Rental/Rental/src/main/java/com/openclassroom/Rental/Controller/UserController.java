package com.openclassroom.Rental.Controller;

import com.openclassroom.Rental.DTO.UserDTO;
import com.openclassroom.Rental.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api")
@Tag(name = ("Utilisateur"))
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Récupérer les informations d'un utilisateur par ID")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {


        // Récupération de l'utilisateur
        Optional<UserDTO> userOptional = userService.getUserById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Échec de la récupération de l'utilisateur\"}");
        }

        UserDTO user = userOptional.get();
        return ResponseEntity.ok(user);
    }
}
