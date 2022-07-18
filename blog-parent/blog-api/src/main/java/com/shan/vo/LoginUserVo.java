package com.shan.vo;

import lombok.Data;

@Data
public class LoginUserVo {
//    private Long id;

    //解决统一缓存中的精度损失问题
    private String id;

    private String account;

    private String nickname;

    private String avatar;
}
