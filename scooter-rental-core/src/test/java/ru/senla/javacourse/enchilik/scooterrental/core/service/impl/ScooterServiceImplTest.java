package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.ScooterDto;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.ScooterStatus;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.RentalPoint;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalPointRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.ScooterRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    ScooterServiceImpl.class,
})
class ScooterServiceImplTest {
    @Autowired
    private ScooterServiceImpl service;

    @MockitoBean
    private ScooterRepository repo;

    @MockitoBean
    private RentalPointRepository rentalPointRepository;

    public void contextTest() {
        assertNotNull(service);
        assertNotNull(repo);
        assertNotNull(rentalPointRepository);
    }

    @Test
    void createScooter() {
        Scooter scooter = generateFullTestData();
        repo.save(scooter);
        verify(repo, times(1)).save(scooter);
        verify(repo, times(1)).save(any());
    }


    @Test
    void findByIdSuccess() {
        Scooter scooter = generateFullTestData();
        doReturn(Optional.of(scooter)).when(repo).findById(123L);

        ScooterDto found = service.getScooterById(123L);

        assertEquals(scooter.getId(), found.getId());
    }

    @Test
    void findByIdFail() {
        doReturn(Optional.empty()).when(repo).findById(any());

        ScooterNotFoundException thrown = assertThrows(ScooterNotFoundException.class, () -> {
            service.getScooterById(123L);
        });

        String message = thrown.getMessage();
        assertNotNull(message);
        assertTrue(message.contains("123"));
        assertTrue(message.toLowerCase().contains("не найден"));
    }

    @Test
    void updateScooter() {
        Scooter scooter = generateFullTestData();
        doReturn(Optional.of(scooter)).when(repo).findById(scooter.getId());
        doReturn(scooter).when(repo).update(scooter);
        service.updateScooter(scooter.getId(), convertToDto(scooter));
        verify(repo, times(1)).update(scooter);
        verify(repo, times(1)).update(any());
    }


    @Test
    void deleteScooter() {
        Scooter scooter = generateFullTestData();
        doReturn(Optional.of(scooter)).when(repo).findById(scooter.getId());
        service.deleteScooter(scooter.getId());
        verify(repo, times(1)).delete(scooter.getId());
        verify(repo, times(1)).delete(any());
    }

    @Test
    void getAllScooters() {
        Scooter scooter1 = generateFullTestData();
        Scooter scooter2 = generateFullTestData();
        Scooter scooter3 = generateFullTestData();

        List<Scooter> rental = List.of(scooter1, scooter2, scooter3);

        doReturn(rental).when(repo).findAll();

        List<ScooterDto> found = service.getAllScooters();
        assertTrue(found.size() == rental.size());
        assertTrue(found.stream().map(ScooterDto::getId).toList()
            .containsAll(rental.stream().map(Scooter::getId).toList()));
    }

    @Test
    void getScootersByRentalPoint() {
        Scooter scooter1 = generateFullTestData();

        List<Scooter> rental = List.of(scooter1);

        doReturn(List.of(scooter1)).when(repo).findByRentalPointId(scooter1.getRentalPoint().getId());
        doReturn(true)
            .when(rentalPointRepository)
            .existsById(scooter1.getRentalPoint().getId());

        List<ScooterDto> found = service.getScootersByRentalPoint(scooter1.getRentalPoint().getId());
        assertEquals(found.size(), rental.size());
        assertTrue(found.stream().map(ScooterDto::getId).toList()
            .containsAll(rental.stream().map(Scooter::getId).toList()));
    }

    @Test
    void updateScooterStatus() {
        Scooter scooter1 = generateFullTestData();
        ScooterStatus scooterStatus = scooter1.getStatus();
        doReturn(Optional.of(scooter1)).when(repo).findById(scooter1.getId());
        service.updateScooterStatus(scooter1.getId(), ScooterStatus.IN_USE);
        assertNotEquals(scooterStatus, scooter1.getStatus());
    }

    private static long randomId() {
        return new Random().nextLong();
    }

    private ScooterDto convertToDto(Scooter scooter) {
        ScooterDto dto = new ScooterDto();
        dto.setId(scooter.getId());
        return dto;
    }

    private Scooter generateFullTestData() {
        RentalPoint rentalPoint = new RentalPoint();
        rentalPoint.setId(randomId());
        Scooter scooter = new Scooter();
        scooter.setId(randomId());
        scooter.setRentalPoint(rentalPoint);
        scooter.setStatus(ScooterStatus.AVAILABLE);
        return scooter;
    }
}