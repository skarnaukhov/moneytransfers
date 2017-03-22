package ru.sk.test.mt.core;

import org.apache.log4j.Logger;
import ru.sk.test.mt.core.exception.NotEnoughFundsException;
import ru.sk.test.mt.data.entity.Account;

import java.math.BigDecimal;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class AccountService {

    private static final Logger logger = Logger.getLogger(AccountService.class);

    public void deposit(Account account, BigDecimal amount) {
        if (!greaterOrEqual(amount, new BigDecimal(0))) {
            throw new IllegalArgumentException("Not able to deposit negative or zero amount");
        }

        final BigDecimal totalValue = account.getBalance().add(amount);
        logger.info(String.format("Depositing to account: %s; was %f new value %f", account.getId(), account.getBalance(), totalValue));
        account.setBalance(totalValue);
    }

    public void withdraw(Account account, BigDecimal amount) {
        if (!greaterOrEqual(amount, new BigDecimal(0))) {
            throw new IllegalArgumentException("Not able to withdraw negative or zero amount");
        }

        final BigDecimal totalValue = account.getBalance().subtract(amount);
        if (greaterOrEqual(totalValue, new BigDecimal(0))) {
            logger.info(String.format("Withdrawing from account: %s; was %f new value %f", account.getId(), account.getBalance(), totalValue));
            account.setBalance(totalValue);
        } else {
            throw new NotEnoughFundsException(String.format("There are not enough funds on account %s", account.getId()));
        }
    }

    private boolean greaterOrEqual(BigDecimal firstValue, BigDecimal secondValue) {
        if (firstValue == null || secondValue == null) {
            return false;
        }
        return firstValue.compareTo(secondValue) != -1;
    }

}
