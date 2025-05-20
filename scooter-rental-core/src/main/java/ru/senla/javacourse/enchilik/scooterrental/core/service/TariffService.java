package ru.senla.javacourse.enchilik.scooterrental.core.service;

import java.util.List;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.SubscriptionDto;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.TariffDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

public interface TariffService {
    TariffDto createTariff(TariffDto tariffDto);

    TariffDto getTariffById(Long id) throws TariffNotFoundException;

    TariffDto updateTariff(Long id, TariffDto tariffDto) throws TariffNotFoundException;

    void deleteTariff(Long id) throws TariffNotFoundException;

    List<TariffDto> getAllTariffs();

    SubscriptionDto buyTariff(User user, Long id);
}
