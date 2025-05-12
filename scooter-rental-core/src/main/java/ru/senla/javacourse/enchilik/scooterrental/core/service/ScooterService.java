package ru.senla.javacourse.enchilik.scooterrental.core.service;

import java.util.List;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.ScooterDto;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.ScooterStatus;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalPointNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;

public interface ScooterService {
    ScooterDto createScooter(ScooterDto scooterDto)
        throws RentalPointNotFoundException, TariffNotFoundException;

    ScooterDto getScooterById(Long id) throws ScooterNotFoundException;

    ScooterDto updateScooter(Long id, ScooterDto scooterDto)
        throws ScooterNotFoundException, RentalPointNotFoundException, TariffNotFoundException;

    void deleteScooter(Long id) throws ScooterNotFoundException;

    List<ScooterDto> getAllScooters();

    List<ScooterDto> getScootersByRentalPoint(Long rentalPointId)
        throws RentalPointNotFoundException;

    void updateScooterStatus(Long scooterId, ScooterStatus newStatus)
        throws ScooterNotFoundException;
}
