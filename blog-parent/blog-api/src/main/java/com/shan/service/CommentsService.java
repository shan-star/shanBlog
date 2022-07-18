package com.shan.service;

import com.shan.vo.Result;
import com.shan.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 根据文章id，查询所有的评论列表
     * @param articleId
     * @return
     */
    Result commentsByArticleId(Long articleId);

    /**
     * 评论
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
