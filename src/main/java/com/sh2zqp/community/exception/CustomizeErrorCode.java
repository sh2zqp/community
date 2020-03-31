package com.sh2zqp.community.exception;

import edu.princeton.cs.algs4.In;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你找的问题不存在，可以换个试试看！！！"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复！！！"),
    NOT_LOGIN(2003, "当前操作需要登录 ，请登录后重试！！！"),
    SYSTEM_ERROR(2004, "网站太热了, 休息会儿再来吧！！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在！！！"),
    REPLY_COMMENT_NOT_FOUND(2005, "您回复的评论不存在，换个试试！！！"),
    REPLY_QUESTION_NOT_FOUND(2005, "您回复的问题不存在，换个试试！！！")
    ;

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
