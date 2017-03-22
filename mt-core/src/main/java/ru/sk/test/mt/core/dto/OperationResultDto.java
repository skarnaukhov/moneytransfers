package ru.sk.test.mt.core.dto;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class OperationResultDto {

    private boolean success;
    private String message;

    public OperationResultDto(boolean success) {
        this.success = success;
    }

    public OperationResultDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
