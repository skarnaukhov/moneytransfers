package ru.sk.test.mt.data.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.Person;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import javax.inject.Inject;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class AccountDAO {

    @Inject
    private HibernateUtil hibernateUtil;

    public Account getAccountByOwnerIdAndNumber(long ownerId, long accountNumber) {
        final Session session = hibernateUtil.getNewSession();
        session.getTransaction().begin();
        final Account account = (Account)session.createQuery("from Account where number = ? and person.id = ?")
                .setParameter(0, accountNumber).setParameter(1, ownerId).getSingleResult();

        session.getTransaction().commit();
        session.close();
        return account;
    }
}
