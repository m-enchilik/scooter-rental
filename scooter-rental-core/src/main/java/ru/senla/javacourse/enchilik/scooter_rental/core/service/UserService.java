package ru.senla.javacourse.enchilik.scooter_rental.core.service;

import java.util.List;
import ru.senla.javacourse.enchilik.scooter_rental.api.dto.UserDto;
import ru.senla.javacourse.enchilik.scooter_rental.api.dto.UserProfileDto;
import ru.senla.javacourse.enchilik.scooter_rental.core.exception.UserAlreadyExistsException;
import ru.senla.javacourse.enchilik.scooter_rental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.User;

public interface UserService {
    UserDto createUser(UserDto userDto) throws UserAlreadyExistsException;

    UserProfileDto getUserById(Long id) throws UserNotFoundException;

    UserProfileDto updateUser(Long id, UserDto userDto) throws UserNotFoundException;

    void deleteUser(Long id) throws UserNotFoundException;

    List<UserProfileDto> getAllUsers();

    User findByUsername(String username);
}
