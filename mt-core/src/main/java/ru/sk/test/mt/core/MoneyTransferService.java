package ru.sk.test.mt.core;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import ru.sk.test.mt.core.dto.ExecutionResult;
import ru.sk.test.mt.core.lock.StripedLock;
import ru.sk.test.mt.data.dao.AccountDAO;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class MoneyTransferService extends AbstractExecutionResponse {

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

    public ExecutionResult getAccountBalance(long accNumber) {
        long[] accountIds = new long[]{accNumber};
        acquireLock(accountIds);
        final Account account;
        try {
            account = accountDAO.getAccountById(accNumber);
            return success(account.getCurrency().getCurrencyCode() + " " + account.getBalance());
        } catch (Exception e) {
            return processExceptionAndReturnDTO(e, "Error occurred while reading account.");
        } finally {
            lock.unlockIds(accountIds);
        }
    }

    public ExecutionResult transfer(long fromAccId, long toAccId, BigDecimal amount) {
        if (fromAccId == toAccId) {
            return success();
        }

        long[] accountIds = new long[]{fromAccId, toAccId};
        acquireLock(accountIds);
        final Transaction transaction = hibernateUtil.beginTransaction();
        final Account fromAccount = accountDAO.getAccountById(fromAccId);
        final Account toAccount = accountDAO.getAccountById(toAccId);
        try {
            accountService.withdraw(fromAccount, amount);
            if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
                amount = accountService.exchange(fromAccount.getCurrency(), toAccount.getCurrency(), amount);
            }
            accountService.deposit(toAccount, amount);
            accountDAO.update(fromAccount);
            accountDAO.update(toAccount);
            transaction.commit();
            return success();
        } catch (Exception e) {
            transaction.rollback();
            return processExceptionAndReturnDTO(e, "Error occurred while acquiring account data.");
        } finally {
            lock.unlockIds(accountIds);
        }
    }

    public ExecutionResult withdraw(long fromAccId, BigDecimal amount) {
        long[] accountIds = new long[]{fromAccId};
        acquireLock(accountIds);
        final Transaction transaction = hibernateUtil.beginTransaction();
        final Account fromAccount = accountDAO.getAccountById(fromAccId);
        try {
            accountService.withdraw(fromAccount, amount);
            accountDAO.update(fromAccount);
            transaction.commit();
            return success();
        } catch (Exception e) {
            transaction.rollback();
            return processExceptionAndReturnDTO(e, "Error occurred while acquiring account data");
        } finally {
            lock.unlockIds(accountIds);
        }
    }

    public ExecutionResult deposit(long toAccId, BigDecimal amount) {
        long[] accountIds = new long[]{toAccId};
        acquireLock(accountIds);
        final Transaction transaction = hibernateUtil.beginTransaction();
        final Account toAccount = accountDAO.getAccountById(toAccId);
        try {
            accountService.deposit(toAccount, amount);
            accountDAO.update(toAccount);
            transaction.commit();
            return success();
        } catch (Exception e) {
            transaction.rollback();
            return processExceptionAndReturnDTO(e, "Error occurred while acquiring account data");
        } finally {
            lock.unlockIds(accountIds);
        }
    }

    private void acquireLock(long[] accountIds) {
        long timeStamp = System.currentTimeMillis();
        logger.info("Acquiring lock for ids: " + Arrays.toString(accountIds));
        lock.lockIds(accountIds);
        logger.info("Lock acquired... time spent: " + (System.currentTimeMillis() - timeStamp));
    }

    private ExecutionResult processExceptionAndReturnDTO(Exception exception, String message) {
        logger.error(message, exception);
        return failure(message + " " + exception.getMessage());
    }


}
