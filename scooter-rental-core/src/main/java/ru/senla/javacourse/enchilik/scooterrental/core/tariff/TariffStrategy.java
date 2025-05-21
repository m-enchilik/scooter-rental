package ru.senla.javacourse.enchilik.scooterrental.core.tariff;

import java.time.LocalDateTime;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

public interface TariffStrategy {
    /**
     * сколько минут можно проехать на данном тарифе.
     */
    long predictTimeLimitMinutes(User user, Subscription subscription);

    default LocalDateTime getTimeLimit(User user, Subscription subscription) {
        Long timeLimitMinutes = predictTimeLimitMinutes(user, subscription);

        return LocalDateTime.now().plusMinutes(timeLimitMinutes);
    }

    Rental finish(Rental rental, long minutesUsed);
}
