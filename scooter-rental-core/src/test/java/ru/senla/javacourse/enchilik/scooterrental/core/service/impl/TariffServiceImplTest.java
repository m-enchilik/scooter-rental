package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.SubscriptionDto;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.TariffDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.TariffRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.SubscriptionService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    TariffServiceImpl.class,
})
class TariffServiceImplTest {

    @Autowired
    private TariffServiceImpl service;

    @MockitoBean
    private TariffRepository repo;

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void contextTest() {
        assertNotNull(service);
        assertNotNull(repo);
        assertNotNull(subscriptionService);
        assertNotNull(userRepository);
    }

    @Test
    void createTariff() {
        Tariff tariff = generateFullTestData();
        repo.save(tariff);
        verify(repo, times(1)).save(tariff);
        verify(repo, times(1)).save(any());
    }


    @Test
    void findByIdSuccess() {
        Tariff tariff = generateFullTestData();
        doReturn(Optional.of(tariff)).when(repo).findById(123L);

        TariffDto found = service.getTariffById(123L);

        assertEquals(tariff.getId(), found.getId());
    }

    @Test
    void findByIdFail() {
        doReturn(Optional.empty()).when(repo).findById(any());

        TariffNotFoundException thrown = assertThrows(TariffNotFoundException.class, () -> {
            service.getTariffById(123L);
        });

        String message = thrown.getMessage();
        assertNotNull(message);
        assertTrue(message.contains("123"));
        assertTrue(message.toLowerCase().contains("не найден"));
    }

    @Test
    void updateTariff() {
        Tariff tariff = generateFullTestData();
        doReturn(Optional.of(tariff)).when(repo).findById(tariff.getId());
        doReturn(tariff).when(repo).save(tariff);
        service.updateTariff(tariff.getId(), convertToDto(tariff));
        verify(repo, times(1)).save(tariff);
        verify(repo, times(1)).save(any());
    }

    @Test
    void deleteTariff() {
        Tariff tariff = generateFullTestData();
        doReturn(Optional.of(tariff)).when(repo).findById(tariff.getId());
        service.deleteTariff(tariff.getId());
        verify(repo, times(1)).delete(tariff.getId());
        verify(repo, times(1)).delete(any());
    }

    @Test
    void getAllTariffs() {
        Tariff tariff1 = generateFullTestData();
        Tariff tariff2 = generateFullTestData();
        Tariff tariff3 = generateFullTestData();

        List<Tariff> tariffs = List.of(tariff1, tariff2, tariff3);

        doReturn(tariffs).when(repo).findAll();

        List<TariffDto> found = service.getAllTariffs();
        assertTrue(found.size() == tariffs.size());
        assertTrue(found.stream().map(TariffDto::getId).toList()
            .containsAll(tariffs.stream().map(Tariff::getId).toList()));
    }

    @Test
    void buyTariff() {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(randomId());
        Tariff tariff = generateFullTestData();
        dto.setTariffId(tariff.getId());
        tariff.setPrice(BigDecimal.ONE);
        User user = new User();
        user.setId(randomId());
        user.setDeposit(BigDecimal.TEN);
        doReturn(dto).when(subscriptionService).save(any(Subscription.class));
        doReturn(Optional.of(tariff)).when(repo).findById(tariff.getId());
        service.buyTariff(user, tariff.getId());
        verify(subscriptionService, times(1)).save(any(Subscription.class));
    }

    private static long randomId() {
        return new Random().nextLong();
    }

    private Tariff generateFullTestData() {
        Tariff tariff = new Tariff();
        tariff.setId(randomId());
        return tariff;
    }

    private TariffDto convertToDto(Tariff tariff) {
        TariffDto dto = new TariffDto();
        dto.setId(tariff.getId());
        return dto;
    }
}