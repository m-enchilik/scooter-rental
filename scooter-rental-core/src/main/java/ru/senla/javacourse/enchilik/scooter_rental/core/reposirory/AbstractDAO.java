package ru.senla.javacourse.enchilik.scooter_rental.core.reposirory;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.senla.javacourse.enchilik.scooter_rental.core.exception.DaoException;


public abstract class AbstractDAO<T, PK extends Serializable> implements DAO<T, PK> {
    public static final Logger logger = LoggerFactory.getLogger(AbstractDAO.class);

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public T save(T entity) throws DaoException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T persisted = session.merge(entity);
            transaction.commit();
            return persisted;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't save entity", e);
            throw new DaoException(e);
        }
    }

//    TODO: можно ли заменить на @Transactional
    @Override
    public Optional<T> findById(PK entityId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(getEntityClass(), entityId);
            transaction.commit();
            fillLazyFields(entity);
            return Optional.of(entity);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find entity", e);
            return Optional.empty();
        }
    }

    protected void fillLazyFields(T entity) {
        // Override if need with Hibernate.initialize(entity.getSomeCollection());
    }


    protected abstract Class<T> getEntityClass();


    @Override
    public T update(T entity) throws DaoException {
        return save(entity);
    }


    @Override
    public void delete(PK entityId) throws DaoException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(entityId);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't delete entity", e);
            throw new DaoException(e);
        }
    }


    @Override
    public List<T> findAll() throws DaoException {
        Transaction transaction = null;
        List<T> entities;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteria = session.getCriteriaBuilder();
            CriteriaQuery<T> query = criteria.createQuery(getEntityClass());
            query.from(getEntityClass());
            entities = session.createQuery(query).getResultList();
            entities.forEach(this::fillLazyFields);
            transaction.commit();
            return entities;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't get collection entity", e);
            throw new DaoException(e);
        }
    }
}
