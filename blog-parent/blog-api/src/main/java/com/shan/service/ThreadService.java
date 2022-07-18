package com.shan.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shan.dao.mapper.ArticleMapper;
import com.shan.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {


    @Async("taskExecutor")
    public void updateViewCount(ArticleMapper articleMapper, Article article){

        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(article.getViewCounts() + 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());
        //多线程，确保线程安全【例如线程A、B同时拿到相同id的文章，此时A先完成更新，则线程B，根据判断阅读数条件，无法再实现更新操作】
        queryWrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(articleUpdate,queryWrapper);
        try {
            //睡眠5秒 证明不会影响主线程的使用
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
