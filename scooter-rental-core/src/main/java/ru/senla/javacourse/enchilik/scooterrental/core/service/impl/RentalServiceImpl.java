package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalDto;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.ScooterStatus;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.ScooterRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.TariffRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.RentalService;
import ru.senla.javacourse.enchilik.scooterrental.core.service.ScooterService;

@Service
public class RentalServiceImpl implements RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final ScooterService scooterService;
    private final TariffRepository tariffRepository;

    @Autowired
    public RentalServiceImpl(
        RentalRepository rentalRepository,
        UserRepository userRepository,
        ScooterRepository scooterRepository,
        ScooterService scooterService,
        TariffRepository tariffRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.scooterRepository = scooterRepository;
        this.scooterService = scooterService;
        this.tariffRepository = tariffRepository;
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

            Tariff tariff =
                tariffRepository
                    .findById(rentalDto.getTariffId())
                    .orElseThrow(() -> new IllegalArgumentException("Тариф не найден"));

            Rental rental = new Rental();
            rental.setUser(user);
            rental.setScooter(scooter);
            rental.setStartTime(rentalDto.getStartTime());
            rental.setStartMileage(scooter.getMileage());
            rental.setTariff(tariff);

            scooterService.updateScooterStatus(scooter.getId(), ScooterStatus.IN_USE);

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

            rental.setEndTime(LocalDateTime.now());
            Scooter scooter = rental.getScooter();
            rental.setEndMileage(scooter.getMileage());

            BigDecimal cost = calculateCost(rental);
            rental.setTotalCost(cost);

            scooterService.updateScooterStatus(scooter.getId(), ScooterStatus.AVAILABLE);
            rentalRepository.save(rental);

            RentalDto rentalDto = new RentalDto();
            rentalDto.setId(rental.getId());
            rentalDto.setUserId(rental.getUser().getId());
            rentalDto.setScooterId(rental.getScooter().getId());
            rentalDto.setStartTime(rental.getStartTime());
            rentalDto.setEndTime(rental.getEndTime());
            rentalDto.setStartMileage(rental.getStartMileage());
            rentalDto.setEndMileage(rental.getEndMileage());
            rentalDto.setTotalCost(rental.getTotalCost());
            rentalDto.setTariffId(rental.getTariff().getId());

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
            List<RentalDto> rentals =
                rentalRepository.findByScooterId(scooterId).stream()
                    .map(this::convertToRentalDto)
                    .collect(Collectors.toList());
            logger.info(
                "Получено {} записей аренды для самоката с ID {}.", rentals.size(), scooterId);
            return rentals;
        } catch (Exception e) {
            logger.error(
                "Ошибка при получении записей аренды для самоката с ID {}: {}",
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
            List<RentalDto> rentals =
                rentalRepository.findByScooterId(scooterId).stream()
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

    private BigDecimal calculateCost(Rental rental) {
        //        Duration duration = Duration.between(rental.getStartTime(), rental.getEndTime());
        //        double hours = duration.toMinutes() / 60.0;
        //        BigDecimal cost;
        //        Tariff tariff = rental.getTariff();
        //
        //        if (tariff.getIsSubscription()) {
        //            cost = tariff.getPrice();
        //        } else {
        //            TODO: Доработать метод
        //            cost = hours * tariff.getPricePerHour();
        //        }
        //скидка
        //            TODO: Доработать метод
        //        if (tariff.getDiscount() != null && tariff.getDiscount() > 0) {
        //            cost = cost * (1 - tariff.getDiscount());
        //        }
        //        return cost;
        return BigDecimal.valueOf(10);
    }

    private RentalDto convertToRentalDto(Rental rental) {
        RentalDto dto = new RentalDto();
        dto.setId(rental.getId());
        dto.setUserId(rental.getUser().getId());
        dto.setUserUsername(rental.getUser().getUsername());
        dto.setScooterId(rental.getScooter().getId());
        dto.setScooterModel(rental.getScooter().getModel());
        dto.setStartTime(rental.getStartTime());
        dto.setEndTime(rental.getEndTime());
        dto.setStartMileage(rental.getStartMileage());
        dto.setEndMileage(rental.getEndMileage());
        dto.setTotalCost(rental.getTotalCost());
        if (rental.getTariff() != null) {
            dto.setTariffId(rental.getTariff().getId());
            dto.setTariffName(rental.getTariff().getName());
        }
        return dto;
    }
}
