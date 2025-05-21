package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.ScooterDto;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.ScooterStatus;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalPointNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalPointRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.ScooterRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.TariffRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.ScooterService;

@Service
public class ScooterServiceImpl implements ScooterService {

    private static final Logger logger = LoggerFactory.getLogger(ScooterServiceImpl.class);

    private final ScooterRepository scooterRepository;
    private final RentalPointRepository rentalPointRepository;
    private final TariffRepository tariffRepository;

    @Autowired
    public ScooterServiceImpl(
        ScooterRepository scooterRepository,
        RentalPointRepository rentalPointRepository,
        TariffRepository tariffRepository) {
        this.scooterRepository = scooterRepository;
        this.rentalPointRepository = rentalPointRepository;
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public ScooterDto createScooter(ScooterDto scooterDto)
        throws RentalPointNotFoundException, TariffNotFoundException {
        logger.info("Попытка создать новый самокат с данными: {}", scooterDto);
        try {
            if (scooterRepository.existsBySerialNumber(scooterDto.getSerialNumber())) {
                logger.error(
                    "Самокат с серийным номером {} уже существует.",
                    scooterDto.getSerialNumber());
                throw new IllegalArgumentException(
                    "Самокат с таким серийным номером уже существует");
            }
            Scooter scooter = new Scooter();
            scooter.setModel(scooterDto.getModel());
            scooter.setSerialNumber(scooterDto.getSerialNumber());
            scooter.setStatus(scooterDto.getStatus());
            scooter.setChargeLevel(scooterDto.getChargeLevel());
            scooter.setMileage(scooterDto.getMileage());

            scooter = scooterRepository.save(scooter);
            scooterDto.setId(scooter.getId());

            logger.info("Самокат успешно создан с ID: {}", scooter.getId());
            return scooterDto;
        } catch (Exception e) {
            logger.error("Ошибка при создании самоката: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ScooterDto getScooterById(Long id) throws ScooterNotFoundException {
        logger.info("Попытка получить самокат с ID: {}", id);
        try {
            Scooter scooter =
                scooterRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Самокат с ID {} не найден.", id);
                            return new ScooterNotFoundException(
                                "Самокат с ID " + id + " не найден");
                        });
            ScooterDto scooterDto = convertToScooterDto(scooter);
            logger.info("Самокат с ID {} успешно получен.", id);
            return scooterDto;

        } catch (Exception e) {
            logger.error("Ошибка при получении самоката с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public ScooterDto updateScooter(Long id, ScooterDto scooterDto)
        throws ScooterNotFoundException, RentalPointNotFoundException, TariffNotFoundException {
        logger.info("Попытка обновить самокат с ID: {}, данные: {}", id, scooterDto);
        try {
            Scooter scooter =
                scooterRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Самокат с ID {} не найден.", id);
                            return new ScooterNotFoundException(
                                "Самокат с ID " + id + " не найден");
                        });

            if (scooterDto.getModel() != null) {
                scooter.setModel(scooterDto.getModel());
            }
            if (scooterDto.getSerialNumber() != null) {
                if (!scooterDto.getSerialNumber().equals(scooter.getSerialNumber())
                    && scooterRepository.existsBySerialNumber(scooterDto.getSerialNumber())) {
                    logger.error("Серийный номер {} уже занят.", scooterDto.getSerialNumber());
                    throw new IllegalArgumentException(
                        "Серийный номер " + scooterDto.getSerialNumber() + " уже занят");
                }
                scooter.setSerialNumber(scooterDto.getSerialNumber());
            }
            if (scooterDto.getStatus() != null) {
                scooter.setStatus(scooterDto.getStatus());
            }
            if (scooterDto.getChargeLevel() != null) {
                scooter.setChargeLevel(scooterDto.getChargeLevel());
            }
            if (scooterDto.getMileage() != null) {
                scooter.setMileage(scooterDto.getMileage());
            }

            scooter = scooterRepository.save(scooter);
            logger.info("Самокат с ID {} успешно обновлен.", id);
            return convertToScooterDto(scooter);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении самоката с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteScooter(Long id) throws ScooterNotFoundException {
        logger.info("Попытка удалить самокат с ID: {}", id);
        try {
            Scooter scooter =
                scooterRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Самокат с ID {} не найден.", id);
                            return new ScooterNotFoundException(
                                "Самокат с ID " + id + " не найден");
                        });
            scooterRepository.delete(scooter.getId());
            logger.info("Самокат с ID {} успешно удален.", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении самоката с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<ScooterDto> getAllScooters() {
        logger.info("Попытка получить все самокаты.");
        try {
            List<ScooterDto> scooters =
                scooterRepository.findAll().stream()
                    .map(this::convertToScooterDto)
                    .collect(Collectors.toList());
            logger.info("Получено {} самокатов.", scooters.size());
            return scooters;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех самокатов: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<ScooterDto> getScootersByRentalPoint(Long rentalPointId)
        throws RentalPointNotFoundException {
        logger.info("Попытка получить самокаты для точки проката с ID: {}", rentalPointId);
        try {
            if (!rentalPointRepository.existsById(rentalPointId)) {
                logger.warn("Точка проката с ID {} не найдена.", rentalPointId);
                throw new RentalPointNotFoundException(
                    "Точка проката с ID " + rentalPointId + " не найдена");
            }
            List<ScooterDto> scooters =
                scooterRepository.findByRentalPointId(rentalPointId).stream()
                    .map(this::convertToScooterDto)
                    .collect(Collectors.toList());
            logger.info(
                "Получено {} самокатов для точки проката с ID {}.",
                scooters.size(),
                rentalPointId);
            return scooters;
        } catch (Exception e) {
            logger.error(
                "Ошибка при получении самокатов для точки проката с ID {}: {}",
                rentalPointId,
                e.getMessage(),
                e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void updateScooterStatus(Long scooterId, ScooterStatus newStatus)
        throws ScooterNotFoundException {
        logger.info("Попытка обновить статус самоката с ID: {} на {}", scooterId, newStatus);
        try {
            Scooter scooter =
                scooterRepository
                    .findById(scooterId)
                    .orElseThrow(
                        () -> {
                            logger.warn("Самокат с ID {} не найден.", scooterId);
                            return new ScooterNotFoundException(
                                "Самокат с ID " + scooterId + " не найден");
                        });
            scooter.setStatus(newStatus);
            scooterRepository.save(scooter);
            logger.info("Статус самоката с ID {} успешно обновлен на {}", scooterId, newStatus);
        } catch (Exception e) {
            logger.error(
                "Ошибка при обновлении статуса самоката с ID {}: {}",
                scooterId,
                e.getMessage(),
                e);
            throw e;
        }
    }

    private ScooterDto convertToScooterDto(Scooter scooter) {
        ScooterDto dto = new ScooterDto();
        dto.setId(scooter.getId());
        dto.setModel(scooter.getModel());
        dto.setSerialNumber(scooter.getSerialNumber());
        dto.setStatus(scooter.getStatus());
        dto.setChargeLevel(scooter.getChargeLevel());
        dto.setMileage(scooter.getMileage());
        return dto;
    }
}
