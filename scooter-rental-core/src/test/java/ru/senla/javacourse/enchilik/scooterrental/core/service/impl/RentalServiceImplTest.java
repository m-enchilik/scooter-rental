package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.RentalDto;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.ScooterStatus;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.RentalNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserRentalBlockedException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.RentalPoint;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalPointRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.ScooterRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.SubscriptionRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.ScooterService;
import ru.senla.javacourse.enchilik.scooterrental.core.tariff.TariffStrategy;
import ru.senla.javacourse.enchilik.scooterrental.core.tariff.TariffStrategyResolver;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    RentalServiceImpl.class,
})
class RentalServiceImplTest {

    @Autowired
    private RentalServiceImpl service;

    @MockitoBean
    private ScooterService scooterService;
    @MockitoBean
    private RentalRepository repo;

    @MockitoBean
    private SubscriptionRepository subscriptionRepository;

    @MockitoBean
    private ScooterRepository scooterRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RentalPointRepository rentalPointRepository;
    //    private RentalPointServiceImpl rentalPointService;

    @MockitoBean
    private TariffStrategyResolver tariffStrategyResolver;


    @Test
    public void contextTest() {
        assertNotNull(service);
        assertNotNull(repo);
        assertNotNull(scooterRepository);
    }

    @Test
    void save() {
        Rental rental = generateFullTestData();
        repo.save(rental);
        verify(repo, times(1)).save(rental);
        verify(repo, times(1)).save(any());
    }

    @Test
    void findByIdSuccess() {
        Rental rental = generateFullTestData();
        doReturn(Optional.of(rental)).when(repo).findById(123L);

        RentalDto found = service.getRentalById(123L);

        assertEquals(rental.getId(), found.getId());
    }

    @Test
    void findByIdFail() {
        doReturn(Optional.empty()).when(repo).findById(any());

        RentalNotFoundException thrown = assertThrows(RentalNotFoundException.class, () -> {
            service.getRentalById(123L);
        });

        String message = thrown.getMessage();
        assertNotNull(message);
        assertTrue(message.contains("123"));
        assertTrue(message.toLowerCase().contains("не найдена"));
    }


    @Test
    void update() {
        Rental rental = generateFullTestData();
        doReturn(Optional.of(rental)).when(repo).findById(rental.getId());
        doReturn(rental).when(repo).update(rental);
        service.update(rental.getId(), convertToDto(rental));
        verify(repo, times(1)).update(rental);
        verify(repo, times(1)).update(any());
    }

    @Test
    void delete() {
        Rental rental = generateFullTestData();
        doReturn(Optional.of(rental)).when(repo).findById(rental.getId());
        service.delete(rental.getId());
        verify(repo, times(1)).delete(rental.getId());
        verify(repo, times(1)).delete(any());
    }

    @Test
    void startRentalUserBlocked() {
        Rental rental = generateFullTestData();

        User user = rental.getUser();
        user.setRentBlocked(true);

        UserRentalBlockedException userRentalBlockedException = assertThrows(UserRentalBlockedException.class, () -> {
            service.startRental(user, 1L, 1L);
        });
    }

    @Test
    void startRental() {
        Rental rental = generateFullTestData();
        long subscriptionId = randomId();
        long scooterId = randomId();

        User user = rental.getUser();
        user.setRentBlocked(false);

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setUser(user);

        Scooter scooter = new Scooter();
        scooter.setId(scooterId);
        scooter.setStatus(ScooterStatus.AVAILABLE);

        Tariff tariff = new Tariff();
        subscription.setTariff(tariff);

        TariffStrategy strategy = mock(TariffStrategy.class);
        doReturn(strategy).when(tariffStrategyResolver).resolve(any());

        doReturn(Optional.of(scooter)).when(scooterRepository).findById(scooterId);
        doAnswer(ctx -> ctx.getArgument(0)).when(repo).save(any());

        doReturn(Optional.of(subscription)).when(subscriptionRepository).findById(subscriptionId);

        service.startRental(user, subscriptionId, scooterId);
        verify(strategy, times(1)).getTimeLimit(user, subscription);
    }

    @Test
    void endRental() {
        long rentalId = randomId();
        Rental rental = generateFullTestData();
        rental.setStartTime(LocalDateTime.now().minusMinutes(3));
        rental.setEndTime(null);
        long pointId = rental.getId();

        RentalPoint rentalPoint = new RentalPoint();
        rentalPoint.setId(pointId);

        doReturn(Optional.of(rental)).when(repo).findById(rentalId);
        doReturn(Optional.of(rentalPoint)).when(rentalPointRepository).findById(pointId);
        doAnswer(ctx -> ctx.getArgument(0)).when(repo).save(any());

        Subscription subscription = new Subscription();
        rental.setSubscription(subscription);

        Tariff tariff = new Tariff();
        subscription.setTariff(tariff);

        TariffStrategy strategy = mock(TariffStrategy.class);
        doReturn(strategy).when(tariffStrategyResolver).resolve(any());

        service.endRental(rentalId, pointId);
        verify(strategy, times(1)).finish(rental, 3 + 1);
    }

    @Test
    void getAllRentals() {
        Rental rental1 = generateFullTestData();
        Rental rental2 = generateFullTestData();
        Rental rental3 = generateFullTestData();
        List<Rental> rental = List.of(rental1, rental2, rental3);

        doReturn(rental).when(repo).findAll();

        List<RentalDto> found = service.getAllRentals();
        assertTrue(found.size() == rental.size());
        assertTrue(found.stream().map(RentalDto::getId).toList()
            .containsAll(rental.stream().map(Rental::getId).toList()));
    }

    @Test
    void getRentalsByUser() {
        Rental rental1 = generateFullTestData();

        List<Rental> rental = List.of(rental1);

        doReturn(List.of(rental1)).when(repo).findByUserId(rental1.getUser().getId());

        List<RentalDto> found = service.getRentalsByUser(rental1.getUser().getId());
        assertTrue(found.size() == rental.size());
        assertTrue(found.stream().map(RentalDto::getId).toList()
            .containsAll(rental.stream().map(Rental::getId).toList()));
    }

    @Test
    void getRentalsByScooter() {
        Rental rental1 = generateFullTestData();

        List<Rental> rental = List.of(rental1);

        doReturn(List.of(rental1)).when(repo).findByUserId(rental1.getScooter().getId());

        List<RentalDto> found = service.getRentalsByUser(rental1.getScooter().getId());
        assertTrue(found.size() == rental.size());
        assertTrue(found.stream().map(RentalDto::getId).toList()
            .containsAll(rental.stream().map(Rental::getId).toList()));
    }

    private static long randomId() {
        return new Random().nextLong();
    }

    private Rental generateFullTestData() {
        User user = new User();
        user.setId(randomId());
        Scooter scooter = new Scooter();
        scooter.setId(randomId());
        Rental rental = new Rental();
        rental.setId(randomId());
        rental.setUser(user);
        rental.setScooter(scooter);
        return rental;
    }

    private RentalDto convertToDto(Rental rental) {
        RentalDto dto = new RentalDto();
        dto.setId(rental.getId());
        return dto;
    }
}