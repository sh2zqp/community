package com.sh2zqp.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND("你找的问题不存在，可以换个试试看！！！");

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}