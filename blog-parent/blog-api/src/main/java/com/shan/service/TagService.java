package com.shan.service;

import com.shan.vo.Result;
import com.shan.vo.TagVo;

import java.util.List;

public interface TagService {
    /**
     * 寻找文章的标签列表
     * @param articleId
     * @return
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    /**
     * 最热标签
     * @param limit
     * @return
     */
    Result hots(int limit);

    /**
     * 查询所有标签
     * @return
     */
    Result findAll();


    /**
     * 查询所有标签详情
     * @return
     */
    Result findAllDetail();

    /**
     * 查询标签详情
     * @param id
     * @return
     */
    Result findDetailById(Long id);
}
