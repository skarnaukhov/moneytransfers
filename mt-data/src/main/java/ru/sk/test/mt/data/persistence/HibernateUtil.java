package ru.sk.test.mt.data.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class HibernateUtil {
    private SessionFactory sessionFactory = new Configuration().configure()
            .buildSessionFactory();

    public Session getNewSession() {
        return sessionFactory.openSession();
    }
}
