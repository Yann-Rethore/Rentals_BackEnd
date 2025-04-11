package com.openclassroom.Rental.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassroom.Rental.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}