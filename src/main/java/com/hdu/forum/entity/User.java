package com.hdu.forum.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Setter
@Getter
public class User implements Serializable{
    private Long id;
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String avatar;
    private Integer defaultGradYear; // 默认届数
    private Integer role; // 0:普通用户 1:管理员
    private Integer status; // 0:禁用 1:正常
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
