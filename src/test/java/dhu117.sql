/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50709
 Source Host           : localhost
 Source Database       : dhu117

 Target Server Type    : MySQL
 Target Server Version : 50709
 File Encoding         : utf-8

 Date: 09/14/2016 18:08:04 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_uc_admin`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_admin`;
CREATE TABLE `t_uc_admin` (
  `userName` varchar(20) COLLATE utf8_bin NOT NULL,
  `password` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `lastLoginIP` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `lastLoginFailureCount` int(11) DEFAULT NULL,
  `email` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `realName` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '管理员类型 1 系统管理员 2 普通管理员',
  PRIMARY KEY (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `t_uc_admin`
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_admin` VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', null, null, null, null, null, null, null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `t_uc_admin_auth`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_admin_auth`;
CREATE TABLE `t_uc_admin_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authName` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `appCode` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_uc_admin_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_admin_role`;
CREATE TABLE `t_uc_admin_role` (
  `userName` varchar(45) COLLATE utf8_bin NOT NULL,
  `roleCode` varchar(45) COLLATE utf8_bin NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `upateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`userName`,`roleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_uc_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_role`;
CREATE TABLE `t_uc_role` (
  `roleCode` varchar(50) COLLATE utf8_bin NOT NULL,
  `type` int(11) DEFAULT '1' COMMENT '角色类型，1、增删改查 2、改查',
  `roleName` varchar(45) COLLATE utf8_bin NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 1正常 0失效',
  PRIMARY KEY (`roleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_uc_role_auth`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_role_auth`;
CREATE TABLE `t_uc_role_auth` (
  `authId` bigint(20) NOT NULL,
  `roleCode` varchar(45) COLLATE utf8_bin NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `upateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`authId`,`roleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `t_uc_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_user`;
CREATE TABLE `t_uc_user` (
  `ucid` bigint(20) NOT NULL AUTO_INCREMENT,
  `userName` varchar(100) DEFAULT NULL COMMENT '用户名',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint(4) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `lastLoginIP` varchar(100) DEFAULT NULL,
  `cityCode` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '用户状态，1 正常 0 删除 ',
  `loginFailureCount` int(11) DEFAULT NULL COMMENT '连续登陆失败次数',
  `score` bigint(20) DEFAULT '0' COMMENT '积分',
  `type` int(11) DEFAULT NULL COMMENT '用户类型 1 前台用户 2 商家',
  `profileImage` varchar(200) DEFAULT NULL COMMENT '头像',
  `amount` bigint(20) DEFAULT '0' COMMENT '余额',
  PRIMARY KEY (`ucid`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_uc_user`
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_user` VALUES ('1', null, '1880818221', 'admin', null, null, null, null, null, null, '1', null, '0', null, null, '0'), ('2', null, '18801619351', '21232f297a57a5a743894a0e4a801fc3', null, null, null, null, null, null, '1', null, '0', null, null, '0'), ('3', null, '18801619352', '21232f297a57a5a743894a0e4a801fc3', null, null, null, null, null, null, '1', null, '0', null, null, '0'), ('4', null, '18801619354', '21232f297a57a5a743894a0e4a801fc3', null, null, null, null, null, null, '1', null, '0', null, null, '0');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
