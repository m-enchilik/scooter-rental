package ru.senla.javacourse.enchilik.scooterrental.core.service;

import java.util.List;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalPointNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

public interface RentalService {
    RentalDto save(RentalDto rentalDto)
        throws UserNotFoundException, ScooterNotFoundException;

    RentalDto update(Long id, RentalDto rentalDto) throws RentalNotFoundException;

    RentalDto getRentalById(Long id) throws RentalNotFoundException;

    void delete(Long id) throws RentalNotFoundException;

    RentalDto startRental(User user, Long subscriptionId, Long scooterId)
        throws RentalNotFoundException, ScooterNotFoundException, TariffNotFoundException;

    RentalDto endRental(Long scooterId, Long pointId)
        throws RentalPointNotFoundException, RentalNotFoundException, ScooterNotFoundException, TariffNotFoundException;

    List<RentalDto> getAllRentals();

    List<RentalDto> getRentalsByUser(Long userId);

    List<RentalDto> getRentalsByScooter(Long scooterId);
}
