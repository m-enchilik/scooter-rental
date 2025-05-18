package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.DaoException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;

@Repository
public class ScooterRepository extends AbstractDao<Scooter, Long> {

    @Override
    protected Class<Scooter> getEntityClass() {
        return Scooter.class;
    }

    public List<Scooter> findByRentalPointId(Long rentalPointId) {
        String hql = "SELECT s FROM Scooter s WHERE s.rentalPoint.id = :rentalPointId";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Scooter> query = session.createQuery(hql, Scooter.class);
            query.setParameter("rentalPointId", rentalPointId);
            List<Scooter> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find scooters by rental-point id: '{}'", rentalPointId, e);
            throw new DaoException(e);
        }
    }

    public boolean existsBySerialNumber(String serialNumber) {
        String hql = "FROM Scooter WHERE serialNumber = :serialNumber";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Scooter> query = session.createQuery(hql, Scooter.class);
            query.setParameter("serialNumber", serialNumber);
            int results = query.getMaxResults();
            if (results != 1) {
                return false;
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find scooters by serial number: '{}'", serialNumber, e);
            return false;
        }
    }

    @Override
    protected void fillLazyFields(Scooter entity) {
        Hibernate.initialize(entity.getRentalPoint());
    }
}
