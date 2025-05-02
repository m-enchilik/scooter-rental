package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.removeIt;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.DAO;

@Repository
public interface TariffRepository extends DAO<Tariff, Long> {
    Tariff save(Tariff tariff);

    Optional<Tariff> findById(Long id);

    Tariff update(Tariff tariff);

    void delete(Long id);

    List<Tariff> findAll();


}
