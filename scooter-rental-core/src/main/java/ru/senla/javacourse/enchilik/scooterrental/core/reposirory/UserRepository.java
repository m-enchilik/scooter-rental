package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.DaoException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

@Repository
public class UserRepository extends AbstractDao<User, Long> {

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    public User findByUsername(String username) {
        String hql = "FROM User u WHERE u.username = :username";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            User user = query.getSingleResult();
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find user by username: '{}'", username, e);
            throw new DaoException(e);
        }
    }

    public User findByEmail(String email) {
        String hql = "FROM User u WHERE u.email = :email";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find user by email: '{}'", email, e);
            throw new DaoException(e);
        }
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }
}
