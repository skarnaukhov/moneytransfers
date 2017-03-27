package ru.sk.test.mt.data.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 * Class to hold H2 embedded DB session as single instance because of H2 limits
 */
public class HibernateUtil {
    private SessionFactory sessionFactory = new Configuration().configure()
            .buildSessionFactory();

    private volatile Session singleSession;

    /**
     * Thread safe gets or creates session object
     * @return single instance {@link Session} object
     */
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

    /**
     * @return a Transaction instance
     *
     * @see Session#beginTransaction()
     */
    public Transaction beginTransaction() {
        return getSession().beginTransaction();
    }
}
