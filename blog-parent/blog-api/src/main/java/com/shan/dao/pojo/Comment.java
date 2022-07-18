package com.shan.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Comment {

    private Long id;

    private String content;

    private Long createDate;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    private Long authorId;

    private Long parentId;//实现对评论评论【评论的评论】

    private Long toUid;

    private Integer level;
}