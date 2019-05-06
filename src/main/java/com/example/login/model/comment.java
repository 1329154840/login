package com.example.login.model;

public class comment {
    private Long id;
    private Long note_id;
    private String username;
    private String content;

    public String getId()
    {
        return String.valueOf(this.id);
    }
    public String getNote_id()
    {
        return String.valueOf(this.note_id);
    }
    public String getUser_name(){
        return this.username;
    }
    public String getContent(){
        return this.content;
    }
}
