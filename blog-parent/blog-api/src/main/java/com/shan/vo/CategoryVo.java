package com.shan.vo;


import lombok.Data;

@Data
public class CategoryVo {

//    private Long id;

    //解决统一缓存中的精度损失问题
    private String id;

    private String avatar;

    private String categoryName;

    private String description;
}