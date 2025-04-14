package com.openclassroom.Rental.Controller;

import com.openclassroom.Rental.Configuration.JwtUtil;
import com.openclassroom.Rental.DTO.LoginRequest;
import com.openclassroom.Rental.Entities.Users;
import com.openclassroom.Rental.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    //private final
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerAndReturnUser(@RequestBody Users user) {

            if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {        // Validation des champs
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }



        // Enregistrement de l'utilisateur

            userService.registerUser(user);
            String token = jwtUtil.generateToken(user.getName());

        return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        // Validation des champs
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Email et mot de passe requis\"}");
        }

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        // Vérification des valeurs
        Users user = userService.findByEmailAndValidatePassword(email, password);

            // Si les valeurs ne correspondent pas, renvoyer une erreur 404
            if (user == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Identifiants invalides\"}");}
            // Génération du token JWT
            String token = jwtUtil.generateToken(user.getName());

            // Réponse avec le token
        return ResponseEntity.ok("{\"token\": \"" + token + "\"}");

        }

        @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser() {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Utilisateur non authentifié\"}");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }




    }



