package com.shan.service;

import com.shan.dao.pojo.SysUser;
import com.shan.vo.Result;
import com.shan.vo.params.LoginParam;

public interface LoginService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 登录 校验token合法性
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
