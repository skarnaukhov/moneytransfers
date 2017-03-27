package ru.sk.test.mt.core.validation;

import ru.sk.test.mt.core.dto.ExecutionRequest;
import ru.sk.test.mt.core.exception.RequestValidationException;

/**
 * Created by Sergey_Karnaukhov on 27.03.2017
 */
public class DepositRqValidationService extends AbstractValidationService {

    public DepositRqValidationService(ExecutionRequest executionRequest) {
        super(executionRequest);
    }

    @Override
    public void validate() throws RequestValidationException {
        if (executionRequest == null) {
            addValidationError(EMPTY_REQUEST_MESSAGE);
        } else {
            if (executionRequest.getToAccountNumber() == null) {
                addValidationError(EMPTY_TO_ACCOUNT_NUMBER);
            }
            if (executionRequest.getAmount() == null) {
                addValidationError(EMPTY_AMOUNT);
            }
        }
        processErrorsIfExist();
    }
}
