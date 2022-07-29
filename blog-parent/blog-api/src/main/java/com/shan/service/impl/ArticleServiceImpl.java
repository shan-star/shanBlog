package com.shan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shan.dao.dos.Archives;
import com.shan.dao.mapper.ArticleBodyMapper;
import com.shan.dao.mapper.ArticleMapper;
import com.shan.dao.mapper.ArticleTagMapper;
import com.shan.dao.pojo.Article;
import com.shan.dao.pojo.ArticleBody;
import com.shan.dao.pojo.ArticleTag;
import com.shan.dao.pojo.SysUser;
import com.shan.service.*;
import com.shan.utils.UserThreadLocal;
import com.shan.vo.*;
import com.shan.vo.params.ArticleParam;
import com.shan.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Result listArticles(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());
//        return Result.success(copyList(articleIPage.getRecords(), true, true));
        List<Article> records = articleIPage.getRecords();
        for (Article record : records) {
            String viewCount = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(record.getId()));
            if (viewCount != null){
                record.setViewCounts(Integer.parseInt(viewCount));
            }
        }
        return Result.success(copyList(records,true,true));
    }


//    @Override
//    public Result listArticles(PageParams pageParams) {
//        /**
//         * 分页查询 article 数据库表
//         *  <E extends IPage<T>> E selectPage(E page, @Param("ew") Wrapper<T> queryWrapper);
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//
//        //查询文章的参数 加上分类id，判断不为空 加上分类条件
//        if (pageParams.getCategoryId() != null) {
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        /**
//         *  加入标签 条件查询
//         *  article表中 并没有tag字段
//         * 一篇文章 有多个标签 //article_tag表中 article_id 1 : n tag_id
//         * 需要先将tag_id 所对应的所有 article_id，查询出来，然后再到article表查询文章对象集合
//         * 【使用articleTagMapper查询到对应的articleTags】
//         */
//        List<Long> articleIdList = new ArrayList<>();
//
//        if (pageParams.getTagId() != null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0){
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//
//
//        //是否置顶进行排序、根据创建时间逆序排序
//        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//
//        List<Article> records = articlePage.getRecords();
//        // 查询得到的文章列表articleList可以直接返回吗？
//        // --不能，需要处理一下展示的数据，选择哪些数据需要展示，哪些数据不展示，
//        // 哪些数据进行需要进行关联表查询才展示,比如在article表查询的列时author_id,展示效果不能是id数字值，应该是author，需要关联查询一下
//        // 对数据库表article中查询得到用于展示的数据进行封装ArticleVo
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//        return Result.success(articleVoList);
//    }


    /**
     * 将List<Article> 转成 List<ArticleVo>
     *
     * @param records
     * @param isTag
     * @param isAuthor
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor));
        }
        return articleVoList;
    }

    /**
     * 将Article 转成 ArticleVo 不是所有的接口都需要标签、作者信息
     *
     * @param article
     * @param isTag
     * @param isAuthor
     * @return
     */
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        //解决统一缓存中的精度损失问题,修改id类型long为String类型
        articleVo.setId(String.valueOf(article.getId()));

        BeanUtils.copyProperties(article, articleVo);
        // 时间
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 不是所有的接口都需要标签、作者信息
        if (isTag) {
            List<TagVo> tags = tagService.findTagsByArticleId(article.getId());
            articleVo.setTags(tags);
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
//            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
            SysUser sysUser = sysUserService.findUserById(authorId);
            UserVo userVo = new UserVo();
            userVo.setAvatar(sysUser.getAvatar());
            userVo.setId(sysUser.getId().toString());
            userVo.setNickname(sysUser.getNickname());
            articleVo.setAuthor(userVo);
        }


        return articleVo;
    }

    /**
     * 最热文章
     *
     * @param limit
     * @return
     */
    @Override
    public Result hotArticles(int limit) {
        // select id,title from article order by view_counts limit 5
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }


    /**
     * 最新文章
     *
     * @param limit
     * @return
     */
    @Override
    public Result newArticles(int limit) {
        // select id,title from article order by view_counts limit 5
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    /**
     * 文章归档
     *
     * @return
     */
    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    /**
     * 查看文章详情
     *
     * @param id
     * @return
     */
    @Override
    public Result findArticleById(Long id) {
        /**
         * 1、根据id 查询文章信息--title、摘要、bodyId、categoryId
         * 2、根据bodyId、categoryId 关联查询
         * 3、查询得到 article，想要给页面展示的是articleVo
         */
        Article article = articleMapper.selectById(id);
        //需要重载copy方法，扩展添加上是否需要文章内容、文章分类
        ArticleVo articleVo = copy(article, true, true, true, true);
        /**
         * 查看完文章了，新增阅读数，有没有问题呢？
         * 查看完文章之后，本应该直接返回数据了，
         * 这时候做了一个更新操作，更新时加写锁，阳塞其他的读操作，性能就会比较低
         * 更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响 查看文章的操作
         * 线程池 可以把更新操作 扔到线程池中去执行，和主线程就不相关了
         */
        //通过多线程，异步更新文章的阅读数
        threadService.updateViewCount(articleMapper, article);
        String viewCount = (String) redisTemplate.opsForHash().get("view_count", String.valueOf(id));
        if (viewCount != null){
            articleVo.setViewCounts(Integer.parseInt(viewCount));
        }
        return Result.success(articleVo);
    }

    @Override
    public Result findArticleByName() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getTitle, "关于我");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        ArticleVo articleVo = copy(articles.get(0), false, false, true, false);
        return Result.success(articleVo);
    }

    /**
     * 复制属性，并判断是否需要标签、作者、文章内容、文章分类
     */
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        //解决统一缓存中的精度损失问题,修改id类型long为String类型
        articleVo.setId(String.valueOf(article.getId()));
        //时间
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (article != null) {
            BeanUtils.copyProperties(article, articleVo);
        }
        if (isTag) {
            //根据标签id集合查找返回标签集合
            List<TagVo> tags = tagService.findTagsByArticleId(article.getId());
            articleVo.setTags(tags);
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
//            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
            SysUser sysUser = sysUserService.findUserById(authorId);
            UserVo userVo = new UserVo();
            userVo.setAvatar(sysUser.getAvatar());
            userVo.setId(sysUser.getId().toString());
            userVo.setNickname(sysUser.getNickname());
            articleVo.setAuthor(userVo);
        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBody(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }


    /**
     * 查找文章内容
     *
     * @param bodyId
     * @return
     */
    private ArticleBodyVo findArticleBody(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    /**
     * 发布文章
     *
     * @param articleParam
     * @return
     */
//    @Override
//    public Result publish(ArticleParam articleParam) {
//        /**
//         * 1、发布文章：构建文章对象【给文本表添加上文章对象】
//         * 2、作者id ：当前的登录用户，通过ThreadLocal 获取
//         * [因为threadLocal是在登录拦截器放行后添加上用户信息的]，
//         * 则需要将发布文章的路径加入登录拦截器
//         * 3、标签：标签id 和 文章id的对应，在[文章-标签表] 添加一个关联记录【给文章-标签表添加上文章-标签对象】
//         * 4、body 内容存储【给内容表添加上内容对象】，且给文章表添加上的bodyId，更新一下数据库的文章记录
//         */
//        Article article = new Article();
//        // 作者id
//        SysUser sysUser = UserThreadLocal.get();
//        article.setAuthorId(sysUser.getId());
//        //解决统一缓存中的精度损失问题
//        article.setCategoryId(Long.valueOf(articleParam.getCategory().getId()));
//        article.setCreateDate(new Date(System.currentTimeMillis()));
//        article.setCommentCounts(0);
//        article.setSummary(articleParam.getSummary());
//        article.setTitle(articleParam.getTitle());
//        article.setViewCounts(0);
//        article.setWeight(Article.Article_Common);
//        article.setBodyId(-1L);
//
//        //给【文章表】添加上文章对象
//        articleMapper.insert(article);
//        //---------------- 关联表之间的添加 ------------------
//        //给【文章-标签表】添加上文章-标签对象
//        List<TagVo> tags = articleParam.getTags();
//        if(tags != null){
//            for(TagVo tag: tags){
//                ArticleTag articleTag = new ArticleTag();
//                articleTag.setArticleId(article.getId());
//                //解决统一缓存中的精度损失问题
//                articleTag.setTagId(Long.valueOf(tag.getId()));
//                articleTagMapper.insert(articleTag);
//            }
//        }
//        //给【文章内容表】添加上文章内容对象
//        ArticleBody articleBody = new ArticleBody();
//        articleBody.setContent(articleParam.getBody().getContent());
//        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
//        articleBody.setArticleId(article.getId());
//        articleBodyMapper.insert(articleBody);
//        //给文章表添加上的bodyId
//        article.setBodyId(articleBody.getId());
//        //更新一下数据库的文章记录
//        articleMapper.updateById(article);
//
//        /**
//         * 方式1：返回 ArticleVo，则需要考虑分布式id 的精度损失问题--在对应的ArticleVo的字段上添加上标签@JsonSerialize
//         * ArticleVo articleVo = new ArticleVo();
//         * articleVo.setId(article.getId());
//         *
//         * 方式2：根据前端接口返回的数据格式，定义成map结构返回，并且value是字符串
//         */
//        Map<String, String> map = new HashMap<>();
//        map.put("id", article.getId().toString());
//        return Result.success(map);
//    }


    @Override
    public Result publish(ArticleParam articleParam) {
        //此接口 要加入到登录拦截当中
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        Article article = new Article();
        boolean isEdit = false;
        if (articleParam.getId() != null) {
            article = new Article();
            article.setId(articleParam.getId());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            articleMapper.updateById(article);
            isEdit = true;
        } else {
            article = new Article();
            article.setAuthorId(sysUser.getId());
            article.setWeight(Article.Article_Common);
            article.setViewCounts(0);
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCommentCounts(0);
            article.setCreateDate(System.currentTimeMillis());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            //插入之后 会生成一个文章id
            this.articleMapper.insert(article);
        }
        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                if (isEdit) {
                    //先删除
                    LambdaQueryWrapper<ArticleTag> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(ArticleTag::getArticleId, articleId);
                    articleTagMapper.delete(queryWrapper);
                }
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        if (isEdit) {
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            LambdaUpdateWrapper<ArticleBody> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(ArticleBody::getArticleId, article.getId());
            articleBodyMapper.update(articleBody, updateWrapper);
        } else {
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            articleBodyMapper.insert(articleBody);

            article.setBodyId(articleBody.getId());
            articleMapper.updateById(article);
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", article.getId().toString());

        if (isEdit) {
            //发送一条消息给rocketmq 当前文章更新了，更新一下缓存吧
            ArticleMessage articleMessage = new ArticleMessage();
            articleMessage.setArticleId(article.getId());
//            rocketMQTemplate.convertAndSend("blog-update-article",articleMessage);
        }
        return Result.success(map);
    }

    @Override
    public Result searchArticle(String search) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.like(Article::getTitle,search);
        //select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

}


