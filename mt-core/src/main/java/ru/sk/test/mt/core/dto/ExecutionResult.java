package ru.sk.test.mt.core.dto;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class ExecutionResult {

    private boolean success;
    private String message;

    public ExecutionResult(boolean success) {
        this.success = success;
    }

    public ExecutionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
