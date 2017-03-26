package ru.sk.test.mt.core;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import ru.sk.test.mt.core.dto.OperationResultDto;
import ru.sk.test.mt.core.lock.StripedLock;
import ru.sk.test.mt.data.dao.AccountDAO;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.ExchangeRate;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class MoneyTransferService {

    private static final Logger logger = Logger.getLogger(MoneyTransferService.class);
    private final StripedLock lock;

    public MoneyTransferService() {
        logger.info("Initializing Transfer service");
        lock = new StripedLock();
    }

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private HibernateUtil hibernateUtil;

    @Inject
    private AccountService accountService;

    public OperationResultDto getAccountBalance(long accNumber) {
        long[] accountIds = new long[]{accNumber};
        lock.lockIds(accountIds);
        final Account account = accountDAO.getAccountById(accNumber);
        lock.unlockIds(accountIds);
        if (account == null) {
            return new OperationResultDto(false, "Could not find an account with such params");
        }
        return new OperationResultDto(true, account.getCurrency().getCurrencyCode() + " " + account.getBalance());
    }

    public OperationResultDto transfer(long fromAccId, long toAccId, BigDecimal amount) {
        long[] accountIds = new long[]{fromAccId, toAccId};
        long timeStamp = System.currentTimeMillis();
        logger.info("Acquiring lock...");
        lock.lockIds(accountIds);
        logger.info("Lock acquired... time spent: " + (System.currentTimeMillis() - timeStamp));
        final Transaction transaction = hibernateUtil.beginTransaction();
        final Account fromAccount = accountDAO.getAccountById(fromAccId);
        final Account toAccount = accountDAO.getAccountById(toAccId);
        try {
            accountService.withdraw(fromAccount, amount);
            if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
                amount = exchange(fromAccount.getCurrency(), toAccount.getCurrency(), amount);
            }
            accountService.deposit(toAccount, amount);
            accountDAO.update(fromAccount);
            accountDAO.update(toAccount);
            transaction.commit();
            return new OperationResultDto(true);
        } catch (Exception e) {
            String message = "Error occurred while acquiring account data";
            logger.error(message, e);
            transaction.rollback();
            return new OperationResultDto(false, message + ": " + e.getMessage());
        } finally {
            lock.unlockIds(accountIds);
        }
    }

    private BigDecimal exchange(Currency fromCurrency, Currency toCurrency, BigDecimal amount) {
        ExchangeRate rate;
        if (fromCurrency.equals(ExchangeRate.BASE_CURRENCY)) {
             rate = accountDAO.getExchangeRate(toCurrency.getCurrencyCode());
            return amount.multiply(rate.getRate());
        } else {
            if (toCurrency.equals(ExchangeRate.BASE_CURRENCY)) {
                rate = accountDAO.getExchangeRate(fromCurrency.getCurrencyCode());
                return amount.divide(rate.getRate(), BigDecimal.ROUND_DOWN);
            } else {
                throw new IllegalArgumentException("Currency exchange without USD is not yet supported");
            }
        }
    }

    public OperationResultDto withdraw(long fromAccId, BigDecimal amount) {
        long[] accountIds = new long[]{fromAccId};
        long timeStamp = System.currentTimeMillis();
        logger.info("Acquiring lock...");
        lock.lockIds(accountIds);
        logger.info("Lock acquired... time spent: " + (System.currentTimeMillis() - timeStamp));
        final Transaction transaction = hibernateUtil.beginTransaction();
        final Account fromAccount = accountDAO.getAccountById(fromAccId);
        try {
            accountService.withdraw(fromAccount, amount);
            accountDAO.update(fromAccount);
            transaction.commit();
            return new OperationResultDto(true);
        } catch (Exception e) {
            String message = "Error occurred while acquiring account data";
            logger.error(message, e);
            transaction.rollback();
            return new OperationResultDto(false, message + ": " + e.getMessage());
        } finally {
            lock.unlockIds(accountIds);
        }
    }

    public OperationResultDto deposit(long toAccId, BigDecimal amount) {
        long[] accountIds = new long[]{toAccId};
        long timeStamp = System.currentTimeMillis();
        logger.info("Acquiring lock...");
        lock.lockIds(accountIds);
        logger.info("Lock acquired... time spent: " + (System.currentTimeMillis() - timeStamp));
        final Transaction transaction = hibernateUtil.beginTransaction();
        final Account toAccount = accountDAO.getAccountById(toAccId);
        try {
            accountService.deposit(toAccount, amount);
            accountDAO.update(toAccount);
            transaction.commit();
            return new OperationResultDto(true);
        } catch (Exception e) {
            String message = "Error occurred while acquiring account data";
            logger.error(message, e);
            transaction.rollback();
            return new OperationResultDto(false, message + ": " + e.getMessage());
        } finally {
            lock.unlockIds(accountIds);
        }
    }


}
