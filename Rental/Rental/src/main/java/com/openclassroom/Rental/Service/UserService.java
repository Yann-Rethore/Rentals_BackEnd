package com.openclassroom.Rental.Service;

import com.openclassroom.Rental.Entities.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassroom.Rental.Repository.UserRepository;

import javax.validation.constraints.NotNull;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @NotNull
    public Users registerUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Users findByEmailAndValidatePassword(String email, String rawPassword) {

        Users user;

        boolean userExist = userRepository.existsByEmail(email);
        if (!userExist) {
            throw new RuntimeException("Utilisateur non trouvé avec cet email.");
        }
        {
            // Recherche de l'utilisateur par email
            user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("Utilisateur non trouvé avec cet email.");
            }
            else {
            // Vérification du mot de passe
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                throw new RuntimeException("Mot de passe incorrect.");
            }
            return user;
        }



    }
}}