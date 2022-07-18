package com.shan.handler;

import com.alibaba.fastjson.JSON;
import com.shan.dao.pojo.SysUser;
import com.shan.service.LoginService;
import com.shan.utils.UserThreadLocal;
import com.shan.vo.ErrorCode;
import com.shan.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器LoginInterceptor
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;
    /**
     * handler方法在springmvc是controller方法
     * 1．需要判断请求的接口路径是否为 HandlerMethod（controller方法），目的是为了放行静态资源
     * 2．判断 token是否为空，如果为空 未登录
     * 3．如果token 不为空，登录验证 loginService checkToken
     * 4．如果认证成功 放行即可
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断请求接口路径是否controller方法，不是则可能是静态资源，直接放行
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        //判断token是否为空
        String token = request.getHeader("Authorization");

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (token == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            //将未登录信息提示到页面
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //校验token的合法性
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        //登录验证成功，放行
        //我希望在controller中 直接获取用户的信息 怎么获取?
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //删除ThreadLocal中的用户信息,用完即删除，否则会有内存泄露的风险。
        UserThreadLocal.remove();
    }
}
