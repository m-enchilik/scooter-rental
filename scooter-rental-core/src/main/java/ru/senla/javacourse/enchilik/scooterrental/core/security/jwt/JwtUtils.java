package ru.senla.javacourse.enchilik.scooterrental.core.security.jwt;

import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.AuthException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Role;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.reposirory.UserRepository;

@Component
public final class JwtUtils {

    private final UserRepository userRepository;

    @Autowired
    public JwtUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        Long userId = claims.get("userId", Long.class);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthException("Can't find user by id='%s'".formatted(userId)));
        jwtInfoToken.setPrincipal(user);
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        return roles.stream()
            .map(Role::valueOf)
            .collect(Collectors.toSet());
    }

}
