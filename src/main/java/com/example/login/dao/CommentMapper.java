package com.example.login.dao;
import com.example.login.model.comment;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface CommentMapper {
    public static final String getOne = "SELECT * FROM comment where note_id = #{note_id} ";
    @Select(getOne)
    List<comment> getOne(int note_id);

    public static final String insert = "Insert into comment(note_id,content,username) VALUES (#{note_id},#{content},#{username}) ";
    @Insert(insert)
    int insert(int note_id,String content,String username);
}


