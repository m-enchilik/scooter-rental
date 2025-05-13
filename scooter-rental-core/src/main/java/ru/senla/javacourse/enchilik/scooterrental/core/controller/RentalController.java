package ru.senla.javacourse.enchilik.scooterrental.core.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<RentalDto> createRental(@Valid @RequestBody RentalDto rentalDto)
        throws UserNotFoundException, ScooterNotFoundException {
        RentalDto createdRental = rentalService.save(rentalDto);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id)
        throws RentalNotFoundException {
        RentalDto rental = rentalService.getRentalById(id);
        return new ResponseEntity<>(rental, HttpStatus.OK);
    }

    @PutMapping("/{id}/end")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<RentalDto> endRental(@PathVariable Long id)
        throws RentalNotFoundException, ScooterNotFoundException {
        RentalDto endRental = rentalService.endRental(id);
        return new ResponseEntity<>(endRental, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        List<RentalDto> rentals = rentalService.getAllRentals();
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<RentalDto>> getRentalsByUser(@PathVariable Long userId) {
        List<RentalDto> rentals = rentalService.getRentalsByUser(userId);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/scooter/{scooterId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<RentalDto>> getRentalsByScooter(@PathVariable Long scooterId) {
        List<RentalDto> rentals = rentalService.getRentalsByScooter(scooterId);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/scooter/{scooterId}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentalDto>> getRentalHistoryByScooter(
        @PathVariable Long scooterId) {
        List<RentalDto> rentals = rentalService.getRentalHistoryByScooter(scooterId);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }
}
