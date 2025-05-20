package ru.senla.javacourse.enchilik.scooterrental.core.service;

import java.math.BigDecimal;
import java.util.List;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.UserDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserAlreadyExistsException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

public interface UserService {
    UserDto createUser(String userName, String password, String firstName, String lastName, String email, String phoneNumber);

    UserDto save(UserDto userDto) throws UserAlreadyExistsException;

    UserDto getUserById(Long id) throws UserNotFoundException;
    UserDto updateUser(Long id, UserDto userDto) throws UserNotFoundException;

    UserDto deposit(User user, BigDecimal amount) throws UserNotFoundException;

    void deleteUser(Long id) throws UserNotFoundException;

    List<UserDto> getAllUsers();

    User findByUsername(String username);
}
