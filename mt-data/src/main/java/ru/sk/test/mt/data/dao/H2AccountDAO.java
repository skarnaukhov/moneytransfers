package ru.sk.test.mt.data.dao;

import org.hibernate.Session;
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
        return h2Session.get(Account.class, accountNumber);
    }

    public ExchangeRate getExchangeRate(String currencyCode) {
        return h2Session.get(ExchangeRate.class, currencyCode);
    }

    @Override
    public void update(@NotNull Account account) {
        h2Session.update(account);
    }
}
