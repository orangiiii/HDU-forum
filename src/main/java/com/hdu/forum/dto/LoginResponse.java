package com.hdu.forum.dto;

import lombok.Data;
import com.hdu.forum.entity.User;

@Data
public class LoginResponse {
    private String token;
    private User user;
}
