package ru.sk.test.mt.core;

import org.apache.log4j.Logger;
import ru.sk.test.mt.data.dao.UserDAO;
import ru.sk.test.mt.data.entity.Person;

import javax.inject.Inject;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class MoneyTransferService {

    private static final Logger logger = Logger.getLogger(MoneyTransferService.class);

    public MoneyTransferService() {
        logger.info("Initializing Transfer service");
    }

    @Inject
    private UserDAO userDAO;

    public Person getPersonById(Long id) {
        return userDAO.getById(id);
    }
}
