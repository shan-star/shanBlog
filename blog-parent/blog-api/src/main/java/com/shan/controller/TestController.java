package com.shan.controller;

import com.shan.dao.pojo.SysUser;
import com.shan.utils.UserThreadLocal;
import com.shan.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping
    public Result test(){
        //从ThreadLocal中获取用户信息
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}