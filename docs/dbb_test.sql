/*
 Navicat Premium Data Transfer

 Source Server         : 本地MySQL
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : dbb_test

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 28/06/2023 21:51:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for logic_test
-- ----------------------------
DROP TABLE IF EXISTS `logic_test`;
CREATE TABLE `logic_test` (
  `id` bigint NOT NULL COMMENT '主键',
  `param_1` varchar(255) DEFAULT '' COMMENT '字段一',
  `param_2` varchar(255) DEFAULT '' COMMENT '字段二',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除，默认0（false）',
  `create_user_id` bigint DEFAULT '0' COMMENT '创建人id',
  `create_user_code` varchar(20) DEFAULT '' COMMENT '创建人编号',
  `create_user_name` varchar(50) DEFAULT '' COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint DEFAULT '0' COMMENT '修改人id',
  `update_user_code` varchar(20) DEFAULT NULL COMMENT '修改人编号',
  `update_user_name` varchar(50) DEFAULT '' COMMENT '修改人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` bigint DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of logic_test
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
