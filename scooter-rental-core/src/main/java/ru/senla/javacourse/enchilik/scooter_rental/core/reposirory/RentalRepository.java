package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory;

import java.util.List;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.Rental;

@Repository
public class RentalRepository extends AbstractDAO<Rental, Long> {
    @Override
    protected Class<Rental> getEntityClass() {
        return Rental.class;
    }

    List<Rental> findByUserId(Long userId);

    List<Rental> findByScooterId(Long scooterId);
}
