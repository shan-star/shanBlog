package com.shan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shan.dao.mapper.CategoryMapper;
import com.shan.dao.pojo.Category;
import com.shan.service.CategoryService;
import com.shan.vo.CategoryVo;
import com.shan.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 根据id查找到分类
     * @param id
     * @return
     */
    @Override
    public CategoryVo findCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = new CategoryVo();
        //解决统一缓存中的精度损失问题
        categoryVo.setId(String.valueOf(id));
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    /**
     * 查询所有分类
     * @return
     */
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Category::getId, Category::getCategoryName);
        List<Category> categories = this.categoryMapper.selectList(lambdaQueryWrapper);
        // 和页面交互的对象Vo
        List<CategoryVo> categoryVoList = copyList(categories);
        return Result.success(categoryVoList);
    }

    /**
     * 属性复制
     * @param categories
     * @return
     */
    private List<CategoryVo> copyList(List<Category> categories) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categories) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        //解决统一缓存中的精度损失问题
        categoryVo.setId(String.valueOf(category.getId()));
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    /**
     * 查询所有分类详情
     * @return
     */
    @Override
    public Result findAllDetail() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        //页面交互的对象
        return Result.success(copyList(categories));
    }

    /**
     * 查询分类详情
     * @return
     */
    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = copy(category);
        return Result.success(categoryVo);
    }
}
