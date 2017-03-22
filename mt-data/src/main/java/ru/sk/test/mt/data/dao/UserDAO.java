package ru.sk.test.mt.data.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.Person;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import javax.inject.Inject;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
public class UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAO.class);

    @Inject
    private HibernateUtil hibernateUtil;

    public UserDAO() {
        logger.info("Initializing userDAO");
    }

    public Person getById(Long id){
        final Session session = hibernateUtil.getNewSession();
        session.getTransaction().begin();
        final Person person = session.get(Person.class, id);
        //todo
        for (Account account : person.getAccounts()) {
        }
        session.getTransaction().commit();
        session.close();
        return person;
    }
}
