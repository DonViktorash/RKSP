package utils;

import models.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Hibernate {
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        System.out.println("#buildSessionFactory");
        try {
            if (sessionFactory == null) {
                Configuration configuration = new Configuration().configure()
                        .addAnnotatedClass(Book.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            }
            return sessionFactory;
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Session openSession() {
        return sessionFactory.openSession();
    }

    public static List<Book> getListOfBooks() {
        Session session = openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        criteriaQuery.select(root);
        Query<Book> query = session.createQuery(criteriaQuery);
        List<Book> books = query.getResultList();
        session.close();
        return books;
    }

    public static void applyTransaction(Book book, Operation operation) {
        Session session = openSession();
        session.beginTransaction();
        switch (operation) {
            case ADD:    session.save(  book); break;
            case UPDATE: session.update(book); break;
            case DELETE: session.delete(book); break;
        }
        session.getTransaction().commit();
        session.close();
    }

    public enum Operation {
        ADD,
        UPDATE,
        DELETE
    }
}

