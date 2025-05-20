package ru.senla.javacourse.enchilik.scooterrental.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TariffNotFoundException extends RuntimeException {
    public TariffNotFoundException(String message) {
        super(message);
    }
    public TariffNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
