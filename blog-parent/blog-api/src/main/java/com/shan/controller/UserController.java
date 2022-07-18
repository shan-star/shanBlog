package com.shan.controller;

import com.shan.service.SysUserService;
import com.shan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.getUserInfoByToken(token);//通过token获取用户信息
    }
}