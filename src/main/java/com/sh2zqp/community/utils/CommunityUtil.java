package com.sh2zqp.community.utils;

import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CommunityUtil {

    // 根据session中的token获得已有登录状态的用户
    public static User getSessionUser(HttpServletRequest request, UserMapper userMapper) {
        User user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findUserByToken(token);
                    if (user != null)
                        break;
                }
            }
        }
        return user;
    }
}
