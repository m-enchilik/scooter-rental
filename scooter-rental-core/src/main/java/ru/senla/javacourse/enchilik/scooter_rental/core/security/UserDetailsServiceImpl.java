package ru.senla.javacourse.enchilik.scooter_rental.core.security;

import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.User;
import ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.removeIt.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "Пользователь с именем "
                                                        + username
                                                        + " не найден"));

        Set<GrantedAuthority> grantedAuthorities =
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
