package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalDto;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.ScooterStatus;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.SubscriptionNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.SubscriptionWrongUserException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserRentalBlockedException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.ScooterRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.SubscriptionRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.TariffRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.RentalService;
import ru.senla.javacourse.enchilik.scooterrental.core.service.ScooterService;
import ru.senla.javacourse.enchilik.scooterrental.core.tariff.TariffStrategy;
import ru.senla.javacourse.enchilik.scooterrental.core.tariff.TariffStrategyResolver;

@Service
public class RentalServiceImpl implements RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final ScooterService scooterService;
    private final SubscriptionRepository subscriptionRepository;
    private final TariffStrategyResolver tariffStrategyResolver;

    @Autowired
    public RentalServiceImpl(
        RentalRepository rentalRepository,
        UserRepository userRepository,
        ScooterRepository scooterRepository,
        ScooterService scooterService,
        SubscriptionRepository subscriptionRepository,
        TariffStrategyResolver tariffStrategyResolver
    ) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.scooterRepository = scooterRepository;
        this.scooterService = scooterService;
        this.subscriptionRepository = subscriptionRepository;
        this.tariffStrategyResolver = tariffStrategyResolver;
    }

    @Override
    @Transactional
    public RentalDto save(RentalDto rentalDto)
        throws UserNotFoundException, ScooterNotFoundException {
        logger.info("Попытка создать запись аренды: {}", rentalDto);
        try {
            User user =
                userRepository
                    .findById(rentalDto.getUserId())
                    .orElseThrow(
                        () -> {
                            logger.warn(
                                "Пользователь с ID {} не найден.",
                                rentalDto.getUserId());
                            return new UserNotFoundException(
                                "Пользователь с ID "
                                    + rentalDto.getUserId()
                                    + " не найден");
                        });
            Scooter scooter =
                scooterRepository
                    .findById(rentalDto.getScooterId())
                    .orElseThrow(
                        () -> {
                            logger.warn(
                                "Самокат с ID {} не найден.",
                                rentalDto.getScooterId());
                            return new ScooterNotFoundException(
                                "Самокат с ID "
                                    + rentalDto.getScooterId()
                                    + " не найден");
                        });

            if (scooter.getStatus() != ScooterStatus.AVAILABLE) {
                logger.error("Самокат с ID {} недоступен для аренды.", scooter.getId());
                throw new IllegalArgumentException("Самокат недоступен для аренды");
            }

            Subscription subscription =
                subscriptionRepository
                    .findById(rentalDto.getSubscriptionId())
                    .orElseThrow(() -> new IllegalArgumentException("Подписка не найдена"));

            Rental rental = new Rental();
            rental.setUser(user);
            rental.setScooter(scooter);
            rental.setStartTime(rentalDto.getStartTime());
            rental.setEndTime(rentalDto.getEndTime());
            rental.setStartMileage(rentalDto.getStartMileage());
            rental.setEndMileage(rentalDto.getEndMileage());
            rental.setSubscription(subscription);
            rental.setTotalCost(rentalDto.getTotalCost());

            rental = rentalRepository.save(rental);
            rentalDto.setId(rental.getId());
            logger.info("Запись аренды успешно создана с ID: {}", rental.getId());
            return rentalDto;
        } catch (Exception e) {
            logger.error("Ошибка при создании записи аренды: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public RentalDto update(Long id, RentalDto rentalDto) throws RentalNotFoundException {
        logger.info("Попытка создать запись аренды: {}", rentalDto);
        try {
            Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("Аренда с ID {} не найдена.", id);
                    return new RentalNotFoundException("Аренда с ID " + id + " не найдена");
                });
            if (rentalDto.getStartTime() != null) {
                rental.setStartTime(rentalDto.getStartTime());
            }
            if (rentalDto.getEndTime() != null) {
                rental.setEndTime(rentalDto.getEndTime());
            }
            if (rentalDto.getStartMileage() != null) {
                rental.setStartMileage(rentalDto.getStartMileage());
            }
            if (rentalDto.getEndMileage() != null) {
                rental.setEndMileage(rentalDto.getEndMileage());
            }
            if (rentalDto.getTotalCost() != null) {
                rental.setTotalCost(rentalDto.getTotalCost());
            }

            rental = rentalRepository.update(rental);

            logger.info("Обновление аренды с ID {} успешно получена.", id);
            return convertToRentalDto(rental);

        } catch (Exception e) {
            logger.error("Ошибка обновления записи аренды с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public RentalDto getRentalById(Long id) throws RentalNotFoundException {
        logger.info("Попытка получить запись аренды с ID: {}", id);
        try {
            Rental rental =
                rentalRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Аренда с ID {} не найдена.", id);
                            return new RentalNotFoundException(
                                "Аренда с ID " + id + " не найдена");
                        });
            RentalDto rentalDto = convertToRentalDto(rental);
            logger.info("Запись аренды с ID {} успешно получена.", id);
            return rentalDto;
        } catch (Exception e) {
            logger.error("Ошибка при получении записи аренды с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(Long id) throws RentalNotFoundException {
        logger.info("Попытка удалить аренду с ID: {}", id);
        try {
            Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("Аренда с ID {} не найдена.", id);
                    return new RentalNotFoundException("Аренда с ID " + id + " не найдена.");
                }
            );
            rentalRepository.delete(id);
            logger.info("Аренда с ID {} успешно удалена.", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении записи аренды с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public RentalDto startRental(User user, Long subscriptionId, Long scooterId)
        throws SubscriptionNotFoundException, ScooterNotFoundException {
        logger.info("Попытка начать аренду по подписке ID: {}", subscriptionId);

        if (user.getRentBlocked()) {
            throw new UserRentalBlockedException("User can't get a rent: '%s'".formatted(user.getUsername()));
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new SubscriptionNotFoundException(
                "subscription not found by id: '%s'".formatted(subscriptionId)
            ));

        if (user.getId() != subscription.getUser().getId()) {
            throw new SubscriptionWrongUserException("Wrong subscription: different user");
        }

        Scooter scooter = scooterRepository
            .findById(scooterId)
            .orElseThrow(() ->
                new ScooterNotFoundException("scooter not found by id: '%s'".formatted(scooterId)));

        Tariff tariff = subscription.getTariff();

        TariffStrategy tariffStrategy = tariffStrategyResolver.resolve(tariff.getType());

        LocalDateTime timeLimit = tariffStrategy.getTimeLimit(user, subscription);

        scooterService.updateScooterStatus(scooter.getId(), ScooterStatus.IN_USE);

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setScooter(scooter);
        rental.setSubscription(subscription);
        rental.setExpirationTime(timeLimit);
        rental.setStartMileage(scooter.getMileage());
        rental.setStartTime(LocalDateTime.now());

        rental = rentalRepository.save(rental);

        return convertToRentalDto(rental);
    }

    @Override
    @Transactional
    public RentalDto endRental(Long id) throws RentalNotFoundException, ScooterNotFoundException {
        logger.info("Попытка завершить аренду с ID: {}", id);
        try {
            Rental rental =
                rentalRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Аренда с ID {} не найдена.", id);
                            return new RentalNotFoundException(
                                "Аренда с ID " + id + " не найдена");
                        });

            if (rental.getEndTime() != null) {
                logger.error("Аренда с ID {} уже завершена.", id);
                throw new IllegalArgumentException("Аренда уже завершена");
            }

            Tariff tariff = rental.getSubscription().getTariff();
            TariffStrategy tariffStrategy = tariffStrategyResolver.resolve(tariff.getType());

            rental.setEndTime(LocalDateTime.now());
            Scooter scooter = rental.getScooter();
            rental.setEndMileage(scooter.getMileage());

            long minutesUsed = ChronoUnit.MINUTES.between(rental.getStartTime(), rental.getEndTime());

            tariffStrategy.finish(rental, minutesUsed);
            rental = rentalRepository.save(rental);

            scooterService.updateScooterStatus(scooter.getId(), ScooterStatus.AVAILABLE);

            RentalDto rentalDto = convertToRentalDto(rental);

            logger.info("Аренда с ID {} успешно завершена.", id);
            return rentalDto;
        } catch (Exception e) {
            logger.error("Ошибка при завершении аренды с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<RentalDto> getAllRentals() {
        logger.info("Попытка получить все записи аренды.");
        try {
            List<RentalDto> rentals =
                rentalRepository.findAll().stream()
                    .map(this::convertToRentalDto)
                    .collect(Collectors.toList());
            logger.info("Получено {} записей аренды.", rentals.size());
            return rentals;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех записей аренды: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<RentalDto> getRentalsByUser(Long userId) {
        logger.info("Попытка получить записи аренды для пользователя с ID: {}", userId);
        try {
            List<RentalDto> rentals =
                rentalRepository.findByUserId(userId).stream()
                    .map(this::convertToRentalDto)
                    .collect(Collectors.toList());
            logger.info(
                "Получено {} записей аренды для пользователя с ID {}.", rentals.size(), userId);
            return rentals;
        } catch (Exception e) {
            logger.error(
                "Ошибка при получении записей аренды для пользователя с ID {}: {}",
                userId,
                e.getMessage(),
                e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<RentalDto> getRentalsByScooter(Long scooterId) {
        logger.info("Попытка получить записи аренды для самоката с ID: {}", scooterId);
        try {
            List<RentalDto> rentals = rentalRepository.findByScooterId(scooterId).stream()
                .map(this::convertToRentalDto)
                .collect(Collectors.toList());
            logger.info("Получено {} записей аренды для самоката с ID {}.", rentals.size(), scooterId);
            return rentals;
        } catch (Exception e) {
            logger.error("Ошибка при получении записей аренды для самоката с ID {}: {}",
                scooterId,
                e.getMessage(),
                e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<RentalDto> getRentalHistoryByScooter(Long scooterId) {
        logger.info("Попытка получить историю аренды для самоката с ID: {}", scooterId);
        try {
            List<RentalDto> rentals = rentalRepository.findByScooterId(scooterId).stream()
                .map(this::convertToRentalDto)
                .collect(Collectors.toList());
            logger.info(
                "Получено {} записей истории аренды для самоката с ID {}.",
                rentals.size(),
                scooterId);
            return rentals;
        } catch (Exception e) {
            logger.error(
                "Ошибка при получении истории аренды для самоката с ID {}: {}",
                scooterId,
                e.getMessage(),
                e);
            throw e;
        }
    }

    private RentalDto convertToRentalDto(Rental rental) {
        RentalDto dto = new RentalDto();
        dto.setId(rental.getId());
        dto.setUserId(rental.getUser().getId());
        dto.setUsername(rental.getUser().getUsername());
        dto.setScooterId(rental.getScooter().getId());
        dto.setScooterModel(rental.getScooter().getModel());
        dto.setStartTime(rental.getStartTime());
        dto.setEndTime(rental.getEndTime());
        dto.setStartMileage(rental.getStartMileage());
        dto.setEndMileage(rental.getEndMileage());
        dto.setTotalCost(rental.getTotalCost());
        if (rental.getSubscription() != null) {
            dto.setSubscriptionId(rental.getSubscription().getId());
            dto.setTariffName(rental.getSubscription().getTariff().getName());
        }
        return dto;
    }
}
