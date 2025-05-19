package ru.senla.javacourse.enchilik.scooterrental.core.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalPointNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.RentalService;
import ru.senla.javacourse.enchilik.scooterrental.core.service.SecurityService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<RentalDto> createRental(@Valid @RequestBody RentalDto rentalDto)
        throws UserNotFoundException, ScooterNotFoundException {
        RentalDto createdRental = rentalService.save(rentalDto);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RentalDto> updateRental(@PathVariable Long id, @Valid @RequestBody RentalDto rentalDto)
        throws RentalNotFoundException {
        RentalDto updatedRental = rentalService.update(id, rentalDto);
        return new ResponseEntity<>(updatedRental, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id)
        throws RentalNotFoundException {
        RentalDto rental = rentalService.getRentalById(id);
        return new ResponseEntity<>(rental, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id)
        throws RentalNotFoundException {
        rentalService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/start")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<RentalDto> startRental(
        @RequestParam Long subscriptionId,
        @RequestParam Long scooterId,
        @AuthenticationPrincipal User user
    ) throws RentalNotFoundException, ScooterNotFoundException {
        RentalDto rentalDto = rentalService.startRental(user, subscriptionId, scooterId);
        return new ResponseEntity<>(rentalDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/end")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<RentalDto> endRental(@PathVariable Long id)
        throws RentalNotFoundException, ScooterNotFoundException {
        RentalDto endRental = rentalService.endRental(id);
        return new ResponseEntity<>(endRental, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        List<RentalDto> rentals = rentalService.getAllRentals();
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<RentalDto>> getRentalsByUser(@PathVariable Long userId) {
        List<RentalDto> rentals = rentalService.getRentalsByUser(userId);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<RentalDto>> getRentalsForAuthenticatedUser(@AuthenticationPrincipal User user) {
        List<RentalDto> rentals = rentalService.getRentalsByUser(user.getId());
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/scooter/{scooterId}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<RentalDto>> getRentalsByScooter(@PathVariable Long scooterId) {
        List<RentalDto> rentals = rentalService.getRentalsByScooter(scooterId);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    @GetMapping("/scooter/{scooterId}/history")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RentalDto>> getRentalHistoryByScooter(
        @PathVariable Long scooterId) {
        List<RentalDto> rentals = rentalService.getRentalHistoryByScooter(scooterId);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }
}
