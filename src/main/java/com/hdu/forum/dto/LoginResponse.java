package com.hdu.forum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import com.hdu.forum.entity.User;

@Data
public class LoginResponse {
    private String token;
    private User user;
}
