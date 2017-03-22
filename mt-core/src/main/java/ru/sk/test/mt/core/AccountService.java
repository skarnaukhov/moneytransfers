package ru.sk.test.mt.core;

import ru.sk.test.mt.core.exception.NotEnoughFundsException;
import ru.sk.test.mt.data.entity.Account;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class AccountService {

    public void deposit(Account account, BigDecimal amount) {
        if (!greaterOrEqual(amount, new BigDecimal(0))) {
            throw new IllegalArgumentException("Not able to deposit negative or zero amount");
        }

        final BigDecimal totalValue = account.getBalance().add(amount);
        account.setBalance(totalValue);
    }

    public void withdraw(Account account, BigDecimal amount) {
        if (!greaterOrEqual(amount, new BigDecimal(0))) {
            throw new IllegalArgumentException("Not able to withdraw negative or zero amount");
        }

        final BigDecimal totalValue = account.getBalance().subtract(amount);
        if (greaterOrEqual(totalValue, new BigDecimal(0))) {
            account.setBalance(totalValue);
        } else {
            throw new NotEnoughFundsException(String.format("There are not enough funds on account %s", account.getNumber()));
        }
    }

    private boolean greaterOrEqual(BigDecimal firstValue, BigDecimal secondValue) {
        if (firstValue == null || secondValue == null) {
            return false;
        }
        return firstValue.compareTo(secondValue) != -1;
    }

}
