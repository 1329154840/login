package com.example.login.model;

public class comment {
    private Long id;
    private Long user_id;
    private Long note_id;
    private String user_name;
    private String content;

    public String getId()
    {
        return String.valueOf(this.id);
    }
    public String getUser_id()
    {
        return String.valueOf(this.user_id);
    }
    public String getNote_id()
    {
        return String.valueOf(this.note_id);
    }
    public String getUser_name(){
        return this.user_name;
    }
    public String getContent(){
        return this.content;
    }
}
