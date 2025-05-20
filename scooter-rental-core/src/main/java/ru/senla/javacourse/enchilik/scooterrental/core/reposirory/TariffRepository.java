package ru.senla.javacourse.enchilik.scooterrental.core.reposirory;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.TariffNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;

@Repository
public class TariffRepository extends AbstractDao<Tariff, Long> {
    @Override
    protected Class<Tariff> getEntityClass() {
        return Tariff.class;
    }

    public Tariff findBasic() {
        String hql = "FROM Tariff t WHERE t.type = 'BASIC'";
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Tariff> query = session.createQuery(hql, Tariff.class);
            Tariff tariff = query.getSingleResult();
            transaction.commit();
            return tariff;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Can't find the only one active BASIC tariff", e);
            throw new TariffNotFoundException("Can't find the only one active BASIC tariff", e);
        }

    }
}
