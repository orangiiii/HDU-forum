package com.hdu.forum.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId; // 父评论ID，如果是顶级评论则为0
    private String content;
    private Integer likeCount;
    private Integer status; // 0:已删除 1:正常
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联查询字段
    private String username;
    private String parentUsername;
}
