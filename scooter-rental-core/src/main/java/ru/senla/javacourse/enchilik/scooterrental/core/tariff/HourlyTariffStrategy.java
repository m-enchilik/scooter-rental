package ru.senla.javacourse.enchilik.scooterrental.core.tariff;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.SubscriptionRepository;

@Component
public class HourlyTariffStrategy implements TariffStrategy {
    private final SubscriptionRepository subscriptionRepository;

    public HourlyTariffStrategy(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public long predictTimeLimitMinutes(User user, Subscription subscription) {
        return subscription.getRestUnits() * 60;
    }

    @Override
    public Rental finish(Rental rental, long minutesUsed) {
        Subscription subscription = rental.getSubscription();
        subscription.setRestUnits(0L);
        subscription.setActive(false);
        BigDecimal price = subscription.getTariff().getPrice();
        rental.setTotalCost(price);

        subscriptionRepository.save(subscription);
        return rental;
    }
}
