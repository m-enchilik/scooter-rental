package ru.senla.javacourse.enchilik.scooterrental.core.tariff;

import org.springframework.stereotype.Component;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.RentalRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class BasicTariffStrategy implements TariffStrategy {

    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public BasicTariffStrategy(UserRepository userRepository, RentalRepository rentalRepository) {
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public int predictTimeLimitMinutes(User user, Subscription subscription) {
        BigDecimal deposit = user.getDeposit();
        BigDecimal price = subscription.getTariff().getPrice();
        return deposit.divideToIntegralValue(price).intValue();
    }

    @Override
    public LocalDateTime getTimeLimit(User user, Subscription subscription) {
        int timeLimitMinutes = predictTimeLimitMinutes(user, subscription);

        return LocalDateTime.now().plusMinutes(timeLimitMinutes);
    }

    @Override
    public Rental finish(Rental rental, long minutesUsed) {
        Tariff tariff = rental.getSubscription().getTariff();
        BigDecimal totalCost = tariff.getPrice().multiply(BigDecimal.valueOf(minutesUsed));
        User user = rental.getUser();
        user.setDeposit(user.getDeposit().subtract(totalCost));
        userRepository.save(user);

        rental.setTotalCost(totalCost);
        return rental;
    }
}
