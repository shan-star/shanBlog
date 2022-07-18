package com.shan.vo;

import lombok.Data;

@Data
public class UserVo {

    private String nickname;

    private String avatar;

//    private Long id;
    //解决统一缓存中的精度损失问题
    private String id;
}