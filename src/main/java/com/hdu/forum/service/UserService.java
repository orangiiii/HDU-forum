package com.hdu.forum.service;

import com.hdu.forum.entity.User;
import com.hdu.forum.mapper.UserMapper;
import com.hdu.forum.util.JwtUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 用户注册 - 只需要用户名和密码
     */
    public User register(String username, String password) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(DigestUtil.md5Hex(password)); // MD5加密
        user.setAvatar("default.jpg");
        user.setRole(0); // 普通用户
        user.setStatus(1); // 正常状态
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        userMapper.insert(user);
        return user;
    }
    
    /**
     * 用户登录
     */
    public String login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!user.getPassword().equals(DigestUtil.md5Hex(password))) {
            throw new RuntimeException("密码错误");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 生成token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 将token存入Redis，设置过期时间为7天
        redisTemplate.opsForValue().set("token:" + token, user, 7, TimeUnit.DAYS);
        
        return token;
    }
    
    /**
     * 根据token获取用户信息
     */
    public User getUserByToken(String token) {
        return (User) redisTemplate.opsForValue().get("token:" + token);
    }
    
    /**
     * 根据ID获取用户
     */
    public User getUserById(Long id) {
        return userMapper.findById(id);
    }
    
    /**
     * 更新用户信息
     */
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }
}
