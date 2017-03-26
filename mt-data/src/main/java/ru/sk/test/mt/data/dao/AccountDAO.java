package ru.sk.test.mt.data.dao;

import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.ExchangeRate;

/**
 * Created by Sergey_Karnaukhov on 25.03.2017
 */
public interface AccountDAO {

    Account getAccountById(long accountNumber);

    void update(Account account);
}
