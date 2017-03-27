package ru.sk.test.mt.core.validation;

import ru.sk.test.mt.core.dto.ExecutionRequest;
import ru.sk.test.mt.core.exception.RequestValidationException;

/**
 * Created by Sergey_Karnaukhov on 27.03.2017
 */
public class WithdrawRqValidationService extends AbstractValidationService {

    public WithdrawRqValidationService(ExecutionRequest executionRequest) {
        super(executionRequest);
    }

    @Override
    public void validate() throws RequestValidationException {
        if (executionRequest == null) {
            addValidationError(EMPTY_REQUEST_MESSAGE);
        } else {
            if (executionRequest.getFromAccountNumber() == null) {
                addValidationError(EMPTY_FROM_ACCOUNT_NUMBER);
            }
            if (executionRequest.getAmount() == null) {
                addValidationError(EMPTY_AMOUNT);
            }
        }
        processErrorsIfExist();
    }

}
