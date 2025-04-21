package com.openclassroom.Rental.Service;

import com.openclassroom.Rental.Configuration.JwtUtil;
import com.openclassroom.Rental.DTO.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassroom.Rental.Repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @NotNull
    public void registerUser(UserDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
        user.setUpdatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
        userRepository.save(user);
    }

    public UserDTO findByEmailAndValidatePassword(String email, String rawPassword) {

        UserDTO user;

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
    }

    public UserDTO findByEmailAndGetUser(String email) {
        UserDTO user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé avec mail.");
        }
        else {
            return user;
        }
    }

    public Long findByEmailAndGetUserID(String token) {
        String email = jwtUtil.extractUserEmail(token);
        UserDTO user = findByEmailAndGetUser(email);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé avec mail.");
        } else {
            return user.getId();
        }
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id);
    }
}