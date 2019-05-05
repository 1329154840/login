package com.example.login.dao;

import com.example.login.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-04-26
 * Time: 21:54
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Mapper
public interface UserMapper {
    public static final String SELECT_ALL = "SELECT username,password FROM user ";
    @Select(SELECT_ALL)
    List<User> findAll();

    public static final String getOne = "SELECT username,password FROM user where username = #{username} and password = #{password}";
    @Select(getOne)
//    @Results({
//            @Result(property = "username",column = "username"),
//            @Result(property = "password",column = "password")
//    })
    List<User> getOne(String username,String password);

    public static final String INSERT = "INSERT INTO user(username,password) VALUES (#{username},#{password})";
    @Insert(INSERT)
    int insert(String username,String password);

}
