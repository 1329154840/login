package com.example.login.dao;
import com.example.login.model.comment;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface CommentMapper {
    public static final String getOne = "SELECT * FROM comment where note_id = #{note_id} ";
    @Select(getOne)
    List<comment> getOne(int note_id);

    public static final String insert = "Insert into comment(content) VALUES (#{content}) ";
    @Insert(insert)
    int insert(String content);
}


