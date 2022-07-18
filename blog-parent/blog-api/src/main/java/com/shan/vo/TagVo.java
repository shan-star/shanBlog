package com.shan.vo;

import lombok.Data;

@Data
public class TagVo {

//    private Long id;
//解决统一缓存中的精度损失问题
    private String id;

    private String tagName;

    private String avatar;
}