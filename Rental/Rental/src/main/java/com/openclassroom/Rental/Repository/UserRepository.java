package com.openclassroom.Rental.Repository;

import com.openclassroom.Rental.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByName(String username);
    boolean existsByEmail(String email);

    Users findByEmail(String email);


}