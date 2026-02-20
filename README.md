# 2026年度奖学金评定专题网站

基于 Vue 3 + Spring Boot 构建的现代化内容管理系统，专为奖学金评定专题设计。

## 技术栈

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Vue Router** - 官方路由管理器
- **Pinia** - 新一代状态管理工具
- **Element Plus** - Vue 3 组件库
- **WangEditor** - 富文本编辑器

### 后端
- **Spring Boot 3.2** - Java Web 框架
- **MyBatis-Plus** - MyBatis 增强工具
- **MySQL** - 关系型数据库
- **Maven** - 项目构建管理

## 功能特性

### 前台展示
- 🏠 **首页展示** - 公告通知、快速入口、内容区块
- 📰 **文章浏览** - 支持卡片、列表、紧凑三种展示风格
- 📌 **置顶功能** - 重要文章置顶显示
- 🔍 **分类浏览** - 按区块查看所有文章
- 📱 **响应式设计** - 适配各种屏幕尺寸

### 后台管理
- 📝 **文章管理** - 发布、编辑、删除文章，支持富文本编辑
- 📂 **区块管理** - 自定义内容区块及展示风格
- 📢 **公告管理** - 发布和管理公告通知
- 🔗 **快速入口** - 管理首页快捷入口
- 🗂️ **菜单管理** - 动态菜单配置
- 👤 **用户管理** - 用户账号管理


## 项目结构

```
vite-content-platform/
├── backend/                    # 后端项目
│   ├── src/main/java/
│   │   └── com/platform/backend/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       └── common/         # 公共类
│   └── src/main/resources/
│       ├── application.yml     # 配置文件
│       └── db/
│           └── init.sql        # 数据库初始化脚本
├── src/                        # 前端项目
│   ├── api/                    # API 接口
│   ├── components/             # 公共组件
│   ├── layouts/                # 布局组件
│   ├── router/                 # 路由配置
│   ├── stores/                 # 状态管理
│   ├── utils/                  # 工具函数
│   └── views/                  # 页面组件
├── public/                     # 静态资源
├── package.json                # 前端依赖
└── pom.xml                     # 后端依赖
```

## 快速开始

### 环境要求
- Node.js >= 16
- JDK >= 17
- MySQL >= 8.0
- Maven >= 3.8

### 数据库配置

1. 创建数据库
```sql
CREATE DATABASE theme_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行数据库初始化脚本
```bash
mysql -u root -p theme_platform < backend/src/main/resources/db/init.sql
```

3. 修改后端配置
```yaml
# backend/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/theme_platform?useSSL=false&serverTimezone=UTC
    username: your_root
    password: your_password
```

### 安装与运行

1. 安装前端依赖
```bash
npm install
```

2. 启动前端开发服务器
```bash
npm run dev
```

3. 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

4. 访问应用
- 前台首页: http://localhost:5173/
- 后台管理: http://localhost:5173/admin

### 默认账号
- 用户名: `admin`
- 密码: `admin123`

## 构建部署

### 前端构建
```bash
npm run build
```

### 后端构建
```bash
cd backend
mvn clean package
```

### 生产部署
将前端构建产物 `dist/` 目录放置到后端静态资源目录，或使用 Nginx 反向代理。

## API 接口

| 模块 | 接口 | 说明 |
|------|------|------|
| 认证 | POST /api/auth/login | 用户登录 |
| 文章 | GET /api/articles | 文章列表 |
| 文章 | GET /api/articles/{id} | 文章详情 |
| 区块 | GET /api/sections/homepage | 首页区块 |
| 区块 | GET /api/sections/{id}/items | 区块内容 |
| 公告 | GET /api/announcements | 公告列表 |
| 快捷入口 | GET /api/quick-entries | 快捷入口列表 |

## 开发说明

### 前端开发
- 使用 `<script setup>` 语法
- 组件命名采用 PascalCase
- API 调用统一使用 `@/api/` 目录下的模块

### 后端开发
- 遵循 RESTful API 设计
- 使用 MyBatis-Plus 简化数据库操作
- 统一返回格式 `Result<T>`

