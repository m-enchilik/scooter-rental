package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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
import ru.senla.javacourse.enchilik.scooterrental.api.dto.SubscriptionDto;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.TariffType;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.ScooterNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.SubscriptionNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.SubscriptionRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.TariffRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    SubscriptionServiceImpl.class,
})
class SubscriptionServiceImplTest {
    @Autowired
    private SubscriptionServiceImpl service;

    @MockitoBean
    private SubscriptionRepository repo;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TariffRepository tariffRepository;

    @Test
    public void contextTest() {
        assertNotNull(service);
        assertNotNull(repo);
        assertNotNull(userRepository);
        assertNotNull(tariffRepository);
    }

    @Test
    void saveByDto() {
        Subscription subscription = generateFullTestData();
        repo.save(subscription);
        verify(repo, times(1)).save(subscription);
        verify(repo, times(1)).save(any());
    }



    @Test
    void saveByEntity() {
        Subscription subscription = generateFullTestData();
        repo.save(subscription);
        verify(repo, times(1)).save(subscription);
        verify(repo, times(1)).save(any());
    }

    @Test
    void findByIdSuccess() {
        Subscription subscription = generateFullTestData();
        doReturn(Optional.of(subscription)).when(repo).findById(123L);

        SubscriptionDto found = service.findById(123L);

        assertEquals(subscription.getId(), found.getId());
    }

    @Test
    void findByIdFail() {
        doReturn(Optional.empty()).when(repo).findById(any());

        SubscriptionNotFoundException thrown = assertThrows(SubscriptionNotFoundException.class, () -> {
            service.findById(123L);
        });

        String message = thrown.getMessage();
        assertNotNull(message);
        assertTrue(message.contains("123"));
        assertTrue(message.toLowerCase().contains("не найдена"));
    }

    @Test
    void update() {
        Subscription subscription = generateFullTestData();
        doReturn(Optional.of(subscription)).when(repo).findById(subscription.getId());
        doReturn(subscription).when(repo).update(subscription);
        service.update(subscription.getId(), convertToDto(subscription));
        verify(repo, times(1)).update(subscription);
        verify(repo, times(1)).update(any());
    }

    @Test
    void delete() {
        Subscription subscription = generateFullTestData();
        doReturn(Optional.of(subscription)).when(repo).findById(subscription.getId());
        service.delete(subscription.getId());
        verify(repo, times(1)).delete(subscription.getId());
        verify(repo, times(1)).delete(any());
    }

    @Test
    void findAll() {
        Subscription subscription1 = generateFullTestData();
        Subscription subscription2 = generateFullTestData();
        Subscription subscription3 = generateFullTestData();

        List<Subscription> subscriptions = List.of(subscription1, subscription2, subscription3);

        doReturn(subscriptions).when(repo).findAll();

        List<SubscriptionDto> found = service.findAll();
        assertTrue(found.size() == subscriptions.size());
        assertTrue(found.stream().map(SubscriptionDto::getId).toList()
            .containsAll(subscriptions.stream().map(Subscription::getId).toList()));
    }

    @Test
    void findByUserId() {
        Subscription subscription = generateFullTestData();

        List<Subscription> subscriptions = List.of(subscription);

        doReturn(List.of(subscription)).when(repo).findByUserId(subscription.getUser().getId());

        List<SubscriptionDto> found = service.findByUserId(subscription.getUser().getId());
        assertTrue(found.stream().map(SubscriptionDto::getId).toList()
            .containsAll(subscriptions.stream().map(Subscription::getId).toList()));
    }

    @Test
    void findActiveByUserId() {
        Subscription subscription = generateFullTestData();
        subscription.setActive(true);
        User user = subscription.getUser();

        List<Subscription> subscriptions = List.of(subscription);

        doReturn(List.of(subscription)).when(repo).findActiveByUserId(user.getId());

        List<SubscriptionDto> found = service.findActiveByUserId(subscription.getUser().getId());
        assertTrue(!found.isEmpty());
    }

    @Test
    void addBasicTariff() {
        Subscription unused = generateFullTestData();
        User user = unused.getUser();
        Tariff tariff = new Tariff();
        tariff.setType(TariffType.BASIC);

        doReturn(tariff).when(tariffRepository).findBasic();
        doAnswer(ctx -> {
            Subscription subscriptionToSave = ctx.getArgument(0);
            assertEquals(user, subscriptionToSave.getUser());
            return subscriptionToSave;
        }).when(repo).save(any());
        service.addBasicTariff(user);
        verify(repo, times(1)).save(any());
    }

    private Subscription generateFullTestData() {
        User user = new User();
        user.setId(randomId());
        Tariff tariff = new Tariff();
        tariff.setId(randomId());
        Subscription subscription = new Subscription();
        subscription.setId(randomId());
        subscription.setUser(user);
        subscription.setTariff(tariff);
        return subscription;
    }

    private SubscriptionDto convertToDto(Subscription subscription) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setUserId(subscription.getUser().getId());
        dto.setTariffId(subscription.getTariff().getId());
        return dto;
    }

    private static long randomId() {
        return new Random().nextLong();
    }
}