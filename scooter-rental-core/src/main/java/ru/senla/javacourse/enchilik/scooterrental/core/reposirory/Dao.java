package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import java.util.List;
import java.util.Optional;

public interface Dao<T, ID> {

    T save(T t);

    Optional<T> findById(ID id);

    T update(T t);

    void delete(ID id);

    List<T> findAll();

}
