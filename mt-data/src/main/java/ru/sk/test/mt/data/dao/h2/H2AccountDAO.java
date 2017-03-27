package ru.sk.test.mt.data.dao.h2;

import org.hibernate.Session;
import ru.sk.test.mt.data.dao.AccountDAO;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.ExchangeRate;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class H2AccountDAO implements AccountDAO {

    private final Session h2Session;

    @Inject
    public H2AccountDAO(HibernateUtil hibernateUtil) {
        h2Session = hibernateUtil.getSession();
    }

    public Account getAccountById(long accountNumber) {
        final Account account = h2Session.get(Account.class, accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Not able to find account with number: " + accountNumber);
        }
        return account;
    }

    @Override
    public void update(@NotNull Account account) {
        h2Session.update(account);
    }
}
