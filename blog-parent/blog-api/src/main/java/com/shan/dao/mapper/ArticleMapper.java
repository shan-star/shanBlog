package com.shan.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shan.dao.dos.Archives;
import com.shan.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 文章归档
     * @return
     */
    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);
}
