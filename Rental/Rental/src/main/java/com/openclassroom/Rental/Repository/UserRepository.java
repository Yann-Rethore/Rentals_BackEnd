package com.openclassroom.Rental.Repository;

import com.openclassroom.Rental.DTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, Long> {
    boolean existsByEmail(String email);

    UserDTO findByEmail(String email);


}