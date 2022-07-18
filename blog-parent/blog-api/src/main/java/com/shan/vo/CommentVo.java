package com.shan.vo;


import lombok.Data;

import java.util.List;

@Data
public class CommentVo  {
//    //防止前端精度的损失，把id转化陈string
//    @JsonSerialize(using = ToStringSerializer.class)
//    private Long id;

    //解决统一缓存中的精度损失问题
    private String id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}