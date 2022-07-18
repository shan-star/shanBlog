package com.shan.controller;

import com.shan.service.LoginService;
import com.shan.vo.Result;
import com.shan.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    //这里选择不注入sysUserService，理由是职责专一性考虑，让用户只负责用户相关的，
    //loginService 负责 登录相关的
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }
}
