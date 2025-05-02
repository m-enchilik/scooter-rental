package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooter_rental.core.model.RentalPoint;

@Repository
public class RentalPointRepository extends AbstractDAO<RentalPoint, Long> {

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    protected Class<RentalPoint> getEntityClass() {
        return RentalPoint.class;
    }


    //    @Query("SELECT rp FROM RentalPoint rp WHERE rp.parentPoint IS NULL")
    @HQL("SELECT rp FROM RentalPoint rp WHERE rp.parentPoint IS NULL")
    List<RentalPoint> findRootRentalPoints() {

        return null;
    }

    //    @Query("SELECT rp FROM RentalPoint rp WHERE rp.parentPoint.id = :parentId")
    @HQL("SELECT rp FROM RentalPoint rp WHERE rp.parentPoint.id = :parentId")
    List<RentalPoint> findByParentPointId(@Param("parentId") Long parentId) {
        Session session = sessionFactory.openSession();
        Query<RentalPoint> query = session.
        return null;
    }

    Optional<RentalPoint> findByIdAndParentPointIsNull(Long id);
}
}
