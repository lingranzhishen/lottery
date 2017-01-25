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

 Date: 01/25/2017 20:25:08 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_cms_article`
-- ----------------------------
DROP TABLE IF EXISTS `t_cms_article`;
CREATE TABLE `t_cms_article` (
  `articleId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `categoryId` bigint(20) DEFAULT NULL COMMENT '所属文章目录',
  `path` varchar(200) DEFAULT NULL,
  `cover` varchar(60) DEFAULT NULL COMMENT '封面图片',
  `thumb` varchar(60) DEFAULT NULL COMMENT '缩略图',
  `title` varchar(200) DEFAULT '' COMMENT '标题',
  `summary` varchar(2000) DEFAULT NULL COMMENT '概要',
  `content` mediumtext COMMENT '文件内容',
  `viewCount` int(11) DEFAULT '0' COMMENT '浏览数',
  `commentCount` int(11) DEFAULT '0' COMMENT '评论数',
  `likeCount` int(11) DEFAULT '0' COMMENT '点赞数',
  `status` int(11) DEFAULT '1' COMMENT '状态：0 隐藏 1 显示',
  `checkStatus` int(11) DEFAULT '1' COMMENT '状态：0 未审核 1 审核通过',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(200) DEFAULT NULL COMMENT '创建人',
  `updator` varchar(200) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`articleId`),
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='文章';

