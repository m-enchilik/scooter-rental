package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalPointDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalPointNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.RentalPoint;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalPointRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.RentalPointService;
import ru.senla.javacourse.enchilik.scooterrental.core.service.ScooterService;

@Service
public class RentalPointServiceImpl implements RentalPointService {

    private static final Logger logger = LoggerFactory.getLogger(RentalPointServiceImpl.class);

    private final RentalPointRepository rentalPointRepository;
    private final ScooterService scooterService;

    @Autowired
    public RentalPointServiceImpl(
        RentalPointRepository rentalPointRepository, ScooterService scooterService) {
        this.rentalPointRepository = rentalPointRepository;
        this.scooterService = scooterService;
    }

    @Override
    @Transactional
    public RentalPointDto save(RentalPointDto rentalPointDto)
        throws RentalPointNotFoundException {
        logger.info("Попытка создать точку проката с данными: {}", rentalPointDto);
        try {
            RentalPoint rentalPoint = new RentalPoint();
            rentalPoint.setName(rentalPointDto.getName());
            rentalPoint.setAddress(rentalPointDto.getAddress());
            rentalPoint.setLatitude(rentalPointDto.getLatitude());
            rentalPoint.setLongitude(rentalPointDto.getLongitude());

            if (rentalPointDto.getParentPointId() != null) {
                RentalPoint parentPoint =
                    rentalPointRepository
                        .findById(rentalPointDto.getParentPointId())
                        .orElseThrow(
                            () -> {
                                logger.error(
                                    "Родительская точка проката с ID {} не найдена.",
                                    rentalPointDto.getParentPointId());
                                return new RentalPointNotFoundException(
                                    "Родительская точка проката с ID "
                                        + rentalPointDto.getParentPointId()
                                        + " не найдена");
                            });
                rentalPoint.setParentPoint(parentPoint);
            }

            rentalPoint = rentalPointRepository.save(rentalPoint);
            rentalPointDto.setId(rentalPoint.getId());
            logger.info("Точка проката успешно создана с ID: {}", rentalPoint.getId());
            return rentalPointDto;
        } catch (Exception e) {
            logger.error("Ошибка при создании точки проката: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public RentalPointDto findById(Long id) throws RentalPointNotFoundException {
        logger.info("Попытка получить точку проката с ID: {}", id);
        try {
            RentalPoint rentalPoint =
                rentalPointRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Точка проката с ID {} не найдена.", id);
                            return new RentalPointNotFoundException(
                                "Точка проката с ID " + id + " не найдена");
                        });
            RentalPointDto rentalPointDto = convertToRentalPointDto(rentalPoint);
            logger.info("Точка проката с ID {} успешно получена.", id);
            return rentalPointDto;
        } catch (Exception e) {
            logger.error("Ошибка при получении точки проката с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public RentalPointDto update(Long id, RentalPointDto rentalPointDto)
        throws RentalPointNotFoundException {
        logger.info("Попытка обновить точку проката с ID: {}, данные: {}", id, rentalPointDto);
        try {
            RentalPoint rentalPoint =
                rentalPointRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Точка проката с id {} не найдена.", id);
                            return new RentalPointNotFoundException(
                                "Точка проката с id" + id + " не найдена");
                        });

            if (rentalPointDto.getName() != null) {
                rentalPoint.setName(rentalPointDto.getName());
            }
            if (rentalPointDto.getAddress() != null) {
                rentalPoint.setAddress(rentalPointDto.getAddress());
            }
            if (rentalPointDto.getLatitude() != null) {
                rentalPoint.setLatitude(rentalPointDto.getLatitude());
            }
            if (rentalPointDto.getLongitude() != null) {
                rentalPoint.setLongitude(rentalPointDto.getLongitude());
            }
            if (rentalPointDto.getParentPointId() != null) {
                RentalPoint parentPoint =
                    rentalPointRepository
                        .findById(rentalPointDto.getParentPointId())
                        .orElseThrow(
                            () -> {
                                logger.error(
                                    "Родительская точка проката с ID {} не найдена.",
                                    rentalPointDto.getParentPointId());
                                return new RentalPointNotFoundException(
                                    "Родительская точка проката с ID "
                                        + rentalPointDto.getParentPointId()
                                        + " не найдена");
                            });
                rentalPoint.setParentPoint(parentPoint);
            }
            rentalPointRepository.save(rentalPoint);
            logger.info("Точка проката с ID {} успешно обновлена.", id);
            return rentalPointDto;
        } catch (Exception e) {
            logger.error("Ошибка при обновлении точки проката с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws RentalPointNotFoundException {
        logger.info("Попытка удалить точку проката с ID: {}", id);
        try {
            RentalPoint rentalPoint =
                rentalPointRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Точка проката с ID {} не найдена.", id);
                            return new RentalPointNotFoundException(
                                "Точка проката с ID " + id + " не найдена");
                        });
            rentalPointRepository.delete(id);
            logger.info("Точка проката с ID {} успешно удалена.", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении точки проката с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<RentalPointDto> findAll() {
        logger.info("Попытка получить все точки проката.");
        try {
            List<RentalPoint> rootRentalPoints = rentalPointRepository.findAll();

            List<RentalPointDto> rentalPointDtos =
                rootRentalPoints.stream()
                    .map(this::convertToRentalPointDtoRecursively)
                    .collect(Collectors.toList());
            logger.info("Получено {} точек проката.", rentalPointDtos.size());
            return rentalPointDtos;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех точек проката: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<RentalPointDto> getRootRentalPoints() {
        logger.info("Попытка получить корневые точки проката.");
        try {
            List<RentalPointDto> rootRentalPoints =
                rentalPointRepository.findRootRentalPoints().stream()
                    .map(this::convertToRentalPointDto)
                    .collect(Collectors.toList());
            logger.info("Получено {} корневых точек проката.", rootRentalPoints.size());
            return rootRentalPoints;
        } catch (Exception e) {
            logger.error("Ошибка при получении корневых точек проката: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<RentalPointDto> getChildRentalPoints(Long parentId)
        throws RentalPointNotFoundException {
        logger.info(
            "Попытка получить дочерние точки проката для родительской точки с ID: {}",
            parentId);
        try {
            if (!rentalPointRepository.existsById(parentId)) {
                logger.warn("Точки проката с ID {} не существует.", parentId);
                throw new RentalPointNotFoundException(
                    "Точки проката с ID" + parentId + " не существует");
            }
            List<RentalPointDto> childRentalPoints =
                rentalPointRepository.findByParentPointId(parentId).stream()
                    .map(this::convertToRentalPointDto)
                    .collect(Collectors.toList());
            logger.info(
                "Получено {} дочерних точек проката для родительской точки с ID: {}",
                childRentalPoints.size(),
                parentId);
            return childRentalPoints;
        } catch (Exception e) {
            logger.error(
                "Ошибка при получении дочерних точек проката для родительской точки с ID {}: {}",
                parentId,
                e.getMessage(),
                e);
            throw e;
        }
    }

    private RentalPointDto convertToRentalPointDto(RentalPoint rentalPoint) {
        RentalPointDto dto = new RentalPointDto();
        dto.setId(rentalPoint.getId());
        dto.setName(rentalPoint.getName());
        dto.setAddress(rentalPoint.getAddress());
        dto.setLatitude(rentalPoint.getLatitude());
        dto.setLongitude(rentalPoint.getLongitude());
        if (rentalPoint.getParentPoint() != null) {
            dto.setParentPointId(rentalPoint.getParentPoint().getId());
        }

        return dto;
    }

    private RentalPointDto convertToRentalPointDtoRecursively(RentalPoint rentalPoint) {
        RentalPointDto dto = convertToRentalPointDto(rentalPoint);
        if (rentalPoint.getChildPoints() != null) {
            dto.setChildPoints(
                rentalPoint.getChildPoints().stream()
                    .map(this::convertToRentalPointDtoRecursively)
                    .toList());
        }
        return dto;
    }
}
