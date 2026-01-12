package com.hdu.forum.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Category {
    private Long id;
    private String name;
    private String description;
    private Integer sort;
    private Integer status; // 0:禁用 1:正常
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
