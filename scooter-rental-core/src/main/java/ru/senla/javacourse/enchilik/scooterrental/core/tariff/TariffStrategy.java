package ru.senla.javacourse.enchilik.scooterrental.core.tariff;

import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

import java.time.LocalDateTime;

public interface TariffStrategy {
    /**
     * сколько минут можно проехать на данном тарифе
     */
    int predictTimeLimitMinutes(User user, Subscription subscription);
    LocalDateTime getTimeLimit(User user, Subscription subscription);
    Rental finish(Rental rental, long minutesUsed);
}
