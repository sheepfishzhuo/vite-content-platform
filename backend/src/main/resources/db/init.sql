/*
 Navicat Premium Dump SQL

 Source Server         : 260118
 Source Server Type    : MySQL
 Source Server Version : 80407 (8.4.7)
 Source Host           : localhost:3306
 Source Schema         : theme_platform

 Target Server Type    : MySQL
 Target Server Version : 80407 (8.4.7)
 File Encoding         : 65001

 Date: 21/02/2026 00:01:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '公告内容',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'notice' COMMENT '类型：notice公告, update更新, warning警告',
  `is_top` tinyint NULL DEFAULT 0 COMMENT '是否置顶',
  `is_enabled` tinyint NULL DEFAULT 1 COMMENT '是否启用',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_is_enabled`(`is_enabled` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES (1, '欢迎使用内容管理平台', '本平台支持多种内容展示风格，欢迎体验！', 'notice', 1, 1, 1, '2026-02-20 19:10:04', '2026-02-20 20:23:32');
INSERT INTO `announcement` VALUES (2, '系统升级通知', '系统将于本周六凌晨进行升级维护', 'security', 0, 1, 2, '2026-02-20 19:10:04', '2026-02-20 20:23:35');

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `section_id` bigint NULL DEFAULT NULL,
  `author_id` bigint NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 0,
  `view_count` int NULL DEFAULT 0,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_top` int NULL DEFAULT 0 COMMENT '是否置顶 0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, '政策1', 'Vue 3 组合式 API 介绍', '<p>Vue 3 是一个渐进式 JavaScript 框架...</p><h2>123</h2>', NULL, 1, NULL, 1, 100, '2026-02-20 18:16:37', '2026-02-20 18:16:37', 0);
INSERT INTO `article` VALUES (2, '政策2', 'Spring Boot 快速入门', '<p>Spring Boot 简化了 Spring 应用开发...</p>', NULL, 1, NULL, 1, 200, '2026-02-20 18:16:38', '2026-02-20 22:54:13', 0);
INSERT INTO `article` VALUES (3, '政策3', '内容平台正式上线', '<p>欢迎大家使用内容管理平台...</p>', NULL, 1, NULL, 1, 50, '2026-02-20 18:16:39', '2026-02-20 22:54:29', 0);
INSERT INTO `article` VALUES (4, '政策4', '政策4', '<p>政策4</p>', '', 1, NULL, 1, 0, '2026-02-20 22:48:51', '2026-02-20 22:51:32', 1);
INSERT INTO `article` VALUES (5, '政策5', '政策5', '<p>政策5</p>', '', 1, NULL, 1, 0, '2026-02-20 22:51:11', '2026-02-20 22:51:11', 0);
INSERT INTO `article` VALUES (6, '常见问题1', '常见问题1', '<p>常见问题1</p>', '', 2, NULL, 1, 0, '2026-02-20 23:07:03', '2026-02-20 23:18:55', 0);
INSERT INTO `article` VALUES (7, '常见问题2', '常见问题2', '<p>常见问题2</p>', '', 2, NULL, 1, 0, '2026-02-20 23:07:42', '2026-02-20 23:18:57', 0);
INSERT INTO `article` VALUES (8, ' 政策6', ' 政策6', '<p> 政策6</p>', '', 1, NULL, 1, 0, '2026-02-20 23:13:52', '2026-02-20 23:19:00', 0);
INSERT INTO `article` VALUES (9, '政策7', '政策7', '<p>政策7</p>', '', 1, NULL, 1, 0, '2026-02-20 23:21:35', '2026-02-20 23:21:35', 0);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT 0,
  `sort_order` int NULL DEFAULT 0,
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_hidden` tinyint NULL DEFAULT 0,
  `menu_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'admin',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 513 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '内容管理', '/admin/content', 0, 1, 'Document', NULL, 0, 'admin', '2026-02-20 18:16:37', '2026-02-20 18:16:37');
INSERT INTO `menu` VALUES (2, '文章列表', '/admin/content/articles', 1, 1, 'List', 'article/List.vue', 0, 'admin', '2026-02-20 18:16:37', '2026-02-20 18:16:37');
INSERT INTO `menu` VALUES (3, '发布文章', '/admin/content/publish', 1, 2, 'Edit', 'article/Edit.vue', 0, 'admin', '2026-02-20 18:16:37', '2026-02-20 18:16:37');
INSERT INTO `menu` VALUES (4, '系统管理', '/admin/system', 0, 2, 'Setting', NULL, 0, 'admin', '2026-02-20 18:16:37', '2026-02-20 23:42:15');
INSERT INTO `menu` VALUES (5, '菜单管理', '/admin/system/menus', 4, 1, 'Menu', 'system/Menu.vue', 0, 'admin', '2026-02-20 18:16:37', '2026-02-20 23:42:35');
INSERT INTO `menu` VALUES (6, '区块管理', '/admin/system/sections', 1, 3, 'Grid', 'system/Section.vue', 0, 'admin', '2026-02-20 18:16:37', '2026-02-20 23:42:42');
INSERT INTO `menu` VALUES (7, '用户管理', '/admin/system/users', 4, 2, 'User', 'system/User.vue', 0, 'admin', '2026-02-20 18:16:37', '2026-02-20 23:42:47');
INSERT INTO `menu` VALUES (8, '公告管理', '/admin/system/announcements', 1, 4, 'List', 'views/system/Announcement.vue', 0, 'admin', '2026-02-20 19:47:50', '2026-02-20 23:42:51');
INSERT INTO `menu` VALUES (9, '快速入口', '/admin/system/quick-entries', 1, 5, 'Guide', 'views/system/QuickEntry.vue', 0, 'admin', '2026-02-20 19:47:50', '2026-02-20 23:42:54');

-- ----------------------------
-- Table structure for quick_entry
-- ----------------------------
DROP TABLE IF EXISTS `quick_entry`;
CREATE TABLE `quick_entry`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '入口名称',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '跳转路径',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'Document' COMMENT '图标名称',
  `color` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' COMMENT '背景颜色',
  `background_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '背景图片URL',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint NULL DEFAULT 1 COMMENT '是否启用',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_is_enabled`(`is_enabled` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '快速入口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of quick_entry
-- ----------------------------
INSERT INTO `quick_entry` VALUES (1, '用户注册', '/', 'User', 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', '', 1, 1, '2026-02-20 19:21:21', '2026-02-20 22:22:15');
INSERT INTO `quick_entry` VALUES (2, '国家奖学金入口', '/', 'Flag', 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)', '', 2, 1, '2026-02-20 19:21:21', '2026-02-20 22:25:57');
INSERT INTO `quick_entry` VALUES (3, '学业奖学金入口', '/', 'Promotion', 'linear-gradient(135deg, #4481eb 0%, #04befe 100%)', '', 3, 1, '2026-02-20 19:21:21', '2026-02-20 22:27:35');
INSERT INTO `quick_entry` VALUES (4, '疑难问题反馈', '/', 'Bell', 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)', '', 4, 1, '2026-02-20 19:21:21', '2026-02-20 22:27:54');

-- ----------------------------
-- Table structure for section
-- ----------------------------
DROP TABLE IF EXISTS `section`;
CREATE TABLE `section`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `display_style` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'card',
  `sort_order` int NULL DEFAULT 0,
  `is_enabled` tinyint NULL DEFAULT 1,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of section
-- ----------------------------
INSERT INTO `section` VALUES (1, '政策规定', '2027年度奖学金评定政策', 'list', 1, 1, '2026-02-20 18:16:37', '2026-02-20 23:22:24');
INSERT INTO `section` VALUES (2, '常见问题', '奖学金评定系统使用方法解答', 'card', 2, 1, '2026-02-20 18:16:37', '2026-02-20 23:09:17');
INSERT INTO `section` VALUES (3, '相关下载', '奖学金评定常用文档', 'compact', 3, 1, '2026-02-20 22:34:09', '2026-02-20 22:36:53');
INSERT INTO `section` VALUES (4, '公告公示', '奖学金评定结果公示', 'list', 4, 1, '2026-02-20 22:35:52', '2026-02-20 22:46:26');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'user',
  `status` tinyint NULL DEFAULT 1,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'admin', '$2a$10$RmXM20T5sAptN6ugsR5AaOs7y4o10IJR0.gyIKrucQkj3nZ96Gsva', 'admin@example.com', 'admin', 1, '2026-02-20 18:20:21');
INSERT INTO `user` VALUES (3, 'yyz', '$2a$10$k6G.UuW0hnurvO.4dfQyXenUDPGa1wY1i9sN/scqIJQZNPJmT8Ujq', '2678660795@qq.com', 'user', 1, '2026-02-20 18:23:03');

SET FOREIGN_KEY_CHECKS = 1;
