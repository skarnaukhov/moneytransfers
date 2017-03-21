package ru.sk.test.mt.data.dao;

import org.apache.log4j.Logger;
import ru.sk.test.mt.data.entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
public class UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAO.class);

    private EntityManager em;

    public UserDAO() {
        logger.info("Initializing userDAO");
        em = Persistence.createEntityManagerFactory("MONEY_TRANSFER").createEntityManager();
    }

    public Person getById(Long id){
        em.getTransaction().begin();
        Person person = em.find(Person.class, id);
        em.getTransaction().commit();
        return person;
    }
}
