package ru.sk.test.mt.data.entity;

import com.google.gson.annotations.Expose;
import ru.sk.test.mt.data.entity.common.AbstractEntity;
import ru.sk.test.mt.data.entity.common.gson.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by Sergey_Karnaukhov on 20.03.2017
 */
@Entity
@Table(name = "ACCOUNT")
public class Account extends AbstractEntity<Long> {

    private Long number;
    @Embedded
    private String currency;
    private BigDecimal balance;
    private Person person;

    public Account() {
        //JPA
    }

    public Account(Long id) {
        super(id);
    }

    @Id
    @Column(name = "ACCOUNT_ID", insertable = false, updatable = false)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    protected void setId(Long aLong) {
        super.setId(aLong);
    }

    @Column(name = "NUMBER")
    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Column(name = "CURRENCY")
    public Currency getCurrency() {
        return Currency.getInstance(currency);
    }

    public void setCurrency(Currency currency) {
        this.currency = currency.getCurrencyCode();
    }

    @Column(name = "BALANCE")
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
