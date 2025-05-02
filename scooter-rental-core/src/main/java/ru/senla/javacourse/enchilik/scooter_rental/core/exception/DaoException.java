package ru.senla.javacourse.enchilik.scooter_rental.core.exception;

public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
