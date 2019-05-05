package com.example.login.model;

public class Travel_notes {
    private Long id;
    private String username;
    private String textname;
    private String text;
    private String zhaiyao;
    private Long poll_num;

    public String getPoll_num()
    {
        return String.valueOf(this.poll_num);
    }

    public String getId()
    {
        return String.valueOf(this.id);
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getTextname()
    {
        return this.textname;
    }

    public String getText()
    {
        return this.text;
    }

    public String getZhaiyao()
    {
        return this.zhaiyao;
    }
}
