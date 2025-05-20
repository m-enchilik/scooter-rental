package ru.senla.javacourse.enchilik.scooterrental.core.tariff;

import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.TariffType;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.SubscriptionRepository;

@Component
public class PrepaidMinutesTariffStrategy implements TariffStrategy {

    private final SubscriptionRepository subscriptionRepository;

    public PrepaidMinutesTariffStrategy(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

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
        long restUnits = subscription.getRestUnits() - minutesUsed;
        subscription.setRestUnits(restUnits);
        if (restUnits <= 0) {
            subscription.setActive(false);
        }
        Tariff tariff = rental.getSubscription().getTariff();
        BigDecimal minuteCost = tariff.getPrice().divide(BigDecimal.valueOf(tariff.getUnitsIncluded()));
        BigDecimal totalCost = minuteCost.multiply(BigDecimal.valueOf(minutesUsed));
        rental.setTotalCost(totalCost);

        subscriptionRepository.save(subscription);

        return rental;
    }
}
