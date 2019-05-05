package com.example.login.controller;

import com.example.login.dao.CommentMapper;
import com.example.login.dao.UserMapper;
import com.example.login.model.User;
import com.example.login.dao.NoteMapper;
import com.example.login.model.Travel_notes;
import com.example.login.model.comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;


import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-04-26
 * Time: 21:52
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Controller
@Slf4j

public class TestController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private CommentMapper commentMapper ;

    @RequestMapping(value = "/index")
    public String main(Model model){

        List<Travel_notes> noteList = noteMapper.findAll();
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (Travel_notes note :noteList){
            Map<String,String> map= new HashMap<>();
            map.put("id",note.getId());
            map.put("username",note.getUsername());
            map.put("textname",note.getTextname());
            if (note.getText()!=null && note.getText().length()>12)
                map.put("text",note.getText().substring(0,11));
            else
                map.put("text",note.getText());
            if (note.getZhaiyao()!=null && (note.getZhaiyao()).length()>12)
                map.put("zhaiyao",note.getZhaiyao().substring(0,11));
            else
                map.put("zhaiyao",note.getZhaiyao());
            mapList.add(map);

        }
        model.addAttribute("mapList",mapList);
        return "index";
    }

    @RequestMapping(value = "/")
    public String error(Model model){

        return "login";
    }

    @RequestMapping(value = "/login/")
    public String login(Model model){
        model.addAttribute("msg","请先登录");
        return "login";
    }



    @PostMapping(value = "/login/")
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password)
    {
        log.info("1");
        List<User> userList = userMapper.getOne(username,password);
        if (userList.size() ==0)
        {
//            model.addAttribute("msg","密码或用户名不正确");
            log.info("登录失败");
            return "login";
        }
        else
        {
            List<Travel_notes> noteList = noteMapper.findAll();
            List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
            for (Travel_notes note :noteList){
                Map<String,String> map= new HashMap<>();
                map.put("id",note.getId());
                map.put("username",note.getUsername());
                map.put("textname",note.getTextname());
                if (note.getText()!=null && note.getText().length()>12)
                    map.put("text",note.getText().substring(0,11));
                else
                    map.put("text",note.getText());
                if (note.getZhaiyao()!=null && (note.getZhaiyao()).length()>12)
                    map.put("zhaiyao",note.getZhaiyao().substring(0,11));
                else
                    map.put("zhaiyao",note.getZhaiyao());
                mapList.add(map);

            }
            model.addAttribute("mapList",mapList);
            log.info("登录成功");


        }


            return "index";

    }

    @RequestMapping(value = "/register/")
    public String register(Model model){

        return "register";
    }

    @PostMapping(value = "/register/")
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password)
    {

        userMapper.insert(username,password);
        model.addAttribute("msg","注册成功");
        log.info("注册成功");
        return "login";
    }

    @RequestMapping(value = "/send_email/")
    public String send_mail(Model model){

        List<User> userList = userMapper.findAll();
        for (User user :userList){
            model.addAttribute("name",user.getUsername());
            log.info("user {} password {}",user.getUsername(),user.getPassword());
        }
        return "send_email";
    }

    @RequestMapping(value = "/detail/")
    public String detail(
            Model model,
            @RequestParam("id") String id)
    {
        log.info(id);
        noteMapper.add_view(Integer.parseInt(id));
        Travel_notes note = noteMapper.getOne(Integer.parseInt(id));
        Map<String,String> map= new HashMap<>();
        map.put("id",note.getId());
        map.put("username",note.getUsername());
        map.put("textname",note.getTextname());//.substring(0,11)
        map.put("text",note.getText());
        map.put("zhaiyao",note.getZhaiyao());
        model.addAttribute("map",map);

        List <comment> commentList = commentMapper.getOne(Integer.parseInt(id));
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (comment com :commentList){
            Map<String,String> comment_map= new HashMap<>();
            comment_map.put("username",com.getUser_name());
            comment_map.put("content",com.getContent());
            mapList.add(comment_map);
        }
        model.addAttribute("mapList",mapList);
        return "detail";
    }

    @RequestMapping(value = "/add/")
    public String add(Model model){

        return "add";
    }

    @PostMapping(value = "/add/")
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("textname") String textname,
                           @RequestParam("text") String text)
    {
        List<String> sentenceList = HanLP.extractSummary(text, 3);
        String zhaiyao = String.join("", sentenceList);
        log.info(zhaiyao);
        noteMapper.insert(username,textname,text,zhaiyao);
        log.info("添加成功");

        List<Travel_notes> noteList = noteMapper.findAll();
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (Travel_notes note :noteList){
            Map<String,String> map= new HashMap<>();
            map.put("id",note.getId());
            map.put("username",note.getUsername());
            map.put("textname",note.getTextname());
            if (note.getText()!=null && note.getText().length()>12)
                map.put("text",note.getText().substring(0,11));
            else
                map.put("text",note.getText());
            if (note.getZhaiyao()!=null && (note.getZhaiyao()).length()>12)
                map.put("zhaiyao",note.getZhaiyao().substring(0,11));
            else
                map.put("zhaiyao",note.getZhaiyao());
            mapList.add(map);

        }
        model.addAttribute("mapList",mapList);

        return "index";
    }

    @PostMapping(value = "/sort/")
    public String register(Model model,
                           @RequestParam("sort") String sort)

    {

        if (sort.equals("点赞数")) {//排序在sql查询中进行
            List<Travel_notes> noteList = noteMapper.poll_num();
            List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
            for (Travel_notes note :noteList){
                Map<String,String> map= new HashMap<>();
                map.put("id",note.getId());
                map.put("username",note.getUsername());
                map.put("textname",note.getTextname());
                if (note.getText()!=null && note.getText().length()>12)
                    map.put("text",note.getText().substring(0,11));
                else
                    map.put("text",note.getText());
                if (note.getZhaiyao()!=null && (note.getZhaiyao()).length()>12)
                    map.put("zhaiyao",note.getZhaiyao().substring(0,11));
                else
                    map.put("zhaiyao",note.getZhaiyao());
                mapList.add(map);

            }

            model.addAttribute("mapList",mapList);
            return "index";
        }
        else if (sort.equals("评论数")) {
            List<Travel_notes> noteList = noteMapper.comment_num();
            List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
            for (Travel_notes note :noteList){
                Map<String,String> map= new HashMap<>();
                map.put("id",note.getId());
                map.put("username",note.getUsername());
                map.put("textname",note.getTextname());
                if (note.getText()!=null && note.getText().length()>12)
                    map.put("text",note.getText().substring(0,11));
                else
                    map.put("text",note.getText());
                if (note.getZhaiyao()!=null && (note.getZhaiyao()).length()>12)
                    map.put("zhaiyao",note.getZhaiyao().substring(0,11));
                else
                    map.put("zhaiyao",note.getZhaiyao());
                mapList.add(map);

            }

            model.addAttribute("mapList",mapList);
            return "index";
        }
        else if (sort.equals("浏览量")) {
            List<Travel_notes> noteList = noteMapper.view_num();
            List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
            for (Travel_notes note :noteList){
                Map<String,String> map= new HashMap<>();
                map.put("id",note.getId());
                map.put("username",note.getUsername());
                map.put("textname",note.getTextname());
                if (note.getText()!=null && note.getText().length()>12)
                    map.put("text",note.getText().substring(0,11));
                else
                    map.put("text",note.getText());
                if (note.getZhaiyao()!=null && (note.getZhaiyao()).length()>12)
                    map.put("zhaiyao",note.getZhaiyao().substring(0,11));
                else
                    map.put("zhaiyao",note.getZhaiyao());
                mapList.add(map);

            }

            model.addAttribute("mapList",mapList);
            return "index";
        }


        List<Travel_notes> noteList = noteMapper.findAll();
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (Travel_notes note :noteList){
            Map<String,String> map= new HashMap<>();
            map.put("id",note.getId());
            map.put("username",note.getUsername());
            map.put("textname",note.getTextname());//.substring(0,11)
            if (note.getText().length()>11)
                map.put("text",note.getText()).substring(0,11);
            else
                map.put("text",note.getText());
            if (note.getZhaiyao().length()>11)
                map.put("zhaiyao",note.getZhaiyao()).substring(0,11);
            else
                map.put("zhaiyao",note.getZhaiyao());

            mapList.add(map);
        }
        model.addAttribute("mapList",mapList);

        return "index";
    }


    @PostMapping(value = "/search/")
    public String search(Model model,
                           @RequestParam("keyword") String keyword
                           )
    {

        List<Travel_notes> noteList = noteMapper.search(keyword);

        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (Travel_notes note :noteList){
            Map<String,String> map= new HashMap<>();
            map.put("id",note.getId());
            map.put("username",note.getUsername());
            map.put("textname",note.getTextname());
            if (note.getText()!=null && note.getText().length()>12)
                map.put("text",note.getText().substring(0,11));
            else
                map.put("text",note.getText());
            if (note.getZhaiyao()!=null && (note.getZhaiyao()).length()>12)
                map.put("zhaiyao",note.getZhaiyao().substring(0,11));
            else
                map.put("zhaiyao",note.getZhaiyao());
            mapList.add(map);

        }
        model.addAttribute("mapList",mapList);

        return "index";
    }


    @PostMapping(value = "/comment/")
    public String comment(
            Model model,
            @RequestParam("comment") String content,
            @RequestParam("id") String id)
    {
        log.info(id);
        log.info(content);
        commentMapper.insert(content);
        noteMapper.add_comment(Integer.parseInt(id));
        Travel_notes note = noteMapper.getOne(Integer.parseInt(id));
        Map<String,String> map= new HashMap<>();
        map.put("id",note.getId());
        map.put("username",note.getUsername());
        map.put("textname",note.getTextname());//.substring(0,11)
        map.put("text",note.getText());
        map.put("zhaiyao",note.getZhaiyao());
        model.addAttribute("map",map);

        List <comment> commentList = commentMapper.getOne(Integer.parseInt(id));
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (comment com :commentList){
            Map<String,String> comment_map= new HashMap<>();
            comment_map.put("username",com.getUser_name());
            comment_map.put("content",com.getContent());
            mapList.add(comment_map);
        }
        model.addAttribute("mapList",mapList);
        return "detail";
    }


    @RequestMapping(value = "/poll/")
    public String poll(
            Model model,
            @RequestParam("id") String id)
    {
        log.info("点赞成功");
        noteMapper.add_poll(Integer.parseInt(id));
        Travel_notes note = noteMapper.getOne(Integer.parseInt(id));
        Map<String,String> map= new HashMap<>();
        map.put("poll_num",note.getPoll_num());
        map.put("id",note.getId());
        map.put("username",note.getUsername());
        map.put("textname",note.getTextname());//.substring(0,11)
        map.put("text",note.getText());
        map.put("zhaiyao",note.getZhaiyao());
        model.addAttribute("map",map);

        List <comment> commentList = commentMapper.getOne(Integer.parseInt(id));
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (comment com :commentList){
            Map<String,String> comment_map= new HashMap<>();
            comment_map.put("username",com.getUser_name());
            comment_map.put("content",com.getContent());
            mapList.add(comment_map);
        }
        model.addAttribute("mapList",mapList);
        return "detail";
    }



}
