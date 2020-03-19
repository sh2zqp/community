package com.sh2zqp.community.mapper;

import com.sh2zqp.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into community.user(name, account_id, token, gmt_create, gmt_modified) " +
            "values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified})")
    void insert(User user);

    @Select("select * from community.user where token = #{token}")
    User findUserByToken(@Param("token") String token);
}
