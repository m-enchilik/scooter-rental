package ru.senla.javacourse.enchilik.scooter_rental.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class UserDto {
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 50, message = "Имя не может превышать 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 50, message = "Фамилия не может превышать 50 символов")
    private String lastName;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат email")
    @Size(max = 100, message = "Email не может превышать 100 символов")
    private String email;

    @Pattern(regexp = "\\d{10,20}", message = "Неверный формат номера телефона")
    private String phoneNumber;

    private Set<String> roles;


    private String deposit;

    private Boolean userBlocked;

    private Boolean rentBlocked;
}
