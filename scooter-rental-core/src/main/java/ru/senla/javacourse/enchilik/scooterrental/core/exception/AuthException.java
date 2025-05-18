package ru.senla.javacourse.enchilik.scooterrental.core.exception;

/**
 * Исключение используется для ошибок аутентификации и авторизациит.
 *
 * @author upagge 21.06.2022
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

}
