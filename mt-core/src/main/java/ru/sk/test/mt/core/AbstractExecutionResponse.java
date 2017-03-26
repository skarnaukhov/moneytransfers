package ru.sk.test.mt.core;

import ru.sk.test.mt.core.dto.ExecutionResult;

/**
 * Created by Sergey_Karnaukhov on 26.03.2017
 */
public abstract class AbstractExecutionResponse {

    public final ExecutionResult success() {
        return new ExecutionResult(true);
    }

    public final ExecutionResult success(String message) {
        return new ExecutionResult(true, message);
    }

    public final ExecutionResult failure(String message) {
        return new ExecutionResult(false, message);
    }
}
