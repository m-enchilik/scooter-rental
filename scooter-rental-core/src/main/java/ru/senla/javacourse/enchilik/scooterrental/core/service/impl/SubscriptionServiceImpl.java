package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.SubscriptionDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.SubscriptionNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.SubscriptionRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.TariffRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository,
                                   UserRepository userRepository,
                                   TariffRepository tariffRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.tariffRepository = tariffRepository;
    }


    @Override
    public SubscriptionDto save(SubscriptionDto subscriptionDto) throws
        UserNotFoundException, TariffNotFoundException {
        logger.info("Попытка создать подписку: {}", subscriptionDto);

        try {
            User user = userRepository.findById(subscriptionDto.getUserId())
                .orElseThrow(() -> {
                    logger.warn("Пользователь с ID {} не найден.", subscriptionDto.getUserId());
                    return new UserNotFoundException("Пользователь с ID "
                        + subscriptionDto.getUserId()
                        + " не найден");
                });

            Tariff tariff = tariffRepository.findById(subscriptionDto.getTariffId())
                .orElseThrow(() -> {
                    logger.error("Тариф с ID {} не найден.", subscriptionDto.getTariffId());
                    return new TariffNotFoundException(
                        "Тариф с ID "
                            + subscriptionDto.getTariffId()
                            + " не найден");
                });

            Subscription subscription = new Subscription();
            subscription.setRestUnits(subscriptionDto.getRestUnits());
            subscription.setExpirationTime(subscriptionDto.getExpirationTime());
            subscription.setUser(user);
            subscription.setTariff(tariff);

            Subscription saved = subscriptionRepository.save(subscription);

            subscriptionDto.setId(saved.getId());
            logger.info("Подписка успешно создана с ID: {}", subscription.getId());
            return subscriptionDto;
        } catch (Exception e) {
            logger.error("Ошибка при создании подписки: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SubscriptionDto findById(Long id) throws SubscriptionNotFoundException {
        logger.info("Попытка получить подписку с ID: {}", id);
        try {
            Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Подписка с ID {} не найдена.", id);
                    return new SubscriptionNotFoundException("Подписка с ID " + id + " не найдена");
                });

            SubscriptionDto subscriptionDto = convertToSubscriptionDto(subscription);

            logger.info("Подписка с ID {} успешно получена.", id);
            return subscriptionDto;
        } catch (Exception e) {
            logger.error("Ошибка при получении подписки с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SubscriptionDto update(Long id, SubscriptionDto subscriptionDto) throws SubscriptionNotFoundException {
        logger.info("Попытка обновить подписку с ID: {}", id);
        try {
            Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Подписка с ID {} не найдена.", id);
                    return new SubscriptionNotFoundException("Подписка с ID " + id + " не найдена");
                });

            if (subscriptionDto.getExpirationTime() != null) {
                subscription.setExpirationTime(subscriptionDto.getExpirationTime());
            }
            if (subscription.getRestUnits() != null) {
                subscription.setRestUnits(subscriptionDto.getRestUnits());
            }

            Subscription update = subscriptionRepository.update(subscription);

            SubscriptionDto returnedDto = convertToSubscriptionDto(update);

            logger.info("Подписка с ID {} успешно обновлена.", id);
            return returnedDto;
        } catch (Exception e) {
            logger.error("Ошибка при обновлении подписки с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(Long id) throws SubscriptionNotFoundException {
        logger.info("Попытка обновить подписку с ID: {}", id);
        try {
            subscriptionRepository.delete(id);
            logger.info("Подписка с ID {} успешно удалена.", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении подписки с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public List<SubscriptionDto> findAll() {
        logger.info("Попытка получить все подписки.");
        try {
            List<SubscriptionDto> dtos =
                subscriptionRepository.findAll().stream()
                    .map(this::convertToSubscriptionDto)
                    .collect(Collectors.toList());
            logger.info("Получено {} подписок.", dtos.size());
            return dtos;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех подписок: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<SubscriptionDto> findByUserId(Long id) {
        logger.info("Попытка получить подписки пользователя с ID: {}", id);
        try {
            List<SubscriptionDto> dtos =
                subscriptionRepository.findByUserId(id).stream()
                    .map(this::convertToSubscriptionDto)
                    .collect(Collectors.toList());
            logger.info("Получено {} подписок.", dtos.size());
            return dtos;
        } catch (Exception e) {
            logger.error("Ошибка при получении подписок пользователя с ID: {}", e.getMessage(), e);
            throw e;
        }
    }

    private SubscriptionDto convertToSubscriptionDto(Subscription saved) {
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(saved.getId());
        subscriptionDto.setUserId(saved.getUser().getId());
        subscriptionDto.setTariffId(saved.getTariff().getId());
        subscriptionDto.setRestUnits(saved.getRestUnits());
        subscriptionDto.setExpirationTime(saved.getExpirationTime());
        return subscriptionDto;
    }
}
