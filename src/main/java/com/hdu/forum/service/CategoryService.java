package com.hdu.forum.service;

import com.hdu.forum.entity.Category;
import com.hdu.forum.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CATEGORY_LIST_KEY = "category:list";
    
    /**
     * 获取所有分类（带缓存）
     */
    @SuppressWarnings("unchecked")
    public List<Category> getAllCategories() {
        // 先从缓存获取
        List<Category> categories = (List<Category>) redisTemplate.opsForValue().get(CATEGORY_LIST_KEY);
        if (categories != null) {
            return categories;
        }
        
        // 缓存未命中，从数据库查询
        categories = categoryMapper.findAll();
        
        // 存入缓存，有效期1小时
        redisTemplate.opsForValue().set(CATEGORY_LIST_KEY, categories, 1, TimeUnit.HOURS);
        
        return categories;
    }
    
    /**
     * 根据ID获取分类
     */
    public Category getCategoryById(Long id) {
        return categoryMapper.findById(id);
    }
    
    /**
     * 创建分类
     */
    public void createCategory(Category category) {
        category.setStatus(1);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insert(category);
        
        // 清除缓存
        redisTemplate.delete(CATEGORY_LIST_KEY);
    }
    
    /**
     * 更新分类
     */
    public void updateCategory(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);
        
        // 清除缓存
        redisTemplate.delete(CATEGORY_LIST_KEY);
    }
    
    /**
     * 删除分类
     */
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
        
        // 清除缓存
        redisTemplate.delete(CATEGORY_LIST_KEY);
    }
}
