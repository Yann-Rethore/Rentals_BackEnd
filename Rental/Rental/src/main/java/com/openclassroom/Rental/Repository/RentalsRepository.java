package com.openclassroom.Rental.Repository;

import com.openclassroom.Rental.DTO.RentalDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalsRepository extends JpaRepository<RentalDTO, Long> {
    // Define custom query methods if needed
    // For example, find rentals by user or property
    // List<Rentals> findByUserId(Long userId);
    // List<Rentals> findByPropertyId(Long propertyId);
}
