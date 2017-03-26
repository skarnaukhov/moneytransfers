package ru.sk.test.mt.core.exception;

/**
 * Created by Sergey_Karnaukhov on 26.03.2017
 */
public class NegativeAmountException extends RuntimeException {

    public NegativeAmountException(String message) {
        super(message);
    }
}
