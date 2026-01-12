package com.hdu.forum.service;

import com.hdu.forum.entity.Post;
import com.hdu.forum.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PostService {
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 获取所有帖子
     */
    public List<Post> getAllPosts() {
        return postMapper.findAll();
    }
    
    /**
     * 根据分类获取帖子
     */
    public List<Post> getPostsByCategoryId(Long categoryId) {
        return postMapper.findByCategoryId(categoryId);
    }
    
    /**
     * 根据用户获取帖子
     */
    public List<Post> getPostsByUserId(Long userId) {
        return postMapper.findByUserId(userId);
    }
    
    /**
     * 根据ID获取帖子详情
     */
    public Post getPostById(Long id) {
        // 增加浏览量
        postMapper.incrementViewCount(id);
        return postMapper.findById(id);
    }
    
    /**
     * 创建帖子
     */
    public Post createPost(Long userId, Long categoryId, String title, String content) {
        Post post = new Post();
        post.setUserId(userId);
        post.setCategoryId(categoryId);
        post.setTitle(title);
        post.setContent(content);
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setIsTop(0);
        post.setStatus(1); // 已发布
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        
        postMapper.insert(post);
        return post;
    }
    
    /**
     * 更新帖子
     */
    public void updatePost(Post post) {
        post.setUpdateTime(LocalDateTime.now());
        postMapper.update(post);
    }
    
    /**
     * 删除帖子
     */
    public void deletePost(Long id) {
        postMapper.deleteById(id);
    }
    
    /**
     * 点赞帖子
     */
    public void likePost(Long postId, Long userId) {
        String key = "post:like:" + postId + ":" + userId;
        
        // 检查是否已点赞
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new RuntimeException("已经点赞过了");
        }
        
        // 增加点赞数
        postMapper.incrementLikeCount(postId);
        
        // 记录点赞状态，有效期7天
        redisTemplate.opsForValue().set(key, true, 7, TimeUnit.DAYS);
    }
}
