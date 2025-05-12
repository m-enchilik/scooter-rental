package ru.senla.javacourse.enchilik.scooterrental.core.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.ScooterDto;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.ScooterStatus;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalPointNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.service.ScooterService;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

    private final ScooterService scooterService;

    @Autowired
    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ScooterDto> createScooter(@Valid @RequestBody ScooterDto scooterDto)
        throws RentalPointNotFoundException {
        ScooterDto createdScooter = scooterService.createScooter(scooterDto);
        return new ResponseEntity<>(createdScooter, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScooterDto> getScooterById(@PathVariable Long id)
        throws ScooterNotFoundException {
        ScooterDto scooter = scooterService.getScooterById(id);
        return new ResponseEntity<>(scooter, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ScooterDto> updateScooter(
        @PathVariable Long id, @Valid @RequestBody ScooterDto scooterDto)
        throws ScooterNotFoundException, RentalPointNotFoundException {
        ScooterDto updatedScooter = scooterService.updateScooter(id, scooterDto);
        return new ResponseEntity<>(updatedScooter, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteScooter(@PathVariable Long id)
        throws ScooterNotFoundException {
        scooterService.deleteScooter(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<ScooterDto>> getAllScooters() {
        List<ScooterDto> scooters = scooterService.getAllScooters();
        return new ResponseEntity<>(scooters, HttpStatus.OK);
    }

    @GetMapping("/rental-point/{rentalPointId}")
    public ResponseEntity<List<ScooterDto>> getScootersByRentalPoint(
        @PathVariable Long rentalPointId) {
        List<ScooterDto> scooters = scooterService.getScootersByRentalPoint(rentalPointId);
        return new ResponseEntity<>(scooters, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Void> updateScooterStatus(
        @PathVariable Long id, @RequestParam ScooterStatus newStatus)
        throws ScooterNotFoundException {
        scooterService.updateScooterStatus(id, newStatus);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
