package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalPointDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalPointNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.RentalPoint;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalPointRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.ScooterService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    RentalPointServiceImpl.class,
})
public class RentalPointServiceImplTest {

    @Autowired
    private RentalPointServiceImpl service;

    @MockitoBean
    private RentalPointRepository repo;
    @MockitoBean
    private ScooterService scooterService;

    @Test
    public void contextTest() {
        assertNotNull(service);
        assertNotNull(repo);
        assertNotNull(scooterService);
    }

    @Test
    void save() {
        RentalPoint rentalPoint = generateTestData();
        repo.save(rentalPoint);
        verify(repo, times(1)).save(rentalPoint);
        verify(repo, times(1)).save(any());
    }


    @Test
    void findByIdSuccess() {
        RentalPoint rentalPoint = generateTestData();
        doReturn(Optional.of(rentalPoint)).when(repo).findById(123L);

        RentalPointDto found = service.findById(123L);

        assertEquals(rentalPoint.getId(), found.getId());
    }

    @Test
    void findByIdFail() {
        doReturn(Optional.empty()).when(repo).findById(any());

        RentalPointNotFoundException thrown = assertThrows(RentalPointNotFoundException.class, () -> {
            service.findById(123L);
        });

        String message = thrown.getMessage();
        assertNotNull(message);
        assertTrue(message.contains("123"));
        assertTrue(message.toLowerCase().contains("не найдена"));
    }

    @Test
    void update() {
        RentalPoint rentalPoint = generateTestData();
        Long id = new Random().nextLong();
        rentalPoint.setId(id);
        doReturn(Optional.of(rentalPoint)).when(repo).findById(id);
        service.update(id, convertToDto(rentalPoint));
        verify(repo, times(1)).update(rentalPoint);
        verify(repo, times(1)).update(any());
    }

    @Test
    void delete() {
        RentalPoint rentalPoint = generateTestData();
        Long id = new Random().nextLong();
        rentalPoint.setId(id);
        doReturn(Optional.of(rentalPoint)).when(repo).findById(id);

        service.delete(id);
        verify(repo, times(1)).delete(id);
        verify(repo, times(1)).delete(any());
    }

    @Test
    void findAll() {
        RentalPoint rentalPoint1 = generateTestData();
        RentalPoint rentalPoint2 = generateTestData();
        RentalPoint rentalPoint3 = generateTestData();
        List<RentalPoint> rentalPoints = List.of(rentalPoint1, rentalPoint2, rentalPoint3);

        doReturn(rentalPoints).when(repo).findAll();

        List<RentalPointDto> found = service.findAll();
        assertTrue(found.size() == rentalPoints.size());
        assertTrue(found.stream().map(RentalPointDto::getId).toList()
            .containsAll(rentalPoints.stream().map(RentalPoint::getId).toList()));
    }

    @Test
    void getRootRentalPoints() {
        RentalPoint rentalPoint1 = generateTestData();
        RentalPoint rentalPoint2 = generateTestData();
        RentalPoint rentalPoint11 = generateTestData();
        RentalPoint rentalPoint12 = generateTestData();
        rentalPoint1.setChildPoints(List.of(rentalPoint11, rentalPoint12));

        List<RentalPoint> rentalPoints = List.of(rentalPoint1, rentalPoint2);

        doReturn(rentalPoints).when(repo).findAll();

        List<RentalPointDto> found = service.findAll();
        RentalPointDto foundRentalPointChildren = found.stream()
            .filter(it -> it.getId().equals(rentalPoint1.getId()))
            .findFirst()
            .get();
        compareByDetails(rentalPoints, found);
        compareByDetails(
            rentalPoint1.getChildPoints(),
            foundRentalPointChildren.getChildPoints()
        );
    }

    @Test
    void getChildRentalPoints() {
        RentalPoint rentalPoint1 = generateTestData();
        RentalPoint rentalPoint2 = generateTestData();
        RentalPoint rentalPoint3 = generateTestData();
        List<RentalPoint> rentalPoints = List.of(rentalPoint1, rentalPoint2, rentalPoint3);

        doReturn(rentalPoints).when(repo).findAll();

        List<RentalPointDto> found = service.findAll();
        assertTrue(found.size() == rentalPoints.size());
        assertTrue(found.stream().map(RentalPointDto::getId).toList()
            .containsAll(rentalPoints.stream().map(RentalPoint::getId).toList()));
    }

    private static void compareByDetails(List<RentalPoint> rentalPoints, List<RentalPointDto> found) {
        assertTrue(found.size() == rentalPoints.size());
        assertTrue(found.stream().map(RentalPointDto::getId).toList()
            .containsAll(rentalPoints.stream().map(RentalPoint::getId).toList()));
    }

    private RentalPoint generateTestData() {
        String name = UUID.randomUUID().toString();
        Long id = new Random().nextLong();
        RentalPoint rentalPoint = new RentalPoint();
        rentalPoint.setId(id);
        rentalPoint.setName(name);
        return rentalPoint;
    }

    private RentalPointDto convertToDto(RentalPoint rentalPoint) {
        RentalPointDto dto = new RentalPointDto();
        dto.setId(rentalPoint.getId());
        dto.setName(rentalPoint.getName());
        return dto;
    }
}