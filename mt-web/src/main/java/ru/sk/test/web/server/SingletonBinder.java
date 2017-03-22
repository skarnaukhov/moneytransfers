package ru.sk.test.web.server;

import org.glassfish.hk2.api.Immediate;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import ru.sk.test.mt.core.AccountService;
import ru.sk.test.mt.core.MoneyTransferService;
import ru.sk.test.mt.data.dao.AccountDAO;
import ru.sk.test.mt.data.dao.UserDAO;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import javax.inject.Singleton;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class SingletonBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(UserDAO.class).to(UserDAO.class).in(Singleton.class);
        bind(AccountDAO.class).to(AccountDAO.class).in(Singleton.class);
        bind(MoneyTransferService.class).to(MoneyTransferService.class).in(Singleton.class);
        bind(HibernateUtil.class).to(HibernateUtil.class).in(Immediate.class);
        bind(AccountService.class).to(AccountService.class).in(Singleton.class);
    }
}
