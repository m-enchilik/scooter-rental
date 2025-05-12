package ru.senla.javacourse.enchilik.scooterrental.core.service;

import java.util.List;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.TariffDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;

public interface TariffService {
    TariffDto createTariff(TariffDto tariffDto);

    TariffDto getTariffById(Long id) throws TariffNotFoundException;

    TariffDto updateTariff(Long id, TariffDto tariffDto) throws TariffNotFoundException;

    void deleteTariff(Long id) throws TariffNotFoundException;

    List<TariffDto> getAllTariffs();
}
