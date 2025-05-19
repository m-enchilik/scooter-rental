package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.DaoException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;

@Repository
public class RentalRepository extends AbstractDao<Rental, Long> {

    @Override
    protected Class<Rental> getEntityClass() {
        return Rental.class;
    }

    public List<Rental> findByUserId(Long userId) {
        String hql = "SELECT r FROM Rental r WHERE r.user.id = :userId";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Rental> query = session.createQuery(hql, Rental.class);
            query.setParameter("userId", userId);
            List<Rental> list = query.list();
            list.forEach(this::fillLazyFields);
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find rentals by user id: '{}'", userId, e);
            throw new DaoException(e);
        }
    }

    public List<Rental> findByScooterId(Long scooterId) {
        String hql = "SELECT r FROM Rental r WHERE r.scooter.id = :scooterId";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Rental> query = session.createQuery(hql, Rental.class);
            query.setParameter("scooterId", scooterId);
            List<Rental> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find rentals by scooter id: '{}'", scooterId, e);
            throw new DaoException(e);
        }
    }

    @Override
    protected void fillLazyFields(Rental entity) {
        Hibernate.initialize(entity.getScooter());
        Hibernate.initialize(entity.getUser());
        Hibernate.initialize(entity.getSubscription());
        Hibernate.initialize(entity.getSubscription().getTariff());
    }
}
