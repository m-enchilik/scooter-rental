package ru.senla.javacourse.enchilik.scooterrental.core.tariff;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.TariffType;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

public class PrepaidMinutesTariffStrategy implements TariffStrategy {
    @Override
    public long predictTimeLimitMinutes(User user, Subscription subscription) {
        if (subscription.getTariff().getType() != TariffType.PREPAID_MINUTES) {
            throw new IllegalArgumentException("Выбрана неверная стратегия.");
        }
        return subscription.getRestUnits();
    }

    @Override
    public Rental finish(Rental rental, long minutesUsed) {
        Subscription subscription = rental.getSubscription();
        subscription.setRestUnits(subscription.getRestUnits() - minutesUsed);
        Tariff tariff = rental.getSubscription().getTariff();
        BigDecimal minuteCost = tariff.getPrice().divide(BigDecimal.valueOf(tariff.getUnitsIncluded()));
        BigDecimal totalCost = minuteCost.multiply(BigDecimal.valueOf(minutesUsed));
        rental.setTotalCost(totalCost);

        return rental;
    }
}
