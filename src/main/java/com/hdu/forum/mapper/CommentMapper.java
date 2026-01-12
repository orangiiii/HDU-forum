package com.hdu.forum.mapper;

import com.hdu.forum.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    
    @Select("SELECT c.*, u.username FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.id = #{id}")
    Comment findById(Long id);
    
    @Select("SELECT c.*, u.username, p.username as parent_username FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "LEFT JOIN comment pc ON c.parent_id = pc.id " +
            "LEFT JOIN user p ON pc.user_id = p.id " +
            "WHERE c.post_id = #{postId} AND c.status = 1 " +
            "ORDER BY c.create_time ASC")
    List<Comment> findByPostId(Long postId);
    
    @Select("SELECT c.*, u.username FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.user_id = #{userId} AND c.status = 1 " +
            "ORDER BY c.create_time DESC")
    List<Comment> findByUserId(Long userId);
    
    @Insert("INSERT INTO comment(post_id, user_id, parent_id, content, like_count, status, create_time, update_time) " +
            "VALUES(#{postId}, #{userId}, #{parentId}, #{content}, #{likeCount}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);
    
    @Update("UPDATE comment SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(Long id);
    
    @Update("UPDATE comment SET status = 0 WHERE id = #{id}")
    int deleteById(Long id);
}
