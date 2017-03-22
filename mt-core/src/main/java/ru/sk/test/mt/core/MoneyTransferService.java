package ru.sk.test.mt.core;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.sk.test.mt.core.dto.OperationResultDto;
import ru.sk.test.mt.core.lock.StripedLock;
import ru.sk.test.mt.data.dao.AccountDAO;
import ru.sk.test.mt.data.dao.UserDAO;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.Person;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * Created by Sergey_Karnaukhov on 21.03.2017
 */
public class MoneyTransferService {

    private static final Logger logger = Logger.getLogger(MoneyTransferService.class);
    private final Map<Account, Lock> accountLockMap;
    private final StripedLock lock;

    public MoneyTransferService() {
        logger.info("Initializing Transfer service");
        accountLockMap = new ConcurrentHashMap<>();
        lock = new StripedLock();
    }

    @Inject
    private UserDAO userDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private HibernateUtil hibernateUtil;

    @Inject
    private AccountService accountService;

    public Person getPersonById(Long id) {
        return userDAO.getById(id);
    }

    public OperationResultDto getAccountBalance(long ownerId, long accNumber) {
        final Account account = accountDAO.getAccountByOwnerIdAndNumber(ownerId, accNumber);
        if (account == null) {
            return new OperationResultDto(false, "Could not find an account with such params");
        }
        return new OperationResultDto(true, account.getCurrency().getCurrencyCode() + " " + account.getBalance());
    }

    public OperationResultDto transfer(long fromUserId, long fromAccId, long toUserId, long toAccId, BigDecimal amount) {
        final Account fromAccount = accountDAO.getAccountByOwnerIdAndNumber(fromUserId, fromAccId);
        final Account toAccount = accountDAO.getAccountByOwnerIdAndNumber(toUserId, toAccId);
        long[] accountIds = new long[]{fromAccount.getId(), toAccount.getId()};
        lock.lockIds(accountIds);
        final Session session = hibernateUtil.getNewSession();
        final Transaction transaction = session.getTransaction();
        transaction.begin();
        try {
            accountService.withdraw(fromAccount, amount);
            accountService.deposit(toAccount, amount);
            session.update(fromAccount);
            session.update(toAccount);
            transaction.commit();
            session.close();
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
