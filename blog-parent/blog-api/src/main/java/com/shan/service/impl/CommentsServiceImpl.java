package com.shan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shan.dao.mapper.CommentMapper;
import com.shan.dao.pojo.Comment;
import com.shan.dao.pojo.SysUser;
import com.shan.service.CommentsService;
import com.shan.service.SysUserService;
import com.shan.utils.UserThreadLocal;
import com.shan.vo.CommentVo;
import com.shan.vo.Result;
import com.shan.vo.UserVo;
import com.shan.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    /**
     * 根据文章id，查询评论列表
     * 1．根据文章id 查询 评论列表 从 comment 表中查询
     * 2．根据作者的id 查询作者的信息
     * 3．判断 如果 level＝1 要去查询它有没有子评论
     * 4．如果有 根据评论id 进行查询（parent＿id）
     * @param articleId
     * @return
     */
    @Override
    public Result commentsByArticleId(Long articleId) {
        //从数据库查询到对应文章id的所有的评论集合
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getLevel, 1);//从评论一级开始
        //查询获得评论集合，需要封装成vo才能展示到页面
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        /**
         * 封装成vo,与前端传递过来的接口中的json进行对应
         */
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    /**
     * 将comments 封装成 commentVoList
     * 2．根据作者的id 查询作者的信息
     * 3．判断 如果 level＝1 要去查询它有没有子评论
     * 4．如果有 根据评论id 进行查询（parent＿id）
     * @param comments
     * @return
     */
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //解决统一缓存中的精度损失问题
        commentVo.setId(String.valueOf(comment.getId()));
        //作者信息
        UserVo userVo = sysUserService.findUserVoById(comment.getAuthorId());
        commentVo.setAuthor(userVo);
        //子评论
        //评论等级，一级评论才可以有子评论
        Integer level = comment.getLevel();
        if(level == 1){
            List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
            commentVo.setChildrens(commentVoList);
        }
        //给谁评论，要求评论层数level 大于1，才可以回复
        if (comment.getLevel() > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    /**
     * 通过父评论id或者到其所有子评论[只实现了两层结构，多层结构，需要递归，或者迭代]
     * @param id
     * @return
     */
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel, 2);
        List<Comment> comments = this.commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }

    /**
     * 写评论
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        CommentVo commontVo = copy(comment);
        return Result.success(commontVo);
    }
}
