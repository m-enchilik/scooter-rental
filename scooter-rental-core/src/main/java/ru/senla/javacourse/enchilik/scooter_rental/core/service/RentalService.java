package ru.senla.javacourse.enchilik.scooter_rental.core.service;

import java.util.List;
import ru.senla.javacourse.enchilik.scooter_rental.api.dto.RentalDto;
import ru.senla.javacourse.enchilik.scooter_rental.core.exception.RentalNotFoundException;
import ru.senla.javacourse.enchilik.scooter_rental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooter_rental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooter_rental.core.exception.UserNotFoundException;

public interface RentalService {
    RentalDto createRental(RentalDto rentalDto)
            throws UserNotFoundException, ScooterNotFoundException;

    RentalDto getRentalById(Long id) throws RentalNotFoundException;

    RentalDto endRental(Long id)
            throws RentalNotFoundException, ScooterNotFoundException, TariffNotFoundException;

    List<RentalDto> getAllRentals();

    List<RentalDto> getRentalsByUser(Long userId);

    List<RentalDto> getRentalsByScooter(Long scooterId);

    List<RentalDto> getRentalHistoryByScooter(Long scooterId);
}
