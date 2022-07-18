package com.shan.service;

import com.shan.vo.CategoryVo;
import com.shan.vo.Result;

public interface CategoryService {
    /**
     * 根据id查找到分类
     * @param id
     * @return
     */
    CategoryVo findCategoryById(Long id);

    /**
     * 查询所有分类
     * @return
     */
    Result findAll();

    /**
     * 查询所有分类详情
     * @return
     */
    Result findAllDetail();

    /**
     * 查询分类详情
     * @return
     */
    Result categoriesDetailById(Long id);
}
