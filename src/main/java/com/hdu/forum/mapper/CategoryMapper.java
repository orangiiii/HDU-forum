package com.hdu.forum.mapper;

import com.hdu.forum.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    
    @Select("SELECT * FROM category WHERE id = #{id}")
    Category findById(Long id);
    
    @Select("SELECT * FROM category WHERE status = 1 ORDER BY sort ASC")
    List<Category> findAll();
    
    @Insert("INSERT INTO category(name, description, sort, status, create_time, update_time) " +
            "VALUES(#{name}, #{description}, #{sort}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);
    
    @Update("UPDATE category SET name=#{name}, description=#{description}, sort=#{sort}, " +
            "status=#{status}, update_time=#{updateTime} WHERE id=#{id}")
    int update(Category category);
    
    @Delete("DELETE FROM category WHERE id = #{id}")
    int deleteById(Long id);
}
