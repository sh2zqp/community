package com.sh2zqp.community.utils;

import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.User;
import com.sh2zqp.community.model.UserExample;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CommunityUtil {

    // 根据session中的token获得已有登录状态的用户
    public static void getSessionUser(HttpServletRequest request, UserMapper userMapper) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size() != 0)
                        request.getSession().setAttribute("user", users.get(0));
                        break;
                }
            }
        }
    }
}
