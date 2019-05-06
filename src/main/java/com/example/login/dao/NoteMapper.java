package com.example.login.dao;

import com.example.login.model.Travel_notes;
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
public interface NoteMapper {
    public static final String SELECT_ALL = "SELECT * FROM travel_notes ";
    @Select(SELECT_ALL)
    List<Travel_notes> findAll();

    public static final String getOne = "SELECT * FROM travel_notes where id = #{id} ";
    @Select(getOne)
    Travel_notes getOne(int id);

    public static final String getMy_note = "SELECT * FROM travel_notes where username = #{usernam} ";
    @Select(getMy_note)
    List<Travel_notes> getMy_note(String usernam);

    public static final String INSERT = "INSERT INTO travel_notes(username,textname,text,zhaiyao) VALUES (#{username},#{textname},#{text},#{zhaiyao})";
    @Insert(INSERT)
    int insert(String username,String textname,String text,String zhaiyao);

    public static final String view_num = "SELECT * FROM travel_notes order by view_num desc";
    @Select(view_num)
    List<Travel_notes> view_num();

    public static final String comment_num = "SELECT * FROM travel_notes order by comment_num desc";
    @Select(comment_num)
    List<Travel_notes> comment_num();

    public static final String poll_num = "SELECT * FROM travel_notes order by poll_num desc";
    @Select(poll_num)
    List<Travel_notes> poll_num();

    public static final String search = "SELECT * FROM travel_notes where textname like concat('%',#{keyword},'%')";
    @Select(search)
    List<Travel_notes> search(String keyword);

    public static final String add_view = "UPDATE travel_notes set view_num=view_num+1 where id=#{id};";
    @Update(add_view)
    int add_view(int id);

    public static final String add_comment = "UPDATE travel_notes set comment_num=comment_num+1 where id=#{id};";
    @Update(add_comment)
    int add_comment(int id);

    public static final String add_poll = "UPDATE travel_notes set poll_num=poll_num+1 where id=#{id};";
    @Update(add_poll)
    int add_poll(int id);

    public static final String delete = "Delete from travel_notes where id=#{id}";
    @Delete(delete)
    int delete(int id);
}