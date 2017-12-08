/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50616
Source Host           : localhost:3306
Source Database       : hiynn_cas_permissions_system

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2017-12-08 12:11:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_app
-- ----------------------------
DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app` (
  `id` int(4) NOT NULL AUTO_INCREMENT COMMENT '系统id',
  `app_key` varchar(255) NOT NULL COMMENT '系统key值',
  `app_name` varchar(255) NOT NULL COMMENT '系统名称',
  `app_desc` varchar(255) DEFAULT NULL COMMENT '系统描述',
  `app_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '系统状态：0停用，1启用',
  `app_context_path` varchar(255) DEFAULT NULL COMMENT '系统根路经',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_appkey` (`app_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_app
-- ----------------------------
INSERT INTO `t_app` VALUES ('1', 'appkey1', '测试系统', '测试系统描述', '1', '/castest', '2017-12-07 18:13:35', '2017-12-07 18:13:38');

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` int(4) NOT NULL COMMENT '系统id',
  `permission` varchar(50) DEFAULT NULL,
  `permission_name` varchar(50) DEFAULT NULL,
  `available` tinyint(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`,`app_id`),
  KEY `fk_permission_appid` (`app_id`),
  CONSTRAINT `fk_permission_appid` FOREIGN KEY (`app_id`) REFERENCES `t_app` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('1', '1', 'user:create', '新建用户', '1', '新建用户权限', '2017-09-19 14:31:29.000000', '2017-09-19 14:31:32.000000');
INSERT INTO `t_permission` VALUES ('2', '1', 'user:delete', '删除用户', '1', '删除用户权限', '2017-09-19 14:32:21.000000', '2017-09-19 14:32:23.000000');
INSERT INTO `t_permission` VALUES ('3', '1', 'user:update', '修改用户', '1', '修改用户权限', '2017-09-19 14:33:15.000000', '2017-09-19 14:33:19.000000');
INSERT INTO `t_permission` VALUES ('4', '1', 'user:view', '查看用户', '1', '查看用户权限', '2017-09-19 14:56:10.000000', '2017-09-19 14:56:13.000000');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` int(4) NOT NULL COMMENT '系统id',
  `role` varchar(50) NOT NULL,
  `role_name` varchar(50) DEFAULT NULL,
  `available` tinyint(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`,`app_id`),
  KEY `fk_role_appid` (`app_id`) USING BTREE,
  CONSTRAINT `fk_role_appid` FOREIGN KEY (`app_id`) REFERENCES `t_app` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '1', 'admin', '管理员', '1', '管理员角色', '2017-09-19 14:34:49.000000', '2017-09-19 14:34:52.000000');
INSERT INTO `t_role` VALUES ('3', '1', 'role1', '普通用户', '1', '普通用户角色', '2017-09-19 15:45:05.000000', '2017-09-19 15:45:07.000000');

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES ('1', '1', '2017-09-19 14:54:59.000000', '2017-09-19 14:55:01.000000');
INSERT INTO `t_role_permission` VALUES ('1', '2', '2017-09-19 14:55:09.000000', '2017-09-19 14:55:11.000000');
INSERT INTO `t_role_permission` VALUES ('1', '3', '2017-09-19 14:55:21.000000', '2017-09-19 14:55:24.000000');
INSERT INTO `t_role_permission` VALUES ('1', '4', '2017-09-19 14:56:47.000000', '2017-09-19 14:56:51.000000');
INSERT INTO `t_role_permission` VALUES ('2', '4', '2017-09-19 14:57:38.000000', '2017-09-19 14:57:41.000000');

-- ----------------------------
-- Table structure for t_url_filter
-- ----------------------------
DROP TABLE IF EXISTS `t_url_filter`;
CREATE TABLE `t_url_filter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` int(4) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '描述',
  `url` varchar(100) DEFAULT NULL COMMENT '路径',
  `roles` varchar(100) DEFAULT NULL COMMENT '访问url需要的角色',
  `permissions` varchar(100) DEFAULT NULL COMMENT '访问url需要的权限',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`,`app_id`),
  KEY `fk_url_filter_appid` (`app_id`) USING BTREE,
  CONSTRAINT `fk_url_filter_appid` FOREIGN KEY (`app_id`) REFERENCES `t_app` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_url_filter
-- ----------------------------
INSERT INTO `t_url_filter` VALUES ('1', '1', '权限管理', '/admin/**', 'admin', '', '2017-09-25 17:35:28', '2017-09-25 17:35:32');
INSERT INTO `t_url_filter` VALUES ('7', '1', '路径权限设置', '/urlFilter/**', 'admin', null, '2017-09-26 11:09:27', '2017-09-26 11:09:27');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `salt` varchar(50) NOT NULL COMMENT '密码加密的salt',
  `locked` tinyint(1) unsigned DEFAULT '0' COMMENT '是否锁定',
  `organization_id` int(20) unsigned DEFAULT NULL COMMENT '组织部门id',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', 'fd32e054edde3dfa430f2384370b5e7e', 'a522078f8b8bcccc341ba1268a8c99a6', '0', null, null, null, null, null, null);
INSERT INTO `t_user` VALUES ('11', 'admin1', 'fd32e054edde3dfa430f2384370b5e7e', 'a522078f8b8bcccc341ba1268a8c99a6', '0', '0', '1@qq.com', '12345678901', null, '2017-06-02 19:21:03.000000', '2017-06-02 19:21:03.000000');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `gmt_create` timestamp(6) NULL DEFAULT NULL,
  `gmt_modified` timestamp(6) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`,`role_id`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1', 'admin', '2017-09-19 14:53:33.000000', '2017-09-19 14:53:36.000000');
INSERT INTO `t_user_role` VALUES ('11', '1', 'admin1', null, null);
