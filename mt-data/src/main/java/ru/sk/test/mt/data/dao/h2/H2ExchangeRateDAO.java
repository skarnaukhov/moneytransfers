package ru.sk.test.mt.data.dao.h2;

import org.hibernate.Session;
import ru.sk.test.mt.data.dao.ExchangeRateDAO;
import ru.sk.test.mt.data.entity.ExchangeRate;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import javax.inject.Inject;

/**
 * Created by Sergey_Karnaukhov on 26.03.2017
 */
public class H2ExchangeRateDAO implements ExchangeRateDAO {

    private final Session h2Session;

    @Inject
    public H2ExchangeRateDAO(HibernateUtil hibernateUtil) {
        h2Session = hibernateUtil.getSession();
    }

    @Override
    public ExchangeRate getExchangeRate(String currencyCode) {
        return h2Session.get(ExchangeRate.class, currencyCode);
    }
}
