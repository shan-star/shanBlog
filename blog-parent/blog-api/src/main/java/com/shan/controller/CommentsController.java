package com.shan.controller;

import com.shan.service.CommentsService;
import com.shan.vo.Result;
import com.shan.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("/article/{id}")
    public Result comments(@PathVariable("id") Long articleId){
        return commentsService.commentsByArticleId(articleId);

    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}
