package ru.senla.javacourse.enchilik.scooterrental.core.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalPointDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalPointNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.service.RentalPointService;

@RestController
@RequestMapping("/api/rental-points")
public class RentalPointController {

    private final RentalPointService rentalPointService;

    @Autowired
    public RentalPointController(RentalPointService rentalPointService) {
        this.rentalPointService = rentalPointService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<RentalPointDto> createRentalPoint(
        @Valid @RequestBody RentalPointDto rentalPointDto) throws RentalPointNotFoundException {
        RentalPointDto createdRentalPoint = rentalPointService.save(rentalPointDto);
        return new ResponseEntity<>(createdRentalPoint, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalPointDto> getRentalPointById(@PathVariable Long id)
        throws RentalPointNotFoundException {
        RentalPointDto rentalPoint = rentalPointService.findById(id);
        return new ResponseEntity<>(rentalPoint, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<RentalPointDto> updateRentalPoint(
        @PathVariable Long id, @Valid @RequestBody RentalPointDto rentalPointDto)
        throws RentalPointNotFoundException {
        RentalPointDto updatedRentalPoint =
            rentalPointService.update(id, rentalPointDto);
        return new ResponseEntity<>(updatedRentalPoint, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRentalPoint(@PathVariable Long id)
        throws RentalPointNotFoundException {
        rentalPointService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<RentalPointDto>> getAllRentalPoints() {
        List<RentalPointDto> rentalPoints = rentalPointService.findAll();
        return new ResponseEntity<>(rentalPoints, HttpStatus.OK);
    }

    @GetMapping("/root")
    public ResponseEntity<List<RentalPointDto>> getRootRentalPoints() {
        List<RentalPointDto> rootRentalPoints = rentalPointService.getRootRentalPoints();
        return new ResponseEntity<>(rootRentalPoints, HttpStatus.OK);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<RentalPointDto>> getChildRentalPoints(@PathVariable Long id)
        throws RentalPointNotFoundException {
        List<RentalPointDto> childRentalPoints = rentalPointService.getChildRentalPoints(id);
        return new ResponseEntity<>(childRentalPoints, HttpStatus.OK);
    }
}
