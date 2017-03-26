package ru.sk.test.mt.data.entity;

import ru.sk.test.mt.data.entity.common.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
@Entity
@Table(name = "EXCHANGE_RATE")
public class ExchangeRate extends AbstractEntity<String> {

    public static final Currency BASE_CURRENCY = Currency.getInstance("USD");

    private BigDecimal rate;

    public ExchangeRate() {
        //jpa
    }

    public ExchangeRate(String id) {
        super(id);
    }

    @Id
    @Column(name = "CURRENCY", insertable = false, updatable = false)
    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    protected void setId(String id) {
        super.setId(id);
    }

    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
