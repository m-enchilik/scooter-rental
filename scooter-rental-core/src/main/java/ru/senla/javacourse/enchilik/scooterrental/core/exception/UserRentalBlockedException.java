package ru.senla.javacourse.enchilik.scooterrental.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserRentalBlockedException extends RuntimeException {
    public UserRentalBlockedException(String message) {
        super(message);
    }
}
