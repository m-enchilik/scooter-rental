package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.removeIt;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.DAO;

@Repository
public interface ScooterRepository extends DAO<Scooter, Long> {

    Scooter save(Scooter scooter);

    Optional<Scooter> findById(Long id);

    Scooter update(Scooter scooter);

    void delete(Long id);

    List<Scooter> findAll();

    List<Scooter> findByRentalPointId(Long rentalPointId);

    boolean existsBySerialNumber(String serialNumber);
}
