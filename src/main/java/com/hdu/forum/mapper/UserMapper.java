package com.hdu.forum.mapper;

import com.hdu.forum.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);
    
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
    
    
    @Insert("INSERT INTO user(username, password, avatar, role, status, create_time, update_time) " +
            "VALUES(#{username}, #{password}, #{avatar}, #{role}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    @Update("UPDATE user SET password=#{password}, avatar=#{avatar}, update_time=#{updateTime} WHERE id=#{id}")
    int update(User user);
    
    @Select("SELECT * FROM user WHERE status = 1 ORDER BY create_time DESC")
    List<User> findAll();
}
