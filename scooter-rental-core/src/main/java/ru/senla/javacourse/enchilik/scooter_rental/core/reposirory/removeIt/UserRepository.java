package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.removeIt;

import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.processing.Find;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.User;
import ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.DAO;

@Repository
public interface UserRepository extends DAO<User, Long> {
    User save(User user);

    @Find
    Optional<User> findById(Long id);

    User update(User user);

    void delete(Long id);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
