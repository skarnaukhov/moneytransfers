package ru.sk.test.mt.core.exception;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class NotEnoughFundsException extends RuntimeException {

    public NotEnoughFundsException(String message) {
        super(message);
    }
}
