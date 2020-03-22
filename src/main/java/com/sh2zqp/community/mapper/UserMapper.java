package com.sh2zqp.community.mapper;

import com.sh2zqp.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name, account_id, token, gmt_create, gmt_modified, bio, avatar_url) " +
            "values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{bio}, #{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findUserByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findUserById(@Param("id") Integer id);

    @Select("select * from user where account_id = #{accountId}")
    User findUserByAccountId(@Param("accountId") String accountId);

    @Update("update user " +
            "set name = #{name}, token = #{token}, bio=#{bio}, avatar_url = #{avatarUrl}, gmt_modified = #{gmtModified} " +
            "where account_id = #{accountId}")
    void updateUser(User user);
}
