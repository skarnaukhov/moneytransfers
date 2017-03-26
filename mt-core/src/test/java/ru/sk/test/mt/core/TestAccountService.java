package ru.sk.test.mt.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.sk.test.mt.core.exception.NegativeAmountException;
import ru.sk.test.mt.core.exception.NotEnoughFundsException;
import ru.sk.test.mt.data.dao.ExchangeRateDAO;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Sergey_Karnaukhov on 26.03.2017
 */
@RunWith(MockitoJUnitRunner.class)
public class TestAccountService {

    @InjectMocks
    private AccountService accountService = new AccountService();

    @Mock
    private ExchangeRateDAO exchangeRateDAO;

    @Before
    public void setUp() {
        when(exchangeRateDAO.getExchangeRate(Currency.getInstance("GBP").getCurrencyCode())).thenReturn(getUSDtoGBPExchangeRate());
    }

    @Test
    public void testExchangePositive() {
        BigDecimal amount = accountService.exchange(Currency.getInstance("USD"), Currency.getInstance("GBP"), BigDecimal.valueOf(2));
        assertEquals(amount, BigDecimal.valueOf(1.6));
    }

    @Test(expected = NegativeAmountException.class)
    public void testExchangeNegative() {
        accountService.exchange(Currency.getInstance("USD"), Currency.getInstance("GBP"), BigDecimal.valueOf(-1));
    }


    @Test
    public void testDepositPositive() {
        Account account = getZeroBalancedAccount();
        accountService.deposit(account, BigDecimal.valueOf(10));
        assertEquals(account.getBalance(), BigDecimal.valueOf(10));
    }

    @Test(expected = NegativeAmountException.class)
    public void testDepositNegative() {
        Account account = getZeroBalancedAccount();
        accountService.deposit(account, BigDecimal.valueOf(-10));
    }

    @Test
    public void testWithdrawPositive() {
        Account account = getZeroBalancedAccount();
        accountService.deposit(account, BigDecimal.valueOf(10));
        accountService.withdraw(account, BigDecimal.valueOf(5));
        assertEquals(account.getBalance(), BigDecimal.valueOf(5));
    }

    @Test(expected = NegativeAmountException.class)
    public void testWithdrawNegative() {
        Account account = getZeroBalancedAccount();
        accountService.deposit(account, BigDecimal.valueOf(10));
        accountService.withdraw(account, BigDecimal.valueOf(-5));
    }

    @Test(expected = NotEnoughFundsException.class)
    public void testWithdrawOverdraft() {
        Account account = getZeroBalancedAccount();
        accountService.withdraw(account, BigDecimal.valueOf(1));
    }

    private Account getZeroBalancedAccount() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(0));
        return account;
    }

    private ExchangeRate getUSDtoGBPExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate("GBP");
        exchangeRate.setRate(BigDecimal.valueOf(0.8));
        return exchangeRate;
    }
}
