package com.sh2zqp.community.service;

import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.User;
import com.sh2zqp.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
        if (dbUsers.size() == 0) {
            // 新建用户插入用户
            userMapper.insert(user);
        } else {
            // 更新已存在的用户
            User dbUser = dbUsers.get(0); // 数据库中已存在的User
            User updateUser = new User();
            updateUser.setName(user.getName());
            updateUser.setBio(user.getBio());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setToken(user.getToken());
            updateUser.setGmtModified(System.currentTimeMillis());
            UserExample updateUserExample = new UserExample();
            updateUserExample.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, updateUserExample);
        }
    }
}
