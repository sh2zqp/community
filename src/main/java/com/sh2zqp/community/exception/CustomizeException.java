package com.sh2zqp.community.exception;

public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(ICustomizeErrorCode customizeErrorCode) {
        this.message = customizeErrorCode.getMessage();
    }


    @Override
    public String getMessage() {
        return message;
    }
}
