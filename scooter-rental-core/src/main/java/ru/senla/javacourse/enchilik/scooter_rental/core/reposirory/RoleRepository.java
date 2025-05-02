package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.Role;

@Repository
public class RoleRepository extends AbstractDAO<Role, Long> {

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    Optional<Role> findByName(String name);
}
