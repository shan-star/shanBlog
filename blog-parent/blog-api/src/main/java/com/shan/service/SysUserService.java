package com.shan.service;

import com.shan.dao.pojo.SysUser;
import com.shan.vo.Result;
import com.shan.vo.UserVo;

public interface SysUserService {
    /**
     * 通过id寻找用户
     * @param id
     * @return
     */
    SysUser findUserById(Long id);

    /**
     * 通过账号、密码寻找用户
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);

    /**
     * 通过token寻找用户
     * @param token
     * @return
     */
    Result getUserInfoByToken(String token);

    /**
     * 通过账号查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 根据id 查询返回用户 UserVo
     * @param id
     * @return
     */
    UserVo findUserVoById(Long id);
}
