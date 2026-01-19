-- HDU论坛数据库初始化脚本

CREATE DATABASE IF NOT EXISTS hdu_forum DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hdu_forum;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(MD5加密)',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `default_grad_year` INT COMMENT '默认届数',
    `role` TINYINT DEFAULT 0 COMMENT '角色: 0-普通用户, 1-管理员',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(255) COMMENT '分类描述',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_sort (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 帖子表
CREATE TABLE IF NOT EXISTS `post` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `image_url` VARCHAR(500) COMMENT '附图URL',
    `grad_year` INT COMMENT '届数',
    `view_count` INT DEFAULT 0 COMMENT '浏览数',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶: 0-否, 1-是',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布, 2-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `publish_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    INDEX idx_user_id (`user_id`),
    INDEX idx_category_id (`category_id`),
    INDEX idx_create_time (`create_time`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父评论ID(0表示顶级评论)',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-已删除, 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_post_id (`post_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_parent_id (`parent_id`),
    FOREIGN KEY (`post_id`) REFERENCES `post`(`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 插入初始数据

-- 插入管理员账号 (密码: admin123, MD5加密后)
INSERT INTO `user` (`username`, `password`, `role`, `status`) 
VALUES ('admin', '0192023a7bbd73250516f069df18b500',  1, 1);

-- 插入测试用户 (密码: 123456)
INSERT INTO `user` (`username`, `password`, `role`, `status`) 
VALUES ('user1', 'e10adc3949ba59abbe56e057f20f883e', 0, 1);
-- 插入分类
INSERT INTO `category` (`name`, `description`, `sort`, `status`) VALUES
('技术交流', '分享技术心得，讨论技术问题', 1, 1),
('学习资料', '课程资料、学习笔记分享', 2, 1),
('校园生活', '校园趣事、活动信息', 3, 1),
('求职招聘', '实习、工作机会分享', 4, 1),
('灌水专区', '随便聊聊', 5, 1);

-- 插入示例帖子
INSERT INTO `post` (`user_id`, `category_id`, `title`, `content`, `view_count`, `like_count`, `comment_count`, `status`) VALUES
(1, 1, '欢迎使用HDU论坛系统', '这是一个基于Spring Boot + MySQL + Redis的论坛系统，欢迎大家使用！', 100, 10, 2, 1),
(2, 1, 'Java学习心得分享', 'Java是一门非常优秀的编程语言，今天和大家分享一些学习心得...', 50, 5, 1, 1);

-- 插入示例评论
INSERT INTO `comment` (`post_id`, `user_id`, `parent_id`, `content`, `like_count`, `status`) VALUES
(1, 2, 0, '很不错的系统！', 3, 1),
(1, 1, 1, '感谢支持！', 1, 1);
