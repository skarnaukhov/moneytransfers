package ru.sk.test.mt.core;

import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.sk.test.mt.data.dao.AccountDAO;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.persistence.HibernateUtil;

import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Sergey_Karnaukhov on 26.03.2017
 */
@RunWith(MockitoJUnitRunner.class)
public class TestMoneyTransferService {

    private static final long fromAccountId = 1;
    private static final long toAccountId = 2;
    private static final BigDecimal fromAccountValue = BigDecimal.valueOf(10);
    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency GBP = Currency.getInstance("GBP");

    @InjectMocks
    private MoneyTransferService moneyTransferService = new MoneyTransferService();

    @Mock
    private AccountDAO accountDAO;

    @Mock
    private HibernateUtil hibernateUtil;

    @Mock
    private AccountService accountService;

    @Before
    public void setUp() {
        Transaction transaction = Mockito.mock(Transaction.class);
        doNothing().when(accountDAO).update(any(Account.class));
        when(hibernateUtil.beginTransaction()).thenReturn(transaction);
        doNothing().when(accountService).deposit(any(Account.class), any(BigDecimal.class));
        doNothing().when(accountService).withdraw(any(Account.class), any(BigDecimal.class));
    }

    @Test
    public void testTransferSameCurrencySuccess() {
        Account fromAccount = getFromAccount();
        Account toAccount = getToAccount();
        when(accountDAO.getAccountById(fromAccountId)).thenReturn(fromAccount);
        when(accountDAO.getAccountById(toAccountId)).thenReturn(toAccount);
        moneyTransferService.transfer(fromAccountId, toAccountId, fromAccountValue);

        verify(accountService).withdraw(fromAccount, fromAccountValue);
        verify(accountService).deposit(toAccount, fromAccountValue);
    }

    @Test
    public void testTransferCurrencyWithExchangeSuccess() {
        Account fromAccount = getFromAccount();
        Account toAccount = getAccount(toAccountId, GBP, BigDecimal.valueOf(0));
        when(accountDAO.getAccountById(fromAccountId)).thenReturn(fromAccount);
        when(accountDAO.getAccountById(toAccountId)).thenReturn(toAccount);
        BigDecimal valueAfterExchange = BigDecimal.valueOf(8);
        when(accountService.exchange(USD, GBP, fromAccountValue)).thenReturn(valueAfterExchange);
        moneyTransferService.transfer(fromAccountId, toAccountId, fromAccountValue);

        verify(accountService).withdraw(fromAccount, fromAccountValue);
        verify(accountService).deposit(toAccount, valueAfterExchange);
        verify(accountService).exchange(USD, GBP, fromAccountValue);
    }

    private Account getFromAccount() {
        return getAccount(fromAccountId, USD, fromAccountValue);
    }

    private Account getToAccount() {
        return getAccount(toAccountId, USD, BigDecimal.valueOf(0));
    }

    private Account getAccount(long id, Currency currency, BigDecimal amount) {
        Account account = new Account(id);
        account.setBalance(amount);
        account.setCurrency(currency);
        return account;
    }

}
