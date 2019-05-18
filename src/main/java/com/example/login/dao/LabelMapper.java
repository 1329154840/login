package com.example.login.dao;

import com.example.login.model.Label;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
public interface LabelMapper {
    public static final String SELECT_ALL = "SELECT * FROM label";
    @Select(SELECT_ALL)
    List<Label> findAll();



    public static final String INSERT = "INSERT INTO label(name) VALUES (#{name})";
    @Insert(INSERT)
    int insert(String name);

}
