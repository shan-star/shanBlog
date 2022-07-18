package com.shan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shan.dao.mapper.TagMapper;
import com.shan.dao.pojo.Tag;
import com.shan.service.TagService;
import com.shan.vo.Result;
import com.shan.vo.TagVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    // mybatis-plus 无法进行多表查询
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        // mybatis-plus 无法进行多表查询[需要在 tagMapper 接口定义方法findTagsByArticleId、同时需要编写具体的mapper.xml]
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    // 将数据库查询的数据进行封装展示
    private TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        //解决统一缓存中的精度损失问题
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }

    private List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }


    /**
     * 最热标签
     * @param limit
     * @return
     */
    @Override
    public Result hots(int limit) {
       // 标签拥有的文章数量最多，是最热标签
       // 查询，根据tag_id 分组 计数，从小到大，排序 取前limit 个

       List<Long> tagIds = tagMapper.findHostTagIds(limit);
       //需要的是 tagId 和 tagName  Tag 对象
       // select * from tag where id in (1, 2, 3, 4) --- 注意查询得到的tagIds 集合不能为空
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        List<Tag> tagList = tagMapper.findTagsByIds(tagIds);
        return Result.success(tagList);
    }

    /**
     * 查询所有标签
     * @return
     */
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Tag::getId, Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(lambdaQueryWrapper);
        return Result.success(copyList(tags));
    }

    /**
     * 查询所有标签详情
     * @return
     */
    @Override
    public Result findAllDetail() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tags));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVo tagVo = copy(tag);
        return Result.success(tagVo);
    }
}
