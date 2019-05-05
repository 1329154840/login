package com.example.login.model;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-04-26
 * Time: 21:57
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;

}
