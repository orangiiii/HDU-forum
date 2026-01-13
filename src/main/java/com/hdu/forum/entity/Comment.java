package com.hdu.forum.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    // 关联查询字段
    private String username;
    private String parentUsername;
}
