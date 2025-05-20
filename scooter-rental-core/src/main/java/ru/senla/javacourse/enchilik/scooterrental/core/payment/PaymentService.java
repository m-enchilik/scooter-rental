package ru.senla.javacourse.enchilik.scooterrental.core.payment;

import java.math.BigDecimal;

public interface PaymentService {

    boolean deposit(BigDecimal amount);
}
