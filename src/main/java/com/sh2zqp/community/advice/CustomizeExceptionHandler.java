package com.sh2zqp.community.advice;

import com.sh2zqp.community.dto.ResultDTO;
import com.sh2zqp.community.exception.CustomizeErrorCode;
import com.sh2zqp.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request,
                        Throwable e,
                        Model model) {

        String contentType = request.getContentType();
        if (contentType.equals("application/json")) {
            // api接口调用，返回json
            if (e instanceof CustomizeException) {
                return ResultDTO.errorOf((CustomizeException) e);
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 页面调用，错误页面跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }

            return new ModelAndView("error");
        }
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.valueOf(statusCode);
    }
}
