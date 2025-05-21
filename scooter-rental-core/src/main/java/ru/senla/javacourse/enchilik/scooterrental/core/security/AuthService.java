package ru.senla.javacourse.enchilik.scooterrental.core.security;

import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.AuthException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.security.jwt.JwtAuthentication;
import ru.senla.javacourse.enchilik.scooterrental.core.security.jwt.JwtProvider;
import ru.senla.javacourse.enchilik.scooterrental.core.security.jwt.JwtRequest;
import ru.senla.javacourse.enchilik.scooterrental.core.security.jwt.JwtResponse;
import ru.senla.javacourse.enchilik.scooterrental.core.service.UserService;

@Service
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }


    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userService.findByUsername(authRequest.getLogin());
        if (user == null) {
            throw new AuthException("Пользователь не найден");
        }
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.findByUsername(login);
                if (user == null) {
                    throw new AuthException("Пользователь не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.findByUsername(login);
                if (user == null) {
                    throw new AuthException("Пользователь не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
