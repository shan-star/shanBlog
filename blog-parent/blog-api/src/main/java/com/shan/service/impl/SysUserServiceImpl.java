package com.shan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shan.dao.mapper.SysUserMapper;
import com.shan.dao.pojo.SysUser;
import com.shan.service.LoginService;
import com.shan.service.SysUserService;
import com.shan.vo.ErrorCode;
import com.shan.vo.LoginUserVo;
import com.shan.vo.Result;
import com.shan.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;
    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("shan");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);//在用户模块这里加密 password 不好，应该在密码模块加密
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }


    /** 1．token合法性校验
     *     是否为空，解析是否成功 redis是否存在
     *  2．如果校验失败 返回错误
     *  3．如果成功，返回对应的结果 LoginUserVo
     * @param token
     * @return
     */
    @Override
    public Result getUserInfoByToken(String token) {
//        //token是否为空
//        if(StringUtils.isBlank(token)){
//            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
//        }
//      //token 校验
        SysUser sysUser = loginService.checkToken(token);
        if(sysUser == null){
             return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        //如果成功，返回对应的结果 LoginUserVo
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        //解决统一缓存中的精度损失问题
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setNickname(sysUser.getNickname());
        System.out.println("loginUserVo:" + loginUserVo);
        return Result.success(loginUserVo);
    }

    /**
     * 根据用户账号查找用户
     * @param account
     * @return
     */
    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 保存用户
     * @param sysUser
     */
    @Override
    public void save(SysUser sysUser) {
        //保存用户的id会自动生成，默认是使用分布式的id（雪花算法）---mybatis-plus
        sysUserMapper.insert(sysUser);
    }


    /**
     * 根据id 查询返回用户 UserVo
     * @param id
     * @return
     */
    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("shan");
        }
        UserVo userVo = new UserVo();
        //解决统一缓存中的精度损失问题
        userVo.setId(String.valueOf(id));
        BeanUtils.copyProperties(sysUser, userVo);
        return userVo;
    }
}
