package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.DaoException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;

@Repository
public class SubscriptionRepository extends AbstractDao<Subscription, Long> {

    @Override
    protected Class<Subscription> getEntityClass() {
        return Subscription.class;
    }

    public List<Subscription> findByUserId(Long userId) {
        String hql = "SELECT s FROM Subscription s WHERE s.user.id = :userId";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Subscription> query = session.createQuery(hql, Subscription.class);
            query.setParameter("userId", userId);
            List<Subscription> list = query.list();
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find subscriptions by user id: '{}'", userId, e);
            throw new DaoException(e);
        }
    }


}
