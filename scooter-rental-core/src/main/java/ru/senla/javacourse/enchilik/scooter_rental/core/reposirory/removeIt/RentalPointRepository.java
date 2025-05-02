package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.removeIt;

import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.processing.HQL;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.RentalPoint;
import ru.senla.javacourse.enchilik.scooter_rental.core.reposirory.DAO;

@Repository
public interface RentalPointRepository extends DAO<RentalPoint, Long> {

    RentalPoint save(RentalPoint rentalpoint);

    Optional<RentalPoint> findById(Long id);

    RentalPoint update(RentalPoint rentalpoint);

    void delete(Long id);

    List<RentalPoint> findAll();

    @HQL("SELECT rp FROM RentalPoint rp WHERE rp.parentPoint IS NULL")
//    @Query("SELECT rp FROM RentalPoint rp WHERE rp.parentPoint IS NULL")
    List<RentalPoint> findRootRentalPoints();

    @Query("SELECT rp FROM RentalPoint rp WHERE rp.parentPoint.id = :parentId")
    List<RentalPoint> findByParentPointId(@Param("parentId") Long parentId);

    Optional<RentalPoint> findByIdAndParentPointIsNull(Long id);
}
