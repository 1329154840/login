package com.example.login.controller;
import com.example.login.controller.read_file;
import com.example.login.dao.*;
import com.example.login.model.*;
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
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import java.io.*;

import java.util.*;
import java.io.File;
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
    private CommentMapper commentMapper;
    @Autowired
    private LabelMapper labelMapper;

    @RequestMapping(value = "/index")
    public String main(Model model,HttpServletRequest request){
        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }
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
                        HttpServletRequest request,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password)
    {
        log.info("1");
        List<User> userList = userMapper.getOne(username,password);
        if (userList.size() ==0)
        {
            model.addAttribute("msg","密码或用户名不正确");
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

            HttpSession session=request.getSession(true);//这就是session的创建
            session.setAttribute("username",username);//给session添加属性属性name： username,属性 value：TOM
            session.setAttribute("password",password);//添加属性 name: password; value: tommmm


        }


        return "redirect:/index/";

    }

    @RequestMapping(value = "/register/")
    public String register(Model model){

        return "register";
    }

    @RequestMapping(value = "/logout/")
    public String logout(Model model,HttpSession session){
        if (session != null) {
            session.removeAttribute("username");
            session.removeAttribute("password");
        }
        return  "redirect:/index/";
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

//    @RequestMapping(value = "/send_email/")
//    public String send_mail(Model model){
//
//        List<User> userList = userMapper.findAll();
//        for (User user :userList){
//            model.addAttribute("name",user.getUsername());
//            log.info("user {} password {}",user.getUsername(),user.getPassword());
//        }
//        return "send_email";
//    }

    @RequestMapping(value = "/detail/")
    public String detail(
            Model model,
            HttpServletRequest request,
            @RequestParam("id") String id)
    {
        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }
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
    public String add(Model model,HttpServletRequest request){
        List<Label> labelList = labelMapper.findAll();
        model.addAttribute("labelList", labelList);
        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }
        return "add";
    }

    @PostMapping(value = "/add/")
    public String register(Model model,
                           HttpServletRequest request,
                           @RequestParam("file") MultipartFile file,
                           @RequestParam("username") String username,
                           @RequestParam("textname") String textname,
                           @RequestParam("labels") String labels,
                           @RequestParam("text") String text_
                            )
    {

        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }

        String text = new String();

        if (!file.isEmpty()) {



            String fileName = file.getOriginalFilename();
            String filePath = "C:/Users/Administrator/Desktop/";//绝对路径，相对路径不知道怎么弄
            File dest = new File(filePath + fileName);
            log.info(dest.toString());
            try {
                file.transferTo(dest);
                log.info("上传成功");

            } catch (IOException e) {
                log.info("上传失败");
            }



            String pathname = filePath + fileName;
            read_file a = new read_file();
            String content = a.readTxt(pathname);
            text = content;
//            try (
//                    InputStreamReader isr = new InputStreamReader(new FileInputStream(pathname), "utf-8");
//                    BufferedReader br = new BufferedReader(isr) // 建立一个对象，它把文件内容转成计算机能读懂的语言
//            ) {
//                String line;
//                String message = "";
//                //网友推荐更加简洁的写法
//                while ((line = br.readLine()) != null) {
//                    message += line;
//                    log.info(line);
//                }
//                text = message;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }



        }
        else
            text = text_;

        List<String> sentenceList = HanLP.extractSummary(text, 3);
        String zhaiyao = String.join("", sentenceList);
        log.info(zhaiyao);
        noteMapper.insert(username,textname,text,zhaiyao,labels);
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
                           HttpServletRequest request,
                           @RequestParam("sort") String sort)

    {
        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }

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
                           HttpServletRequest request,
                           @RequestParam("keyword") String keyword
                           )
    {
        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }
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
            HttpServletRequest request,
            @RequestParam("comment") String content,
            @RequestParam("id") String id)
    {
        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }

        log.info(id);
        log.info(content);
        commentMapper.insert(Integer.parseInt(id),content,login_user);
        noteMapper.add_comment(Integer.parseInt(id));
        log.info("添加评论成功");
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
            log.info(com.getUser_name());
            comment_map.put("content",com.getContent());
            mapList.add(comment_map);
        }
        model.addAttribute("mapList",mapList);
        return "detail";
    }


    @RequestMapping(value = "/poll/")
    public String poll(
            Model model,
            HttpServletRequest request,
            @RequestParam("id") String id)
    {

        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }

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

    @RequestMapping(value = "/my_note/")
    public String my_note(Model model,HttpServletRequest request){
        String login_user=null;
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }

        List<Travel_notes> noteList = noteMapper.getMy_note(login_user);
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
        return "my_note";
    }


    @RequestMapping(value = "/delete/")
    public String my_note(Model model,
                          HttpServletRequest request,
                          @RequestParam("id") String id){
        noteMapper.delete(Integer.parseInt(id));
        return "redirect:/my_note/";
    }

    @RequestMapping(value = "/add_label/")
    public String add_label(Model model,HttpServletRequest request){

        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }
        return "add_label";
    }

    @PostMapping(value = "/add_label/")
    public String add_labber(Model model,
                           HttpServletRequest request,
                           @RequestParam("name") String name
                           )
    {

        String login_user="未登录游客";
        HttpSession session=request.getSession(false);
        Object obj = null;
        if (session!=null)
        {
            if (session.getAttribute("username")!=null) {
                obj = session.getAttribute("username");
                log.info(obj.toString());
            }
            if (obj!=null) {
                login_user = obj.toString();
                model.addAttribute("login_user", login_user);
            }
        }

        labelMapper.insert(name);
        log.info("添加成功");
        return "redirect:/add/";

    }


}



