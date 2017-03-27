package ru.sk.test.mt.core;

import org.apache.log4j.Logger;
import ru.sk.test.mt.core.exception.NegativeAmountException;
import ru.sk.test.mt.core.exception.NotEnoughFundsException;
import ru.sk.test.mt.data.dao.ExchangeRateDAO;
import ru.sk.test.mt.data.entity.Account;
import ru.sk.test.mt.data.entity.ExchangeRate;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class AccountService {

    private static final Logger logger = Logger.getLogger(AccountService.class);

    @Inject
    private ExchangeRateDAO exchangeRateDAO;

    /**
     * Checks amount is valid and deposits to supplied account
     * @param account to deposit
     * @param amount to deposit
     */
    public void deposit(@NotNull Account account, @NotNull BigDecimal amount) {
        checkPositive(amount, "Not able to deposit negative or zero amount");

        final BigDecimal totalValue = account.getBalance().add(amount);
        logger.info(String.format("Depositing to account: %s; was %f new value %f", account.getId(), account.getBalance(), totalValue));
        account.setBalance(totalValue);
    }

    /**
     * Checks amount is valid; account has enough fund and withdraws from supplied account
     * @param account to withdraw
     * @param amount to withdraw
     */
    public void withdraw(@NotNull Account account, @NotNull BigDecimal amount) {
        checkPositive(amount, "Not able to withdraw negative or zero amount");

        final BigDecimal totalValue = account.getBalance().subtract(amount);
        if (greaterOrEqual(totalValue, new BigDecimal(0))) {
            logger.info(String.format("Withdrawing from account: %s; was %f new value %f", account.getId(), account.getBalance(), totalValue));
            account.setBalance(totalValue);
        } else {
            throw new NotEnoughFundsException(String.format("There are not enough funds on account %s", account.getId()));
        }
    }

    /**
     * Does conversion based on provided currencies;
     * Checks current exchange rate for calculations;
     * Currently supports only from or to USD conversions
     * @param fromCurrency object of type {@link Currency} as source of conversion
     * @param toCurrency object of type {@link Currency} as target for conversion
     * @param amount of money to be converted
     * @return amount based on conversion result
     */
    public BigDecimal exchange(@NotNull Currency fromCurrency, @NotNull Currency toCurrency, @NotNull BigDecimal amount) {
        checkPositive(amount, "Not able to exchange negative or zero amount");
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }
        logger.info(String.format("Exchanging %f %s to %s", amount, fromCurrency.getCurrencyCode(), toCurrency.getCurrencyCode()));
        ExchangeRate rate;
        if (fromCurrency.equals(ExchangeRate.BASE_CURRENCY)) {
            rate = exchangeRateDAO.getExchangeRate(toCurrency.getCurrencyCode());
            return amount.multiply(rate.getRate());
        } else {
            if (toCurrency.equals(ExchangeRate.BASE_CURRENCY)) {
                rate = exchangeRateDAO.getExchangeRate(fromCurrency.getCurrencyCode());
                return amount.divide(rate.getRate(), BigDecimal.ROUND_DOWN);
            } else {
                throw new IllegalArgumentException("Currency exchange without USD is not yet supported");
            }
        }
    }

    private boolean greaterOrEqual(BigDecimal firstValue, BigDecimal secondValue) {
        if (firstValue == null || secondValue == null) {
            return false;
        }
        return firstValue.compareTo(secondValue) != -1;
    }

    private void checkPositive(BigDecimal amount, String message) {
        if (!greaterOrEqual(amount, new BigDecimal(0))) {
            throw new NegativeAmountException(message);
        }
    }

}
