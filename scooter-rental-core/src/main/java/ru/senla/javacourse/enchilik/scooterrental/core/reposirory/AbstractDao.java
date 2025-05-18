package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.DaoException;


public abstract class AbstractDao<T, K extends Serializable> implements Dao<T, K> {
    public static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public T save(T entity) throws DaoException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T persisted = session.merge(entity);
            fillLazyFields(persisted);
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
    public Optional<T> findById(K entityK) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(getEntityClass(), entityK);
            if (entity != null) {
                fillLazyFields(entity);
            }
            transaction.commit();
            return Optional.ofNullable(entity);
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
    public void delete(K entityK) throws DaoException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Optional<T> entity = findById(entityK);
            if (entity.isEmpty()) {
                logger.error("Entity not exists");
                return;
            }
            session.remove(entity.get());
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

    public boolean existsById(K k) {
        return findById(k).isPresent();
    }
}
