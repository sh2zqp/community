package com.sh2zqp.community.service;

import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        User dbUser = userMapper.findUserByAccountId(user.getAccountId());
        if (dbUser == null) {
            // 新建用户插入用户
            userMapper.insert(user);
        } else {
            // 更新已存在的用户
            dbUser.setName(user.getName());
            dbUser.setBio(user.getBio());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setGmtModified(System.currentTimeMillis());
            userMapper.updateUser(dbUser);
        }
    }
}
