package com.openclassroom.Rental.Service;

import com.openclassroom.Rental.DTO.RentalDTO;
import com.openclassroom.Rental.Repository.RentalsRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class RentalsService {

    private final RentalsRepository rentalsRepository;

    public RentalsService(RentalsRepository rentalsRepository) {
        this.rentalsRepository = rentalsRepository;
    }


        public void createRental(String name, Double surface, Double price, String pictureURL, String description, Long ownerId) {
            RentalDTO rental = new RentalDTO();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setPicture(pictureURL);
            rental.setDescription(description);
            rental.setOwner_id(ownerId);
            rental.setCreated_at(new Timestamp(System.currentTimeMillis()));
            rental.setUpdated_at(new Timestamp(System.currentTimeMillis()));

            rentalsRepository.save(rental);
        }

    public List<RentalDTO> getAllRentals() {
        return rentalsRepository.findAll();
    }



    public boolean updateRental(Long id, String name, Double surface, Double price,  String description) {
        Optional<RentalDTO> rentalOptional = rentalsRepository.findById(id);

        if (rentalOptional.isPresent()) {
            RentalDTO rental = rentalOptional.get();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            rentalsRepository.save(rental);
            return true;
        }

        return false;

    }public Optional<RentalDTO> getRentalById(Long id) {
        return rentalsRepository.findById(id);
    }

}


