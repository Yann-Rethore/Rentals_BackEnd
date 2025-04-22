package com.openclassroom.Rental.Controller;

import com.openclassroom.Rental.Configuration.JwtUtil;
import com.openclassroom.Rental.DTO.UserDTO;
import com.openclassroom.Rental.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {


    private final UserService userService;


    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Enregistrement d'un utilisateur")
    @PostMapping("/register")
    public ResponseEntity<?> registerAndReturnUser(@RequestBody UserDTO user) {

            if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {        // Validation des champs
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        // Enregistrement de l'utilisateur
            userService.registerUser(user);

            // Création du token
            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");


    }

    @Operation(summary = "Connexion d'un utilisateur")
    @PostMapping("/login")
    //public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        public ResponseEntity<?> login(@RequestBody UserDTO loginRequest) {
        // Invalider le token en cours
        SecurityContextHolder.clearContext();

        // Validation des champs
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Email et mot de passe requis\"}");
        }


        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        // Vérification des valeurs
        UserDTO user = userService.findByEmailAndValidatePassword(email, password);

            // Si les valeurs ne correspondent pas, renvoyer une erreur 401
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Identifiants invalides\"}");
            }
            else {
            // Génération du token JWT
            String token = jwtUtil.generateToken(email);

            // Réponse avec le token
                return ResponseEntity.ok("{\"token\": \"" + token + "\"}");

        }

    }

    @Operation(summary = "Récupérer les informations de l'utilisateur connecté")
    @GetMapping("me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Vérifiez si l'en-tête Authorization est présent
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
        // Extraire le jeton
        String token = authorizationHeader.substring(7);

        // Récupérer les informations de l'utilisateur à partir du jeton
        String email = jwtUtil.extractUsername(token);

        UserDTO user = userService.findByEmailAndGetUser(email);

        String id = user.getId().toString();
        String name = user.getName();
        Timestamp createdAt = user.getCreatedAt();
        Timestamp updatedAt = user.getUpdatedAt();


        // réponse utilisateur
        return ResponseEntity.ok("{\n" +
                "  \"id\": \"" + id + "\",\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"created_at\": \"" + createdAt.toLocalDateTime() + "\",\n" +
                "  \"updated_at\": \"" + updatedAt.toLocalDateTime() + "\"\n" +
                "}");
    }
}




