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
import org.springframework.web.bind.annotation.RestController;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.SubscriptionDto;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.TariffDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.service.TariffService;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<TariffDto> createTariff(@Valid @RequestBody TariffDto tariffDto) {
        TariffDto createdTariff = tariffService.createTariff(tariffDto);
        return new ResponseEntity<>(createdTariff, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffDto> getTariffById(@PathVariable Long id)
        throws TariffNotFoundException {
        TariffDto tariff = tariffService.getTariffById(id);
        return new ResponseEntity<>(tariff, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<TariffDto> updateTariff(
        @PathVariable Long id, @Valid @RequestBody TariffDto tariffDto)
        throws TariffNotFoundException {
        TariffDto updatedTariff = tariffService.updateTariff(id, tariffDto);
        return new ResponseEntity<>(updatedTariff, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) throws TariffNotFoundException {
        tariffService.deleteTariff(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<TariffDto>> getAllTariffs() {
        List<TariffDto> tariffs = tariffService.getAllTariffs();
        return new ResponseEntity<>(tariffs, HttpStatus.OK);
    }

    @PostMapping("/buy/{id}")
    public ResponseEntity<SubscriptionDto> deleteTariff(@AuthenticationPrincipal User user, @PathVariable Long id)
        throws TariffNotFoundException {
        SubscriptionDto dto = tariffService.buyTariff(user, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
