package ru.senla.javacourse.enchilik.scooter_rental.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScooterNotFoundException extends RuntimeException {
    public ScooterNotFoundException(String message) {
        super(message);
    }
}
