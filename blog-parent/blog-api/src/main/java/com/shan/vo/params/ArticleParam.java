package com.shan.vo.params;

import com.shan.vo.CategoryVo;
import com.shan.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;


    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

    //搜索参数
    private String search;
}
