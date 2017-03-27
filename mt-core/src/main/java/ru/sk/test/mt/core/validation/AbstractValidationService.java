package ru.sk.test.mt.core.validation;

import ru.sk.test.mt.core.dto.ExecutionRequest;
import ru.sk.test.mt.core.exception.RequestValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey_Karnaukhov on 27.03.2017
 */
public abstract class AbstractValidationService {

    protected final ExecutionRequest executionRequest;
    private List<String> validationErrors = new ArrayList<>();

    protected static final String EMPTY_REQUEST_MESSAGE = "Execution request should be not null";
    protected static final String EMPTY_FROM_ACCOUNT_NUMBER = "From account number should be not null";
    protected static final String EMPTY_TO_ACCOUNT_NUMBER = "To account number should be not null";
    protected static final String EMPTY_AMOUNT = "Transfer amount should be not null";

    public AbstractValidationService(ExecutionRequest executionRequest) {
        this.executionRequest = executionRequest;
    }

    public abstract void validate() throws RequestValidationException;

    protected void addValidationError(String validationError) {
        this.validationErrors.add(validationError);
    }

    protected void processErrorsIfExist() throws RequestValidationException {
        if (this.validationFailed()) {
            throw new RequestValidationException(getValidationErrorMessage());
        }
    }

    private boolean validationFailed() {
        return !validationErrors.isEmpty();
    }

    private String getValidationErrorMessage() {
        String validationErrorMessage = "Validation error.";
        for (String validationError : validationErrors) {
            validationErrorMessage += "\n" + validationError;
        }
        return validationErrorMessage;
    }

}
