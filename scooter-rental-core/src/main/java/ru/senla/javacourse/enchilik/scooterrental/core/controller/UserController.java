package ru.senla.javacourse.enchilik.scooterrental.core.controller;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.UserDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserAlreadyExistsException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id)
        throws UserNotFoundException {
        UserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/self-register")
    public ResponseEntity<UserDto> selfRegister(
        @RequestParam String userName,
        @RequestParam String password,
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam String email,
        @RequestParam String phoneNumber
        )
        throws UserNotFoundException {
        UserDto createdUser = userService.createUser(userName, password, firstName, lastName, email, phoneNumber);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> registerByAdmin(@Valid @RequestBody UserDto userDto)
        throws UserAlreadyExistsException {
        UserDto createdUser = userService.create(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // TODO: следует выделить информацию, которую пользователь не может обновить в отдельную сущность
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
    public ResponseEntity<UserDto> updateUser(
        @PathVariable Long id, @Valid @RequestBody UserDto userDto)
        throws UserNotFoundException {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<UserDto> updateUserDeposit(
        @AuthenticationPrincipal User user,
        @RequestParam BigDecimal amount)
        throws UserNotFoundException {
        UserDto userDto = userService.deposit(user, amount);
        return  new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
