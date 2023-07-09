/*
 Navicat Premium Data Transfer

 Source Server         : 本地MySQL
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : dbb_demo

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 28/06/2023 21:50:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for create_test
-- ----------------------------
DROP TABLE IF EXISTS `create_test`;
CREATE TABLE `create_test` (
  `id` bigint NOT NULL COMMENT '主键',
  `remark` varchar(20) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT NULL COMMENT '是否删除，默认0（false）',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_user_code` varchar(20) DEFAULT NULL COMMENT '创建人编号',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人id',
  `update_user_code` varchar(20) DEFAULT NULL COMMENT '修改人编号',
  `update_user_name` varchar(50) DEFAULT NULL COMMENT '修改人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int DEFAULT NULL COMMENT '版本号，乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `ix_create_test_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of create_test
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for do_test
-- ----------------------------
DROP TABLE IF EXISTS `do_test`;
CREATE TABLE `do_test` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remark` text COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除，默认0（false）',
  `create_user_id` bigint DEFAULT '0' COMMENT '创建人id',
  `create_user_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '创建人编号',
  `create_user_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint DEFAULT '0' COMMENT '修改人id',
  `update_user_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人编号',
  `update_user_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '修改人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` bigint DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of do_test
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='AT transaction mode undo table';

-- ----------------------------
-- Records of undo_log
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
