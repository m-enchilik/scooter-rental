package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.DaoException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Role;

@Repository
public class RoleRepository extends AbstractDao<Role, Long> {

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    public Role findByName(String name) {
        String hql = "FROM Role WHERE name = :name";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Role> query = session.createQuery(hql, Role.class);
            query.setParameter("name", name);
            Role role = query.getSingleResult();
            transaction.commit();
            return role;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find role by role-name: '{}'", name, e);
            throw new DaoException(e);
        }
    }
}
