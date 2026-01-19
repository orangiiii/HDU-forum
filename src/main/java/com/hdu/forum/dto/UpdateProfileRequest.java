package com.hdu.forum.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String avatar;
    private Integer defaultGradYear;
}
