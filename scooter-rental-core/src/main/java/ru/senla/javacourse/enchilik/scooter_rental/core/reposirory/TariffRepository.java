package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory;

import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.Tariff;

@Repository
public class TariffRepository extends AbstractDAO<Tariff, Long> {
    @Override
    protected Class<Tariff> getEntityClass() {
        return Tariff.class;
    }


}
