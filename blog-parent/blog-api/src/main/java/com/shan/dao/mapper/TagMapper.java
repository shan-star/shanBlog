package com.shan.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shan.dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章id，查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 最热标签 前面limit 条,
     * 因为是在文章-标签表进行分组排序查询，查询结果是tagIds集合
     * @param limit
     * @return
     */
    List<Long> findHostTagIds(int limit);

    /**
     * 将 tagIds集合 转化成 tag对象的集合
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByIds(List<Long> tagIds);
}
