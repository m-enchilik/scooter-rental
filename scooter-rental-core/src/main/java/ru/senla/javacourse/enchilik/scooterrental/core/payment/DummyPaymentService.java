package ru.senla.javacourse.enchilik.scooterrental.core.payment;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class DummyPaymentService implements PaymentService{
    @Override
    public boolean deposit(BigDecimal amount) {
        return true;
    }
}
