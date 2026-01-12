package com.hdu.forum.controller;

import com.hdu.forum.dto.Result;
import com.hdu.forum.entity.Comment;
import com.hdu.forum.entity.User;
import com.hdu.forum.service.CommentService;
import com.hdu.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 根据帖子ID获取评论列表
     */
    @GetMapping("/post/{postId}")
    public Result<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据用户ID获取评论列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Comment>> getCommentsByUserId(@PathVariable Long userId) {
        try {
            List<Comment> comments = commentService.getCommentsByUserId(userId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 创建评论
     */
    @PostMapping("/create")
    public Result<Comment> createComment(@RequestHeader("Authorization") String token,
                                        @RequestBody Comment comment) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            Comment newComment = commentService.createComment(
                comment.getPostId(),
                user.getId(),
                comment.getParentId(),
                comment.getContent()
            );
            return Result.success(newComment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteComment(@RequestHeader("Authorization") String token,
                                       @PathVariable Long id) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            commentService.deleteComment(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 点赞评论
     */
    @PostMapping("/like/{id}")
    public Result<String> likeComment(@RequestHeader("Authorization") String token,
                                     @PathVariable Long id) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            commentService.likeComment(id, user.getId());
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
