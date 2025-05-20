package ru.senla.javacourse.enchilik.scooterrental.core.service;


import java.util.List;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.SubscriptionDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.SubscriptionNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

public interface SubscriptionService {
    SubscriptionDto save(SubscriptionDto subscriptionDto);

    SubscriptionDto save(Subscription subscription);

    SubscriptionDto findById(Long id) throws SubscriptionNotFoundException;

    SubscriptionDto update(Long id, SubscriptionDto subscriptionDto) throws SubscriptionNotFoundException;

    void delete(Long id) throws SubscriptionNotFoundException;

    List<SubscriptionDto> findAll();

    List<SubscriptionDto> findByUserId(Long id);

    List<SubscriptionDto> findActiveByUserId(Long id);

    void addBasicTariff(User user);
}
