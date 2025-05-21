package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.UserDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Role;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.payment.PaymentService;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.SubscriptionService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    UserServiceImpl.class,
})
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl service;

    @MockitoBean
    private UserRepository repo;

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    public void contextTest() {
        assertNotNull(service);
        assertNotNull(repo);
        assertNotNull(subscriptionService);
        assertNotNull(passwordEncoder);
        assertNotNull(paymentService);
    }

    @Test
    void createByParams() {
        String userName = "name";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email";
        String phoneNumber = "phoneNumber";

        String encodedPassword = "encoded password";

        doReturn(encodedPassword).when(passwordEncoder).encode(password);

        doAnswer(ctx -> {
            User userToSave = ctx.getArgument(0);
            assertEquals(userName, userToSave.getUsername());
            assertEquals(encodedPassword, userToSave.getPassword());
            assertEquals(firstName, userToSave.getFirstName());
            assertEquals(lastName, userToSave.getLastName());
            assertEquals(email, userToSave.getEmail());
            assertEquals(phoneNumber, userToSave.getPhoneNumber());

            return userToSave;
        }).when(repo).save(any());

        service.createUser(userName, password, firstName, lastName, email, phoneNumber);
        verify(repo, times(1)).save(any());
    }

    @Test
    void createByDto() {
        User user = generateFullTestData();
        repo.save(user);
        verify(repo, times(1)).save(user);
        verify(repo, times(1)).save(any());
    }


    @Test
    void findByIdSuccess() {
        User user = generateFullTestData();
        doReturn(Optional.of(user)).when(repo).findById(123L);

        UserDto found = service.getUserById(123L);

        assertEquals(user.getId(), found.getId());
    }

    @Test
    void findByIdFail() {
        doReturn(Optional.empty()).when(repo).findById(any());

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            service.getUserById(123L);
        });

        String message = thrown.getMessage();
        assertNotNull(message);
        assertTrue(message.contains("123"));
        assertTrue(message.toLowerCase().contains("не найден"));
    }

    @Test
    void updateUser() {
        User user = generateFullTestData();
        doReturn(Optional.of(user)).when(repo).findById(user.getId());
        doReturn(user).when(repo).save(user);
        service.updateUser(user.getId(), convertToDto(user));
        verify(repo, times(1)).save(user);
        verify(repo, times(1)).save(any());
    }

    @Test
    void deposit() {
        User user = generateFullTestData();

        doReturn(user).when(repo).update(user);
        service.deposit(user, BigDecimal.valueOf(100500L));
        verify(repo, times(1)).update(user);
        verify(repo, times(1)).update(any());
    }

    @Test
    void deleteUser() {
        User user = generateFullTestData();
        doReturn(Optional.of(user)).when(repo).findById(user.getId());
        doReturn(user).when(repo).save(user);
        service.deleteUser(user.getId());
        verify(repo, times(1)).delete(user.getId());
        verify(repo, times(1)).delete(any());
    }

    @Test
    void getAllUsers() {
        User user1 = generateFullTestData();
        User user2 = generateFullTestData();
        User user3 = generateFullTestData();

        List<User> users = List.of(user1, user2, user3);

        doReturn(users).when(repo).findAll();

        List<UserDto> found = service.getAllUsers();
        assertTrue(found.size() == users.size());
        assertTrue(found.stream().map(UserDto::getId).toList()
            .containsAll(users.stream().map(User::getId).toList()));
    }

    @Test
    void findByUsername() {
        User user = generateFullTestData();

        doReturn(user).when(repo).findByUsername(user.getUsername());

        User found = service.findByUsername(user.getUsername());
        assertTrue(found.getUsername().equals(user.getUsername()));

    }

    private static long randomId() {
        return new Random().nextLong();
    }

    private User generateFullTestData() {
        User user = new User();
        user.setId(randomId());
        user.setRoles(Set.of(Role.USER));
        user.setDeposit(BigDecimal.TEN);
        user.setUsername("username");
        return user;
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setRoles(user.getRoles().stream()
            .map(r -> r.name())
            .collect(Collectors.toSet()));
        return dto;
    }
}