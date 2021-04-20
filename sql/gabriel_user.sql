/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 20/04/2021 10:13:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for banned_list
-- ----------------------------
DROP TABLE IF EXISTS `banned_list`;
CREATE TABLE `banned_list`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT 1,
  `delete_date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of banned_list
-- ----------------------------

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

-- ----------------------------
-- Table structure for user_data
-- ----------------------------
DROP TABLE IF EXISTS `user_data`;
CREATE TABLE `user_data`  (
  `uuid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `register_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `head_pic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `permission` int(1) NOT NULL DEFAULT 9,
  `gender` tinyint(1) NOT NULL DEFAULT 1,
  `get_cover` tinyint(1) NOT NULL DEFAULT 1,
  `hitokoto` tinyint(1) NOT NULL DEFAULT 1,
  `email_bind` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0,
  `nick` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_login_date` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_data
-- ----------------------------
INSERT INTO `user_data` VALUES ('c38dd61e-a422-4eb5-99c9-ce612cda9cc2', 'kyu92', '89c1ff772d3c77f63a26be8bc2d7bf7b', NULL, NULL, '2021-02-21 15:38:22', NULL, NULL, 0, 1, 1, 1, 1, NULL, NULL);

-- ----------------------------
-- View structure for admin_view
-- ----------------------------
DROP VIEW IF EXISTS `admin_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `admin_view` AS select `u`.`uuid` AS `uuid`,`u`.`username` AS `username`,`u`.`mobile` AS `mobile`,`u`.`email` AS `email`,`u`.`register_date` AS `register_date`,`u`.`gender` AS `gender`,`u`.`nick` AS `nick`,`u`.`last_login_date` AS `last_login_date`,if(((select count(0) from `banned_list` where ((`banned_list`.`uuid` = `u`.`uuid`) and (`banned_list`.`end_date` > now()) and (`banned_list`.`delete_flag` <> 1))) > 0),1,0) AS `banned` from `user_data` `u`;

SET FOREIGN_KEY_CHECKS = 1;
