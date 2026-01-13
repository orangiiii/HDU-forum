# HDU论坛系统

一个基于 Spring Boot + MySQL + Redis 的内网论坛系统。

## 技术栈

### 后端
- Java 17
- Spring Boot 3.2.1
- MyBatis 3.0.3
- MySQL 8.0
- Redis
- JWT 认证

### 前端
- 原生 HTML/CSS/JavaScript
- 无需任何构建工具

## 功能特性

- ✅ 用户注册/登录
- ✅ 帖子发布/浏览/编辑/删除
- ✅ 评论功能
- ✅ 分类管理
- ✅ 点赞功能
- ✅ Redis缓存
- ✅ JWT身份验证

## 快速开始

### 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source sql/init.sql
```

默认创建了两个测试账号：
- 管理员：username: `admin`, password: `admin123`
- 普通用户：username: `user1`, password: `123456`

### 3. 启动Redis

```bash
redis-server
```

### 4. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库和Redis连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hdu_forum
    username: root
    password: 你的密码
  
  data:
    redis:
      host: localhost
      port: 6379
```

### 5. 编译运行

```bash
# 编译项目
mvn clean package

# 运行项目
java -jar target/forum-1.0.0.jar

# 或者直接使用Maven运行
mvn spring-boot:run
```

### 6. 访问系统

打开浏览器访问：http://localhost:8080

- 首页：http://localhost:8080/index.html
- 登录：http://localhost:8080/login.html
- 注册：http://localhost:8080/register.html

## 项目结构

```
HDU-forum/
├── src/
│   └── main/
│       ├── java/com/hdu/forum/
│       │   ├── ForumApplication.java     # 主程序
│       │   ├── controller/               # 控制器层
│       │   │   ├── UserController.java
│       │   │   ├── PostController.java
│       │   │   ├── CommentController.java
│       │   │   └── CategoryController.java
│       │   ├── service/                  # 业务逻辑层
│       │   │   ├── UserService.java
│       │   │   ├── PostService.java
│       │   │   ├── CommentService.java
│       │   │   └── CategoryService.java
│       │   ├── mapper/                   # 数据访问层
│       │   │   ├── UserMapper.java
│       │   │   ├── PostMapper.java
│       │   │   ├── CommentMapper.java
│       │   │   └── CategoryMapper.java
│       │   ├── entity/                   # 实体类
│       │   │   ├── User.java
│       │   │   ├── Post.java
│       │   │   ├── Comment.java
│       │   │   └── Category.java
│       │   ├── dto/                      # 数据传输对象
│       │   │   ├── Result.java
│       │   │   ├── LoginRequest.java
│       │   │   └── RegisterRequest.java
│       │   ├── config/                   # 配置类
│       │   │   ├── RedisConfig.java
│       │   │   └── CorsConfig.java
│       │   └── util/                     # 工具类
│       │       └── JwtUtil.java
│       └── resources/
│           ├── application.yml           # 配置文件
│           └── static/                   # 静态资源
│               ├── index.html
│               ├── login.html
│               ├── register.html
│               ├── detail.html
│               ├── css/
│               │   └── style.css
│               └── js/
│                   ├── config.js
│                   ├── utils.js
│                   ├── index.js
│                   ├── login.js
│                   ├── register.js
│                   └── detail.js
├── sql/
│   └── init.sql                          # 数据库初始化脚本
├── pom.xml                               # Maven配置
└── README.md                             # 项目说明
```

## API 接口

### 用户相关
- POST `/user/register` - 用户注册
- POST `/user/login` - 用户登录
- GET `/user/info` - 获取当前用户信息
- GET `/user/{id}` - 获取用户信息
- PUT `/user/update` - 更新用户信息

### 分类相关
- GET `/category/list` - 获取所有分类
- GET `/category/{id}` - 获取分类详情
- POST `/category/create` - 创建分类（管理员）
- PUT `/category/update` - 更新分类（管理员）
- DELETE `/category/{id}` - 删除分类（管理员）

### 帖子相关
- GET `/post/list` - 获取所有帖子
- GET `/post/{id}` - 获取帖子详情
- GET `/post/category/{categoryId}` - 获取分类下的帖子
- GET `/post/user/{userId}` - 获取用户的帖子
- POST `/post/create` - 创建帖子
- PUT `/post/update` - 更新帖子
- DELETE `/post/{id}` - 删除帖子
- POST `/post/like/{id}` - 点赞帖子

### 评论相关
- GET `/comment/post/{postId}` - 获取帖子的评论
- GET `/comment/user/{userId}` - 获取用户的评论
- POST `/comment/create` - 创建评论
- DELETE `/comment/{id}` - 删除评论
- POST `/comment/like/{id}` - 点赞评论

## 内网部署

1. 确保MySQL、Redis在内网可访问
2. 修改 `application.yml` 中的数据库和Redis地址为内网地址
3. 编译打包：`mvn clean package`
4. 部署到服务器并运行
5. 内网用户通过服务器IP访问：`http://服务器IP:8080`

## 注意事项

- 默认端口为8080，可在 `application.yml` 中修改
- JWT密钥在 `JwtUtil.java` 中，生产环境请修改
- 密码使用MD5加密，生产环境建议使用更安全的加密方式
- Redis默认无密码，生产环境请设置密码

## 开发计划

- [ ] 添加文件上传功能
- [ ] 添加私信功能
- [ ] 添加搜索功能
- [ ] 添加用户权限管理
- [ ] 优化前端界面
- [ ] 添加单元测试

## 许可证

MIT License
