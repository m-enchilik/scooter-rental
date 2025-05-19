package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.TariffDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.TariffRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.TariffService;

@Service
public class TariffServiceImpl implements TariffService {

    private static final Logger logger = LoggerFactory.getLogger(TariffServiceImpl.class);

    private final TariffRepository tariffRepository;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public TariffDto createTariff(TariffDto tariffDto) {
        logger.info("Попытка создать новый тариф с данными: {}", tariffDto);
        try {
            Tariff tariff = new Tariff();
            tariff.setType(tariffDto.getType());
            tariff.setName(tariffDto.getName());
            tariff.setDescription(tariffDto.getDescription());
            tariff.setPrice(tariffDto.getSubscriptionPrice());
            tariff.setUnitsIncluded(tariffDto.getUnitsIncluded());
            tariff.setValidityPeriodHours(tariffDto.getValidityPeriodHours());
            tariff.setIsSubscription(tariffDto.getIsSubscription());


            tariff = tariffRepository.save(tariff);
            tariffDto.setId(tariff.getId());

            logger.info("Тариф успешно создан с ID: {}", tariff.getId());
            return tariffDto;
        } catch (Exception e) {
            logger.error("Ошибка при создании тарифа: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public TariffDto getTariffById(Long id) throws TariffNotFoundException {
        logger.info("Попытка получить тариф с ID: {}", id);
        try {
            Tariff tariff =
                tariffRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Тариф с ID {} не найден.", id);
                            return new TariffNotFoundException(
                                "Тариф с ID " + id + " не найден");
                        });
            TariffDto tariffDto = convertToTariffDto(tariff);
            logger.info("Тариф с ID {} успешно получен.", id);
            return tariffDto;
        } catch (Exception e) {
            logger.error("Ошибка при получении тарифа с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public TariffDto updateTariff(Long id, TariffDto tariffDto) throws TariffNotFoundException {
        logger.info("Попытка обновить тариф с ID: {}, данные: {}", id, tariffDto);
        try {
            Tariff tariff =
                tariffRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Тариф с ID {} не найден.", id);
                            return new TariffNotFoundException(
                                "Тариф с ID " + id + " не найден");
                        });

            if (tariffDto.getName() != null) {
                tariff.setName(tariffDto.getName());
            }
            if (tariffDto.getDescription() != null) {
                tariff.setDescription(tariffDto.getDescription());
            }
            //            TODO: доработать
            //            if (tariffDto.getPricePerHour() != null) {
            //                tariff.setPricePerHour(tariffDto.getPricePerHour());
            //            }
            if (tariffDto.getSubscriptionPrice() != null) {
                tariff.setPrice(tariffDto.getSubscriptionPrice());
            }
            //            if (tariffDto.getDiscount() != null) {
            //                tariff.setDiscount(tariffDto.getDiscount());
            //            }
            if (tariffDto.getIsSubscription() != null) {
                tariff.setIsSubscription(tariffDto.getIsSubscription());
            }

            tariffRepository.save(tariff);
            TariffDto updatedTariffDto = convertToTariffDto(tariff);

            logger.info("Тариф с ID {} успешно обновлен.", id);
            return updatedTariffDto;
        } catch (Exception e) {
            logger.error("Ошибка при обновлении тарифа с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteTariff(Long id) throws TariffNotFoundException {
        logger.info("Попытка удалить тариф с ID: {}", id);
        try {
            Tariff tariff =
                tariffRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Тариф с ID {} не найден.", id);
                            return new TariffNotFoundException(
                                "Тариф с ID " + id + " не найден");
                        });
            tariffRepository.delete(tariff.getId());
            logger.info("Тариф с ID {} успешно удален.", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении тарифа с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<TariffDto> getAllTariffs() {
        logger.info("Попытка получить все тарифы.");
        try {
            List<Tariff> tariffs = tariffRepository.findAll();
            List<TariffDto> tariffDtos =
                tariffs.stream().map(this::convertToTariffDto).collect(Collectors.toList());
            logger.info("Получено {} тарифов.", tariffDtos.size());
            return tariffDtos;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех тарифов: {}", e.getMessage(), e);
            throw e;
        }
    }

    private TariffDto convertToTariffDto(Tariff tariff) {
        TariffDto dto = new TariffDto();
        dto.setId(tariff.getId());
        dto.setType(tariff.getType());
        dto.setName(tariff.getName());
        dto.setDescription(tariff.getDescription());
        dto.setSubscriptionPrice(tariff.getPrice());
        dto.setUnitsIncluded(tariff.getUnitsIncluded());
        dto.setValidityPeriodHours(tariff.getValidityPeriodHours());
        dto.setIsSubscription(tariff.getIsSubscription());
        return dto;
    }
}
