package ru.senla.javacourse.enchilik.scooterrental.core.exception;

public class UserHasNotEnoughMoneyException extends RuntimeException {
    public UserHasNotEnoughMoneyException(String message) {
        super(message);
    }
}
