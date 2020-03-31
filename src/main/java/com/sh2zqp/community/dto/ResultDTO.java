
package com.sh2zqp.community.dto;

import com.sh2zqp.community.exception.CustomizeErrorCode;
import com.sh2zqp.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO {
    private Integer code;
    private String message;

    private ResultDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultDTO errorOf(Integer code, String message) {
        return new ResultDTO(code, message);
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    public static ResultDTO okOf() {
        return new ResultDTO(200, "请求成功");
    }
}