-- ----------------------------
--  Table structure for `t_cms_category`
-- ----------------------------
DROP TABLE IF EXISTS `t_cms_category`;
CREATE TABLE `t_cms_category` (
  `categoryId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '目录ID',
  `parentId` bigint(20) NOT NULL DEFAULT '0' COMMENT '父亲Id，用于构建目录树',
  `ename` varchar(45) NOT NULL COMMENT '英文名',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '中文名',
  `path` varchar(200) NOT NULL DEFAULT '' COMMENT '路径',
  `content` text,
  `level` tinyint(4) DEFAULT '1' COMMENT '层级',
  `sort` tinyint(4) DEFAULT '0' COMMENT '排序',
  `width` int(11) DEFAULT '0',
  `height` int(11) DEFAULT '0',
  `articleCount` int(11) DEFAULT '0' COMMENT '文件数',
  `status` int(11) DEFAULT '0' COMMENT '状态：0 隐藏 1 显示',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL,
  `creator` varchar(200) DEFAULT NULL COMMENT '创建人',
  `updator` varchar(200) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`categoryId`),
  UNIQUE KEY `idx_ename` (`ename`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='文章类型';

-- ----------------------------
--  Table structure for `t_cms_headline`
-- ----------------------------
DROP TABLE IF EXISTS `t_cms_headline`;
CREATE TABLE `t_cms_headline` (
  `headlineId` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `picture` varchar(100) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `sort` tinyint(4) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `creator` varchar(200) DEFAULT NULL COMMENT '创建人',
  `updator` varchar(200) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`headlineId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `t_lottery_record`
-- ----------------------------
DROP TABLE IF EXISTS `t_lottery_record`;
CREATE TABLE `t_lottery_record` (
  `lotteryNo` varchar(100) NOT NULL,
  `firstDigit` int(11) DEFAULT NULL,
  `secondDigit` int(11) DEFAULT NULL,
  `thirdDigit` int(11) DEFAULT NULL,
  `fourthDigit` int(11) DEFAULT NULL,
  `fifthDigit` int(11) DEFAULT NULL,
  `number` varchar(11) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL COMMENT '生成时间',
  `sequenceOfToday` int(11) DEFAULT NULL COMMENT '一天当中的第几期',
  `type` tinyint(4) DEFAULT '1' COMMENT '彩票种类，默认1、时时彩',
  PRIMARY KEY (`lotteryNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_lottery_record`
-- ----------------------------
BEGIN;
INSERT INTO `t_lottery_record` VALUES ('170124071', '7', '8', '1', '3', '1', '78131', '2017-01-24 19:26:09', '71', '1'), ('170124072', '4', '1', '4', '8', '2', '41482', '2017-01-24 19:26:09', '72', '1'), ('170124073', '0', '5', '1', '3', '2', '05132', '2017-01-24 19:26:09', '73', '1'), ('170124074', '2', '4', '8', '4', '4', '24844', '2017-01-24 19:26:09', '74', '1'), ('170124075', '3', '8', '1', '6', '5', '38165', '2017-01-24 19:26:09', '75', '1'), ('170124076', '4', '5', '8', '7', '7', '45877', '2017-01-24 18:56:36', '76', '1'), ('170124077', '7', '5', '1', '1', '2', '75112', '2017-01-24 19:26:09', '77', '1'), ('170124078', '0', '1', '2', '1', '8', '01218', '2017-01-24 19:26:09', '78', '1'), ('170124079', '0', '2', '4', '9', '0', '02490', '2017-01-24 19:26:09', '79', '1'), ('170124080', '7', '4', '0', '0', '9', '74009', '2017-01-24 19:30:01', '80', '1'), ('170124081', '1', '5', '4', '6', '4', '15464', '2017-01-24 19:42:00', '81', '1'), ('170124082', '8', '2', '6', '6', '0', '82660', '2017-01-24 19:51:00', '82', '1'), ('170124083', '2', '1', '6', '8', '2', '21682', '2017-01-24 20:06:00', '83', '1'), ('170124084', '7', '0', '9', '3', '8', '70938', '2017-01-24 20:09:03', '84', '1'), ('170125014', '4', '2', '8', '3', '8', '42838', '2017-01-25 03:10:35', '14', '1'), ('170125015', '8', '4', '4', '1', '6', '84416', '2017-01-25 03:10:35', '15', '1'), ('170125016', '5', '3', '8', '7', '3', '53873', '2017-01-25 03:10:35', '16', '1'), ('170125017', '3', '9', '7', '3', '7', '39737', '2017-01-25 03:10:35', '17', '1'), ('170125018', '2', '2', '1', '2', '8', '22128', '2017-01-25 03:10:35', '18', '1'), ('170125019', '4', '4', '8', '4', '4', '44844', '2017-01-25 03:10:35', '19', '1'), ('170125020', '9', '3', '5', '6', '3', '93563', '2017-01-25 03:10:35', '20', '1'), ('170125021', '4', '9', '2', '7', '5', '49275', '2017-01-25 03:10:35', '21', '1'), ('170125022', '3', '3', '8', '5', '2', '33852', '2017-01-25 03:10:34', '22', '1'), ('170125068', '2', '0', '9', '3', '4', '20934', '2017-01-25 18:52:45', '68', '1'), ('170125069', '6', '9', '5', '0', '8', '69508', '2017-01-25 18:52:45', '69', '1'), ('170125070', '8', '3', '8', '0', '4', '83804', '2017-01-25 18:52:45', '70', '1'), ('170125071', '1', '9', '3', '8', '7', '19387', '2017-01-25 18:52:45', '71', '1'), ('170125072', '4', '1', '5', '4', '4', '41544', '2017-01-25 18:52:45', '72', '1'), ('170125073', '6', '3', '4', '8', '8', '63488', '2017-01-25 18:52:45', '73', '1'), ('170125074', '5', '3', '1', '1', '9', '53119', '2017-01-25 18:52:45', '74', '1'), ('170125075', '8', '6', '3', '0', '8', '86308', '2017-01-25 18:52:45', '75', '1'), ('170125076', '4', '9', '5', '6', '5', '49565', '2017-01-25 18:52:45', '76', '1');
COMMIT;

-- ----------------------------
--  Table structure for `t_lottery_rule`
-- ----------------------------
DROP TABLE IF EXISTS `t_lottery_rule`;
CREATE TABLE `t_lottery_rule` (
  `ruleName` varchar(255) DEFAULT NULL,
  `ruleCode` varchar(255) NOT NULL,
  `ruleDesc` varchar(255) DEFAULT NULL,
  `number` int(11) NOT NULL COMMENT '连续多少次漏的情况',
  PRIMARY KEY (`ruleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_lottery_rule`
-- ----------------------------
BEGIN;
INSERT INTO `t_lottery_rule` VALUES ('大小单双监控规则', 'singleDigitRule', '规则修改', '100');
COMMIT;

-- ----------------------------
--  Table structure for `t_sys_parameter`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_parameter`;
CREATE TABLE `t_sys_parameter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '参数编码',
  `optionValue` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '参数值',
  `createTime` datetime DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '参数类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `t_sys_parameter`
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_parameter` VALUES ('1', 'uc', null, '2016-09-14 22:06:14', 'app');
COMMIT;

-- ----------------------------
--  Table structure for `t_uc_admin`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_admin`;
CREATE TABLE `t_uc_admin` (
  `userName` varchar(20) CHARACTER SET utf8 NOT NULL,
  `password` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `lastLoginIP` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `lastLoginFailureCount` int(11) DEFAULT NULL,
  `email` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `phone` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `realName` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '管理员类型 1 系统管理员 2 普通管理员',
  PRIMARY KEY (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `t_uc_admin`
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_admin` VALUES ('aaa', '77e2edcc9b40441200e31dc57dbb8829', null, null, null, null, null, '', '18801619354', '爽肤水', null), ('admin', '21232f297a57a5a743894a0e4a801fc3', null, null, null, null, null, null, null, null, null), ('阿斯顿发斯蒂芬', 'c3284d0f94606de1fd2af172aba15bf3', null, null, null, null, null, '', '18801619352', '', null);
COMMIT;

-- ----------------------------
--  Table structure for `t_uc_admin_auth`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_admin_auth`;
CREATE TABLE `t_uc_admin_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authName` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `remark` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `appCode` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '应用编码',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `url` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限对应的URL',
  `parentId` int(11) DEFAULT '0' COMMENT '上级id 为0是根权限',
  `level` int(11) DEFAULT NULL COMMENT '权限类型 1 系统权限 2 系统功能权限 3系统功能子功能权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='权限表';

-- ----------------------------
--  Records of `t_uc_admin_auth`
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_admin_auth` VALUES ('1', '用户中心', '用户中心', 'uc', '2016-09-14 22:06:52', '2016-09-14 22:06:55', '/', '0', '1'), ('2', '信息管理中心', null, 'cms', null, null, '/', '0', '1'), ('3', '用户管理', null, 'uc', null, null, '/user', '1', '2'), ('4', '角色管理', null, 'uc', null, null, '/role', '1', '2'), ('6', '管理员管理', null, 'uc', null, null, '/admin', '1', '2');
COMMIT;

-- ----------------------------
--  Table structure for `t_uc_admin_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_admin_role`;
CREATE TABLE `t_uc_admin_role` (
  `userName` varchar(45) CHARACTER SET utf8 NOT NULL,
  `roleCode` varchar(45) CHARACTER SET utf8 NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `creator` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `updator` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`userName`,`roleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='管理员角色对照表';

-- ----------------------------
--  Records of `t_uc_admin_role`
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_admin_role` VALUES ('', 'admin', null, null, null, '阿斯顿发送到'), ('aaa', 'admin', null, null, null, 'afas'), ('aaa', 'cmsAdmin', null, null, null, 'adasd'), ('aaa', 'userAdmin', null, null, null, 'ada'), ('admin', 'admin', null, null, null, null), ('admin', 'cmsAdmin', null, null, null, 'admin'), ('admin', 'userAdmin', null, null, null, 'admin');
COMMIT;

-- ----------------------------
--  Table structure for `t_uc_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_role`;
CREATE TABLE `t_uc_role` (
  `roleCode` varchar(50) CHARACTER SET utf8 NOT NULL,
  `type` int(11) DEFAULT '1' COMMENT '角色类型，1、增删改查 2、改查',
  `roleName` varchar(45) CHARACTER SET utf8 NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 1正常 0失效',
  PRIMARY KEY (`roleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

-- ----------------------------
--  Records of `t_uc_role`
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_role` VALUES ('admin', '1', '超级管理员', '2016-09-14 22:09:19', '2016-09-14 22:09:21', '1'), ('cmsAdmin', '2', '文章管理员', null, null, null), ('userAdmin', '1', '用户管理员', null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `t_uc_role_auth`
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_role_auth`;
CREATE TABLE `t_uc_role_auth` (
  `authId` bigint(20) NOT NULL,
  `roleCode` varchar(45) CHARACTER SET utf8 NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `updator` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`authId`,`roleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色权限关系表';

-- ----------------------------
--  Records of `t_uc_role_auth`
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_role_auth` VALUES ('1', 'admin', '2016-09-14 22:09:58', '2016-09-14 22:10:00', null, null), ('1', 'cmsAdmin', null, null, null, 'asdfasf'), ('2', 'admin', null, null, null, null), ('2', 'cmsAdmin', null, null, null, 'asda'), ('3', 'admin', null, null, null, null), ('3', 'cmsAdmin', null, null, null, 'adsa'), ('6', 'cmsAdmin', null, null, null, 'sfsdf');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_uc_user`
-- ----------------------------
BEGIN;
INSERT INTO `t_uc_user` VALUES ('1', null, '1880818221', 'admin', null, null, null, null, null, null, '1', null, '0', '1', null, '0'), ('2', null, '18801619351', '21232f297a57a5a743894a0e4a801fc3', null, null, null, null, null, null, '1', null, '0', '1', null, '0'), ('3', null, '18801619352', '21232f297a57a5a743894a0e4a801fc3', null, null, null, null, null, null, '1', null, '0', '1', null, '0'), ('4', null, '18801619354', '21232f297a57a5a743894a0e4a801fc3', null, null, null, null, null, null, '1', null, '0', '1', null, '0'), ('5', 'sfds', 'sfds', '0284bb853a649751efbca489e6132b12', '', null, null, null, null, null, '1', null, '0', null, null, '0'), ('9', '收水电费水电费水电费', '所发生的', 'a61e32644afb9dbc12fc2d052f52bf15', '', null, null, null, null, null, '1', null, '0', null, null, '0'), ('10', 'asdfasd', 'asdfasf', '93862c560643de26d7dde4ccc83e50f4', '', null, null, null, null, null, '1', null, '0', null, null, '0'), ('12', 'asdfasdfasdfasasdfasdf', 'asfasdfasdfa', 'a8f30dd65902e03e6b98eb2f97bc1d48', '', null, null, null, null, null, '1', null, '0', null, null, '0'), ('16', 'asdfasdfasasdfasdfassfasdfasdf', 'sadfasdf', 'b3a91fdeeff141eede04fb67686bac76', '', null, null, null, null, null, '1', null, '0', null, null, '0'), ('20', '18801619353', '18801619353', '827ccb0eea8a706c4c34a16891f84e7b', null, null, null, null, null, null, '1', null, '0', null, null, '0'), ('24', '18801619355', '18801619355', '827ccb0eea8a706c4c34a16891f84e7b', null, null, null, null, null, null, '1', null, '0', null, null, '0'), ('25', '188016193512', '188016193512', '827ccb0eea8a706c4c34a16891f84e7b', null, null, null, null, null, null, '1', null, '0', null, null, '0'), ('26', '1812121', '1812121', '827ccb0eea8a706c4c34a16891f84e7b', null, null, null, null, null, null, '1', null, '0', null, null, '0');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
