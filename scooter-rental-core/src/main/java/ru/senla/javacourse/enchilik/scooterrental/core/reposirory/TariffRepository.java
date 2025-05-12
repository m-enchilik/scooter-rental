package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;

@Repository
public class TariffRepository extends AbstractDao<Tariff, Long> {
    @Override
    protected Class<Tariff> getEntityClass() {
        return Tariff.class;
    }
}
