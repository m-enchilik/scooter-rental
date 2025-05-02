package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.removeIt;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.DAO;

@Repository
public interface RentalRepository extends DAO<Rental, Long> {
    Rental save(Rental rental);

    Optional<Rental> findById(Long id);

    Rental update(Rental rental);

    void delete(Long id);

    List<Rental> findAll();

    List<Rental> findByUserId(Long userId);

    List<Rental> findByScooterId(Long scooterId);
}
