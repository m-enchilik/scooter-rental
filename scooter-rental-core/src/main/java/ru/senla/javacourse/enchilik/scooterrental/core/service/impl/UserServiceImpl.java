package ru.senla.javacourse.enchilik.scooterrental.core.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.UserDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserAlreadyExistsException;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.UserNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Role;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.payment.PaymentService;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;
import ru.senla.javacourse.enchilik.scooterrental.core.service.SubscriptionService;
import ru.senla.javacourse.enchilik.scooterrental.core.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;


    @Autowired
    public UserServiceImpl(
        UserRepository userRepository, SubscriptionService subscriptionService,
        @Lazy PasswordEncoder passwordEncoder, PaymentService paymentService) {
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
        this.passwordEncoder = passwordEncoder;
        this.paymentService = paymentService;
    }

    @Override
    public UserDto createUser(
        String userName,
        String password,
        String firstName,
        String lastName,
        String email,
        String phoneNumber) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);


        User user = new User();
        user.setUsername(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setUserBlocked(false);
        user.setRentBlocked(false);
        user.setDeposit(BigDecimal.ZERO);
        user.setRoles(roles);
        user = userRepository.save(user);

        subscriptionService.addBasicTariff(user);

        UserDto userDto = convertToUserDto(user);
        return userDto;
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) throws UserAlreadyExistsException {
        logger.info("Попытка создать нового пользователя с данными: {}", userDto);

        try {
            if (userRepository.existsByUsername(userDto.getUsername())) {
                logger.error(
                    "Пользователь с именем пользователя {} уже существует.",
                    userDto.getUsername());
                throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
            }

            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPhoneNumber(userDto.getPhoneNumber());
            if (userDto.getDeposit() == null) {
                user.setDeposit(BigDecimal.ZERO);
            } else {
                user.setDeposit(new BigDecimal(userDto.getDeposit()));
            }

            if (userDto.getUserBlocked() == null) {
                user.setUserBlocked(false);
            } else {
                user.setUserBlocked(userDto.getUserBlocked());
            }

            if (userDto.getRentBlocked() == null) {
                user.setRentBlocked(false);
            } else {
                user.setRentBlocked(userDto.getRentBlocked());
            }

            Set<Role> roles = userDto.getRoles().stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());

            user.setRoles(roles);

            user = userRepository.save(user);

            userDto.setId(user.getId());

            subscriptionService.addBasicTariff(user);

            logger.info("Пользователь успешно создан с ID: {}", user.getId());
            return userDto;
        } catch (Exception e) {
            logger.error("Ошибка при создании пользователя: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) throws UserNotFoundException {
        logger.info("Попытка получить пользователя с ID: {}", id);

        try {
            User user =
                userRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Пользователь с ID {} не найден.", id);
                            return new UserNotFoundException(
                                "Пользователь с ID " + id + " не найден");
                        });

            UserDto userDto = convertToUserDto(user);
            logger.info("Пользователь с ID {} успешно получен.", id);
            return userDto;
        } catch (Exception e) {
            logger.error("Ошибка при получении пользователя с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) throws UserNotFoundException {
        logger.info("Попытка обновить пользователя с ID: {}, данные: {}", id, userDto);

        try {
            User user =
                userRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Пользователь с ID {} не найден.", id);
                            return new UserNotFoundException(
                                "Пользователь с ID " + id + " не найден");
                        });

            if (userDto.getUsername() != null) {
                if (!userDto.getUsername().equals(user.getUsername())
                    && userRepository.existsByUsername(userDto.getUsername())) {
                    logger.error("Имя пользователя {} уже занято.", userDto.getUsername());
                    throw new UserAlreadyExistsException(
                        "Имя пользователя " + userDto.getUsername() + " уже занято");
                }
                user.setUsername(userDto.getUsername());
            }
            if (userDto.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            if (userDto.getFirstName() != null) {
                user.setFirstName(userDto.getFirstName());
            }
            if (userDto.getLastName() != null) {
                user.setLastName(userDto.getLastName());
            }
            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }
            if (userDto.getPhoneNumber() != null) {
                user.setPhoneNumber(userDto.getPhoneNumber());
            }

            if (userDto.getRoles() != null) {
                Set<Role> roles = userDto.getRoles().stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());

                user.setRoles(roles);
            }

            userRepository.save(user);
            logger.info("Пользователь с ID {} успешно обновлен.", id);
            return convertToUserDto(user);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении пользователя с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public UserDto deposit(User user, BigDecimal amount) throws UserNotFoundException {
        if (paymentService.deposit(amount)) {
            BigDecimal before = user.getDeposit();
            if (before == null) {
                before = BigDecimal.ZERO;
            }
            user.setDeposit(before.add(amount));
        }
        user = userRepository.update(user);
        UserDto userDto = convertToUserDto(user);
        return userDto;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws UserNotFoundException {
        logger.info("Попытка удалить пользователя с ID: {}", id);

        try {
            User user =
                userRepository
                    .findById(id)
                    .orElseThrow(
                        () -> {
                            logger.warn("Пользователь с ID {} не найден.", id);
                            return new UserNotFoundException(
                                "Пользователь с ID " + id + " не найден");
                        });
            userRepository.delete(user.getId());
            logger.info("Пользователь с ID {} успешно удален.", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении пользователя с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        logger.info("Попытка получить всех пользователей.");

        try {
            List<User> users = userRepository.findAll();
            List<UserDto> userDtos =
                users.stream().map(this::convertToUserDto).collect(Collectors.toList());
            logger.info("Получено {} пользователей.", users.size());
            return userDtos;

        } catch (Exception e) {
            logger.error("Ошибка при получении всех пользователей: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        logger.info("Попытка найти пользователя по имени пользователя: {}", username);
        try {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                logger.info("Пользователь с именем пользователя {} успешно найден.", username);
            } else {
                logger.warn("Пользователь с именем пользователя {} не найден.", username);
            }
            return user;
        } catch (Exception e) {
            logger.error(
                "Ошибка при поиске пользователя по имени пользователя {}: {}",
                username,
                e.getMessage(),
                e);
            throw e;
        }
    }

    private UserDto convertToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword("<HIDDEN>");
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoles(user.getRoles()
            .stream()
            .map(Role::name)
            .collect(Collectors.toSet()));
        dto.setDeposit(user.getDeposit().toString());
        dto.setUserBlocked(user.getUserBlocked());
        dto.setRentBlocked(user.getRentBlocked());
        return dto;
    }
}
