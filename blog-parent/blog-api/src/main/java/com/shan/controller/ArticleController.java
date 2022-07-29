package com.shan.controller;


import com.shan.common.aop.LogAnnotation;
import com.shan.common.cache.Cache;
import com.shan.service.ArticleService;
import com.shan.vo.Result;
import com.shan.vo.params.ArticleParam;
import com.shan.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页展示文章列表
     * @param pageParams
     * @return
     */
    @PostMapping//添加上自定义的日志注解，作用：代表此接口要使用记录日志功能
    @LogAnnotation(module = "文章",operation = "获取文章列表")
    @Cache(expire = 5 * 60 * 1000,name = "list_article")//缓存处理
    public Result listArticles(@RequestBody PageParams pageParams){
       return articleService.listArticles(pageParams);//业务层，接收前台参数，调用相应的业务方法处理参数，然后返回数据
    }

    /**
     * 最热文章
     * @return
     */
    @PostMapping("/hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")//缓存处理
    public Result hotArticles(){
        int limit = 5;
        return articleService.hotArticles(limit);
    }

    /**
     * 最新文章
     * @return
     */
    @PostMapping("/new")
    @Cache(expire = 5 * 60 * 1000,name = "new_article")//缓存处理
    public Result newArticles(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    /**
     * 查看文章详情
     * @param id
     * @return
     */
    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
        return articleService.findArticleById(id);
    }

    /**
     * 查询关于我的博客
     */
    @PostMapping("/about")
    public Result aboutMe() {
        return articleService.findArticleByName();
    }


    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }


    @PostMapping("{id}")
    public Result articleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    /**
     * 搜索文章
     * @param articleParam
     * @return
     */
    @PostMapping("search")
    public Result search(@RequestBody ArticleParam articleParam){
        //写一个搜索接口
        String search = articleParam.getSearch();
        return articleService.searchArticle(search);
    }
}
