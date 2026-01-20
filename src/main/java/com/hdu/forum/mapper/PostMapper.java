package com.hdu.forum.mapper;

import com.hdu.forum.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    
    @Select("SELECT p.*, u.username, c.name as category_name FROM post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.id = #{id}")
    Post findById(Long id);
    
    @Select("SELECT p.*, u.username, c.name as category_name, p.image_url FROM post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.status = 1 ORDER BY p.is_top DESC, p.create_time DESC")
    List<Post> findAll();
    
    @Select("SELECT p.*, u.username, c.name as category_name FROM post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.category_id = #{categoryId} AND p.status = 1 " +
            "ORDER BY p.is_top DESC, p.create_time DESC")
    List<Post> findByCategoryId(Long categoryId);
    
    @Select("SELECT p.*, u.username, c.name as category_name FROM post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.user_id = #{userId} ORDER BY p.create_time DESC")
    List<Post> findByUserId(Long userId);
    
    @Insert("INSERT INTO post(user_id, category_id, title, content, image_url, grad_year, view_count, like_count, " +
            "comment_count, is_top, status, create_time, update_time, publish_time) " +
            "VALUES(#{userId}, #{categoryId}, #{title}, #{content}, #{imageUrl}, #{gradYear}, #{viewCount}, #{likeCount}, " +
            "#{commentCount}, #{isTop}, #{status}, #{createTime}, #{updateTime}, #{publishTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Post post);
    
    @Update("UPDATE post SET category_id=#{categoryId}, title=#{title}, content=#{content}, " +
            "image_url=#{imageUrl}, grad_year=#{gradYear}, " +
            "is_top=#{isTop}, status=#{status}, update_time=#{updateTime}, publish_time=#{publishTime} WHERE id=#{id}")
    int update(Post post);
    
    @Update("UPDATE post SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(Long id);
    
    @Update("UPDATE post SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(Long id);
    
    @Update("UPDATE post SET comment_count = comment_count + 1 WHERE id = #{id}")
    int incrementCommentCount(Long id);
    
    @Update("UPDATE post SET status = 2 WHERE id = #{id}")
    int deleteById(Long id);
    
    @Select("SELECT p.*, u.username, c.name as category_name FROM post p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.user_id = #{userId} AND p.status = 0 " +
            "ORDER BY p.update_time DESC LIMIT 1")
    Post findLatestDraftByUserId(Long userId);
}
