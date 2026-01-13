package com.hdu.forum.controller;

import com.hdu.forum.dto.LoginRequest;
import com.hdu.forum.dto.RegisterRequest;
import com.hdu.forum.dto.Result;
import com.hdu.forum.entity.User;
import com.hdu.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;import jakarta.annotation.PostConstruct;



@RestController
@RequestMapping("/ping")
public class PingController {

    @GetMapping
    public String ping() {
        return "pong";
    }
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void checkModules() {
        System.out.println("     ===================       ");
    
        System.out.println(objectMapper.getRegisteredModuleIds());
    }
}
