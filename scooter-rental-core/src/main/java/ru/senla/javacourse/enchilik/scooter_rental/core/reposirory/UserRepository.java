package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.User;

@Repository
public class UserRepository extends AbstractDAO<User, Long> {

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
