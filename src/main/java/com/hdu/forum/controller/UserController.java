package com.hdu.forum.controller;

import com.hdu.forum.dto.ChangePasswordRequest;
import com.hdu.forum.dto.LoginRequest;
import com.hdu.forum.dto.RegisterRequest;
import com.hdu.forum.dto.Result;
import com.hdu.forum.dto.UpdateProfileRequest;
import com.hdu.forum.entity.User;
import com.hdu.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@Validated @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(
                request.getUsername(),
                request.getPassword()
            );
            return Result.success(user);
        } catch (Exception e) {
            e.printStackTrace(); // 打印完整堆栈
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 测试接口 - 直接返回User对象
     */
    @GetMapping("/test/{username}")
    public Result<User> testUser(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            System.out.println("===== 测试用户序列化 =====");
            System.out.println("用户: " + user);
            System.out.println("创建时间类型: " + user.getCreateTime().getClass().getName());
            System.out.println("========================");
            return Result.success(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginRequest request) {
        try {
            String token = userService.login(request.getUsername(), request.getPassword());
            User user = userService.getUserByToken(token);
            
            // 添加日志输出
            System.out.println("===== 登录成功 =====");
            System.out.println("用户名: " + user.getUsername());
            System.out.println("创建时间: " + user.getCreateTime());
            System.out.println("更新时间: " + user.getUpdateTime());
            System.out.println("==================");
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace(); // 打印完整堆栈
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            User user = userService.getUserByToken(token);
            if (user == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            // 隐藏密码
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<String> updateUser(@RequestHeader("Authorization") String token,
                                     @RequestBody User updateUser) {
        try {
            User currentUser = userService.getUserByToken(token);
            if (currentUser == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            // 只允许更新部分字段
            // currentUser.setNickname(updateUser.getNickname());
            // currentUser.setEmail(updateUser.getEmail());
            currentUser.setAvatar(updateUser.getAvatar());
            
            userService.updateUser(currentUser);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestHeader("Authorization") String token,
                                         @RequestBody ChangePasswordRequest request) {
        try {
            User currentUser = userService.getUserByToken(token);
            if (currentUser == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            userService.changePassword(currentUser.getId(), 
                                      request.getOldPassword(), 
                                      request.getNewPassword());
            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新个人信息
     */
    @PutMapping("/profile")
    public Result<User> updateProfile(@RequestHeader("Authorization") String token,
                                      @RequestBody UpdateProfileRequest request) {
        try {
            User currentUser = userService.getUserByToken(token);
            if (currentUser == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            
            // 更新字段
            if (request.getAvatar() != null) {
                currentUser.setAvatar(request.getAvatar());
            }
            if (request.getDefaultGradYear() != null) {
                currentUser.setDefaultGradYear(request.getDefaultGradYear());
            }
            
            userService.updateUser(currentUser);
            
            // 返回更新后的用户信息
            currentUser.setPassword(null); // 隐藏密码
            return Result.success(currentUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
