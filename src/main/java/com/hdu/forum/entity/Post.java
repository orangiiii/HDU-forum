package com.hdu.forum.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Post {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer isTop; // 0:否 1:是
    private Integer status; // 0:草稿 1:已发布 2:已删除
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联查询字段
    private String username;
    private String categoryName;
}
