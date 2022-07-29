package com.shan.service;

import com.shan.vo.Result;
import com.shan.vo.params.ArticleParam;
import com.shan.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParam
     * @return
     */
    Result listArticles(PageParams pageParam);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticles(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param id
     * @return
     */
    Result findArticleById(Long id);

    /**
     * 查询文章通过标题
     * @return
     */
    Result findArticleByName();

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);


    /**
     * 文章搜索
     * @param search
     * @return
     */
    Result searchArticle(String search);
}
