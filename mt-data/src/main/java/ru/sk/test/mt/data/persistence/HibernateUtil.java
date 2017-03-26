package ru.sk.test.mt.data.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class HibernateUtil {
    private SessionFactory sessionFactory = new Configuration().configure()
            .buildSessionFactory();

    private volatile Session singleSession;

    public Session getSession() {
        Session localInstance = singleSession;
        if (localInstance == null) {
            synchronized (this) {
                localInstance = singleSession;
                if (localInstance == null) {
                    singleSession = localInstance = sessionFactory.openSession();
                }
            }
        }
        return localInstance;
    }

    public Transaction beginTransaction() {
        return getSession().beginTransaction();
    }
}
