package com.hdu.forum.controller;

import com.hdu.forum.dto.Result;
import com.hdu.forum.entity.Post;
import com.hdu.forum.entity.User;
import com.hdu.forum.service.PostService;
import com.hdu.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取所有帖子
     */
    @GetMapping("/list")
    public Result<List<Post>> getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();
            return Result.success(posts);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据分类获取帖子
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<Post>> getPostsByCategory(@PathVariable Long categoryId) {
        try {
            List<Post> posts = postService.getPostsByCategoryId(categoryId);
            return Result.success(posts);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据用户获取帖子
     */
    @GetMapping("/user/{userId}")
    public Result<List<Post>> getPostsByUser(@PathVariable Long userId) {
        try {
            List<Post> posts = postService.getPostsByUserId(userId);
            return Result.success(posts);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取帖子详情
     */
    @GetMapping("/{id}")
    public Result<Post> getPostById(@PathVariable Long id) {
        try {
            Post post = postService.getPostById(id);
            if (post == null) {
                return Result.error("帖子不存在");
            }
            return Result.success(post);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 创建帖子
     */
    @PostMapping("/create")
    public Result<Post> createPost(@RequestHeader("Authorization") String token,
                                   @RequestBody Post post) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            Post newPost = postService.createPost(
                user.getId(),
                post.getCategoryId(),
                post.getTitle(),
                post.getContent()
            );
            return Result.success(newPost);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 发布帖子（新界面，支持更多字段）
     */
    @PostMapping("/publish")
    public Result<Post> publishPost(@RequestHeader("Authorization") String token,
                                    @RequestBody Post post) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            Post newPost = postService.publishPost(user.getId(), post);
            return Result.success(newPost);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 自动保存草稿
     */
    @PostMapping("/saveDraft")
    public Result<Post> saveDraft(@RequestHeader("Authorization") String token,
                                  @RequestBody Post post) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            Post draft = postService.saveDraft(user.getId(), post);
            return Result.success(draft);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取当前用户最近的草稿
     */
    @GetMapping("/draft/latest")
    public Result<Post> getLatestDraft(@RequestHeader("Authorization") String token) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            Post draft = postService.getLatestDraft(user.getId());
            return Result.success(draft);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新帖子
     */
    @PutMapping("/update")
    public Result<String> updatePost(@RequestHeader("Authorization") String token,
                                     @RequestBody Post post) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            Post existPost = postService.getPostById(post.getId());
            if (existPost == null) {
                return Result.error("帖子不存在");
            }
            
            if (!existPost.getUserId().equals(user.getId())) {
                return Result.error(403, "无权修改此帖子");
            }
            
            postService.updatePost(post);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/{id}")
    public Result<String> deletePost(@RequestHeader("Authorization") String token,
                                     @PathVariable Long id) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            Post post = postService.getPostById(id);
            if (post == null) {
                return Result.error("帖子不存在");
            }
            
            if (!post.getUserId().equals(user.getId()) && user.getRole() != 1) {
                return Result.error(403, "无权删除此帖子");
            }
            
            postService.deletePost(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 点赞帖子
     */
    @PostMapping("/like/{id}")
    public Result<String> likePost(@RequestHeader("Authorization") String token,
                                   @PathVariable Long id) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            postService.likePost(id, user.getId());
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
