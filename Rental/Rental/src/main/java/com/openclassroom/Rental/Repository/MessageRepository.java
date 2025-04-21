package com.openclassroom.Rental.Repository;

import com.openclassroom.Rental.DTO.MessageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageDTO, Long> {


}
