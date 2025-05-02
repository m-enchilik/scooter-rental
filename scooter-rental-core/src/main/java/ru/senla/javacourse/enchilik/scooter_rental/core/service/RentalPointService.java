package ru.senla.javacourse.enchilik.scooter_rental.core.service;


import java.util.List;
import ru.senla.javacourse.enchilik.scooter_rental.api.dto.RentalPointDto;
import ru.senla.javacourse.enchilik.scooter_rental.core.exception.RentalPointNotFoundException;

public interface RentalPointService {
    RentalPointDto save(RentalPointDto rentalPointDto)
            throws RentalPointNotFoundException;

    RentalPointDto findById(Long id) throws RentalPointNotFoundException;

    RentalPointDto update(Long id, RentalPointDto rentalPointDto)
            throws RentalPointNotFoundException;

    void delete(Long id) throws RentalPointNotFoundException;

    List<RentalPointDto> findAll();

    List<RentalPointDto> getRootRentalPoints();

    List<RentalPointDto> getChildRentalPoints(Long parentId)
            throws RentalPointNotFoundException;
}
