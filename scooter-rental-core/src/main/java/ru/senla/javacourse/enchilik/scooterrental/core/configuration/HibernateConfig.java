package ru.senla.javacourse.enchilik.scooterrental.core.configuration;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Rental;
import ru.senla.javacourse.enchilik.scooterrental.core.model.RentalPoint;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Scooter;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Subscription;
import ru.senla.javacourse.enchilik.scooterrental.core.model.Tariff;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;

@Configuration
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory(
            JdbcConnectionDetails connectionDetails,
            FlywayMigrationInitializer dependency // Чтобы запускалось строго после Flyway
    ) {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        // Hibernate settings equivalent to hibernate.cfg.xml's properties
        Properties properties = new Properties();
        properties.put(Environment.JAKARTA_JDBC_DRIVER, connectionDetails.getDriverClassName());
        properties.put(Environment.JAKARTA_JDBC_URL, connectionDetails.getJdbcUrl());
        properties.put(Environment.JAKARTA_JDBC_USER, connectionDetails.getUsername());
        properties.put(Environment.JAKARTA_JDBC_PASSWORD, connectionDetails.getPassword());
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

        properties.put(Environment.SHOW_SQL, "true");

        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.POOL_SIZE, 10);

        properties.put(Environment.HBM2DDL_AUTO, "validate");

        configuration.setProperties(properties);

        configuration.addAnnotatedClass(Rental.class);
        configuration.addAnnotatedClass(RentalPoint.class);
        configuration.addAnnotatedClass(Scooter.class);
        configuration.addAnnotatedClass(Subscription.class);
        configuration.addAnnotatedClass(Tariff.class);
        configuration.addAnnotatedClass(User.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}