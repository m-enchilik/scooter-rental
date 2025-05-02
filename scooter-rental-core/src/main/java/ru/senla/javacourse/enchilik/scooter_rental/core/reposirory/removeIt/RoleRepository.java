package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.removeIt;


import java.util.List;
import java.util.Optional;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.Role;
import ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.DAO;

public interface RoleRepository extends DAO<Role, Long> {

    Role save(Role role);

    Optional<Role> findById(Long id);

    Role update(Role role);

    void delete(Long id);

    List<Role> findAll();

    Optional<Role> findByName(String name);
}
