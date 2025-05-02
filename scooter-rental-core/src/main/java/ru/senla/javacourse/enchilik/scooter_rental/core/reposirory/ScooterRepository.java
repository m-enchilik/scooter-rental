package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.Scooter;

@Repository
public class ScooterRepository extends AbstractDAO<Scooter, Long> {

    @Override
    protected Class<Scooter> getEntityClass() {
        return Scooter.class;
    }

    List<Scooter> findByRentalPointId(Long rentalPointId);

    boolean existsBySerialNumber(String serialNumber);
}
