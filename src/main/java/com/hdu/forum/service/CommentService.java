package com.hdu.forum.service;

import com.hdu.forum.entity.Comment;
import com.hdu.forum.mapper.CommentMapper;
import com.hdu.forum.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 根据帖子ID获取评论列表
     */
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentMapper.findByPostId(postId);
    }
    
    /**
     * 根据用户ID获取评论列表
     */
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentMapper.findByUserId(userId);
    }
    
    /**
     * 创建评论
     */
    public Comment createComment(Long postId, Long userId, Long parentId, String content) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setParentId(parentId != null ? parentId : 0L);
        comment.setContent(content);
        comment.setLikeCount(0);
        comment.setStatus(1);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        
        commentMapper.insert(comment);
        
        // 增加帖子的评论数
        postMapper.incrementCommentCount(postId);
        
        return comment;
    }
    
    /**
     * 删除评论
     */
    public void deleteComment(Long id) {
        commentMapper.deleteById(id);
    }
    
    /**
     * 点赞评论
     */
    public void likeComment(Long commentId, Long userId) {
        String key = "comment:like:" + commentId + ":" + userId;
        
        // 检查是否已点赞
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new RuntimeException("已经点赞过了");
        }
        
        // 增加点赞数
        commentMapper.incrementLikeCount(commentId);
        
        // 记录点赞状态，有效期7天
        redisTemplate.opsForValue().set(key, true, 7, TimeUnit.DAYS);
    }
}
