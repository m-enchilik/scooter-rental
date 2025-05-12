package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.DaoException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.RentalPoint;

@Repository
public class RentalPointRepository extends AbstractDao<RentalPoint, Long> {

    @Override
    protected Class<RentalPoint> getEntityClass() {
        return RentalPoint.class;
    }

    public List<RentalPoint> findRootRentalPoints() {
        String hql = "SELECT rp FROM RentalPoint rp WHERE rp.parentPoint IS NULL";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<RentalPoint> query = session.createQuery(hql, RentalPoint.class);
            List<RentalPoint> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find root rental-points", e);
            throw new DaoException(e);
        }
    }

    public List<RentalPoint> findByParentPointId(Long parentId) {
        String hql = "SELECT rp FROM RentalPoint rp WHERE rp.parentPoint.id = :parentId";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<RentalPoint> query = session.createQuery(hql, RentalPoint.class);
            query.setParameter("parentId", parentId);
            List<RentalPoint> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find rental-points by parent id: '{}'", parentId, e);
            throw new DaoException(e);
        }
    }
}
