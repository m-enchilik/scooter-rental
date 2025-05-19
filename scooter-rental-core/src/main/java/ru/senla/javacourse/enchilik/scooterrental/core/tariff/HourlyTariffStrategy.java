package ru.senla.javacourse.enchilik.scooterrental.core.tariff;

import java.math.BigDecimal;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

public class HourlyTariffStrategy implements TariffStrategy {
    @Override
    public long predictTimeLimitMinutes(User user, Subscription subscription) {
        return subscription.getRestUnits() * 60;
    }

    @Override
    public Rental finish(Rental rental, long minutesUsed) {
        Subscription subscription = rental.getSubscription();
        subscription.setRestUnits(0L);
        BigDecimal price = subscription.getTariff().getPrice();
        rental.setTotalCost(price);
        return rental;
    }
}
