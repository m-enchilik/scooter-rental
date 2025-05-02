package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {

    T save(T t);

    Optional<T> findById(ID id);

    T update(T t);

    void delete(ID id);

    List<T> findAll();

}
