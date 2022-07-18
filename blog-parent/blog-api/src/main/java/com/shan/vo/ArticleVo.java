package com.shan.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    //一定要记得加 要不然 会出现精度损失
//    @JsonSerialize(using = ToStringSerializer.class)
//    private Long id;
    //解决统一缓存中的精度损失问题
    private String id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

//    private String author;
    private UserVo author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private List<CategoryVo> categorys;

    private CategoryVo category;
}

