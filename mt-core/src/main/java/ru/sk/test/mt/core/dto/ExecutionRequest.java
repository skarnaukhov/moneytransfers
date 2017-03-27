package ru.sk.test.mt.core.dto;

import java.math.BigDecimal;

/**
 * Created by Sergey_Karnaukhov on 27.03.2017
 */
public class ExecutionRequest {

    private Long fromAccountNumber;
    private Long toAccountNumber;
    private BigDecimal amount;

    public Long getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(long fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public Long getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(long toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
