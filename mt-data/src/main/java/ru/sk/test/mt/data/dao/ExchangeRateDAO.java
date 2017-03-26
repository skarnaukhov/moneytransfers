package ru.sk.test.mt.data.dao;

import ru.sk.test.mt.data.entity.ExchangeRate;

/**
 * Created by Sergey_Karnaukhov on 26.03.2017
 */
public interface ExchangeRateDAO {

    ExchangeRate getExchangeRate(String currencyCode);
}
