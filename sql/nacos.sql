/*
 Navicat Premium Data Transfer

 Source Server         : kyu
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : kyu92.top:3306
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 20/04/2021 10:07:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 265 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (95, 'BookServiceProvider-dev.yaml', 'DEFAULT_GROUP', 'spring:\n  banner:\n    charset: UTF-8', '81c4595019498eeb54c222756ea82506', '2021-02-17 02:56:52', '2021-04-19 14:17:02', NULL, '0:0:0:0:0:0:0:1', '', '', '书籍服务生产者配置', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (96, 'BookServiceConsumer-dev.yaml', 'DEFAULT_GROUP', 'spring:\n  servlet:\n    multipart:\n      max-file-size: 50MB\n      max-request-size: 50MB\n\nfeign:\n  okhttp:\n    enabled: true\n\n#ftp:\n  #host: 127.0.0.1 \n  # host: 218.93.206.10\n  #port: 21\n  #username: kyu\n  # username: administrator\n  #password: guan123123/\n  # password: 928495870irisu!@\n  #basePath: \n  #encoding: UTF-8', '64b8f0ffb8f54c649dadc5052ff50391', '2021-02-17 03:12:04', '2021-03-02 04:59:51', NULL, '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (100, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2021-02-17 08:08:32', '2021-02-17 08:08:32', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (101, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2021-02-17 08:08:32', '2021-02-17 08:08:32', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (102, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-02-17 08:08:32', '2021-02-17 08:08:32', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (103, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:33', '2021-02-17 08:08:33', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (104, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2021-02-17 08:08:33', '2021-02-17 08:08:33', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (105, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2021-02-17 08:08:33', '2021-02-17 08:08:33', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (106, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2021-02-17 08:08:34', '2021-02-17 08:08:34', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (107, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:34', '2021-02-17 08:08:34', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (108, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2021-02-17 08:08:34', '2021-02-17 08:08:34', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (109, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2021-02-17 08:08:35', '2021-02-17 08:08:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (110, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2021-02-17 08:08:35', '2021-02-17 08:08:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (111, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2021-02-17 08:08:35', '2021-02-17 08:08:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (112, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2021-02-17 08:08:35', '2021-02-17 08:08:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (113, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2021-02-17 08:08:36', '2021-02-17 08:08:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (114, 'service.vgroupMapping.ManagerServiceGroup', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2021-02-17 08:08:36', '2021-02-17 08:08:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (115, 'service.default.grouplist', 'SEATA_GROUP', '127.0.0.1:8091', 'c32ce0d3e264525dcdada751f98143a3', '2021-02-17 08:08:36', '2021-02-17 08:08:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (116, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:37', '2021-02-17 08:08:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (117, 'service.disableGlobalTransaction', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:37', '2021-02-17 08:08:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (118, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b', '2021-02-17 08:08:37', '2021-02-17 08:08:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (119, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2021-02-17 08:08:38', '2021-02-17 08:08:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (120, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2021-02-17 08:08:38', '2021-02-17 08:08:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (121, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-02-17 08:08:38', '2021-02-17 08:08:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (122, 'client.rm.reportRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-02-17 08:08:39', '2021-02-17 08:08:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (123, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:39', '2021-02-17 08:08:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (124, 'client.rm.sqlParserType', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2021-02-17 08:08:39', '2021-02-17 08:08:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (125, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:39', '2021-02-17 08:08:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (126, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:40', '2021-02-17 08:08:40', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (127, 'client.tm.commitRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-02-17 08:08:40', '2021-02-17 08:08:40', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (128, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-02-17 08:08:40', '2021-02-17 08:08:40', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (129, 'client.tm.defaultGlobalTransactionTimeout', 'SEATA_GROUP', '60000', '2b4226dd7ed6eb2d419b881f3ae9c97c', '2021-02-17 08:08:41', '2021-02-17 08:08:41', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (130, 'client.tm.degradeCheck', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:41', '2021-02-17 08:08:41', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (131, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2021-02-17 08:08:41', '2021-02-17 08:08:41', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (132, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '2000', '08f90c1a417155361a5c4b8d297e0d78', '2021-02-17 08:08:41', '2021-02-17 08:08:41', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (133, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2021-02-17 08:08:42', '2021-02-17 08:08:42', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (134, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2021-02-17 08:08:42', '2021-02-17 08:08:42', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (135, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2021-02-17 08:08:42', '2021-02-17 08:08:42', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (136, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.cj.jdbc.Driver', '33763409bb7f4838bde4fae9540433e4', '2021-02-17 08:08:43', '2021-02-17 08:08:43', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (137, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://kyu92.top:3306/seata?useUnicode=true', '5d9892e11c269af0a74d6cc2b9b93f28', '2021-02-17 08:08:43', '2021-02-17 08:08:43', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (138, 'store.db.user', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2021-02-17 08:08:43', '2021-02-17 08:08:43', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (139, 'store.db.password', 'SEATA_GROUP', 'guan123123/', '0473074d91018c09e4dd52a5f43f4ecc', '2021-02-17 08:08:43', '2021-02-17 08:08:43', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (140, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2021-02-17 08:08:44', '2021-02-17 08:08:44', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (141, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2021-02-17 08:08:44', '2021-02-17 08:08:44', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (142, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2021-02-17 08:08:44', '2021-02-17 08:08:44', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (143, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2021-02-17 08:08:45', '2021-02-17 08:08:45', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (144, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-02-17 08:08:45', '2021-02-17 08:08:45', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (145, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2021-02-17 08:08:45', '2021-02-17 08:08:45', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (146, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2021-02-17 08:08:46', '2021-02-17 08:08:46', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (147, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2021-02-17 08:08:46', '2021-02-17 08:08:46', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (148, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2021-02-17 08:08:46', '2021-02-17 08:08:46', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (149, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2021-02-17 08:08:46', '2021-02-17 08:08:46', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (150, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2021-02-17 08:08:47', '2021-02-17 08:08:47', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (151, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2021-02-17 08:08:47', '2021-02-17 08:08:47', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (152, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2021-02-17 08:08:47', '2021-02-17 08:08:47', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (153, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:48', '2021-02-17 08:08:48', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (154, 'client.undo.dataValidation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-02-17 08:08:48', '2021-02-17 08:08:48', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (155, 'client.undo.logSerialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2021-02-17 08:08:48', '2021-02-17 08:08:48', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (156, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2021-02-17 08:08:49', '2021-02-17 08:08:49', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (157, 'server.undo.logSaveDays', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2021-02-17 08:08:49', '2021-02-17 08:08:49', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (158, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2021-02-17 08:08:49', '2021-02-17 08:08:49', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (159, 'client.undo.logTable', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614', '2021-02-17 08:08:49', '2021-02-17 08:08:49', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (160, 'client.log.exceptionRate', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2021-02-17 08:08:50', '2021-02-17 08:08:50', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (161, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2021-02-17 08:08:50', '2021-02-17 08:08:50', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (162, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2021-02-17 08:08:50', '2021-02-17 08:08:50', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (163, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2021-02-17 08:08:51', '2021-02-17 08:08:51', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (164, 'metrics.registryType', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465', '2021-02-17 08:08:51', '2021-02-17 08:08:51', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (165, 'metrics.exporterList', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2021-02-17 08:08:51', '2021-02-17 08:08:51', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (166, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2021-02-17 08:08:52', '2021-02-17 08:08:52', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (167, 'ManagerServiceConsumer-dev.yaml', 'DEFAULT_GROUP', 'feign:\n  okhttp:\n    enabled: true', 'ccff2b7d02720b3e716288df3770fdce', '2021-02-17 08:16:40', '2021-04-19 12:55:06', NULL, '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (171, 'UserServiceProvider-dev.yaml', 'DEFAULT_GROUP', 'spring:\n  banner:\n    charset: UTF-8', '81c4595019498eeb54c222756ea82506', '2021-02-17 09:03:46', '2021-04-19 14:17:27', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (178, 'UserServiceConsumer-dev.yaml', 'DEFAULT_GROUP', 'spring: \n  mail: \n    host: smtp.mxhichina.com\n    port: 25\n    username: postmaster@kyu92.top\n    password: Anzu928495870/', '3cedd28a4c361dd2e85822b51487b583', '2021-02-21 15:07:09', '2021-04-19 14:17:51', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (216, 'mybatis-plus.yaml', 'DEFAULT_GROUP', 'mybatis-plus:\r\n  type-aliases-package: org.spring.springboot.domain\r\n  mapper-locations: classpath:mapper/**/*.xml\r\n  global-config:\r\n    banner: false\r\n  configuration:\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl', '939c594570cb35b4d41caa70529eebae', '2021-03-02 04:52:26', '2021-03-02 04:52:26', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (218, 'seata.yaml', 'DEFAULT_GROUP', 'seata:\n  enabled: true\n  application-id: GabrielCloud\n  tx-service-group: ManagerServiceGroup\n  enable-auto-data-source-proxy: true\n  config:\n    nacos:\n      server-addr: localhost:81\n      group: SEATA_GROUP\n      username: nacos\n      password: nacos\n  registry:\n    nacos:\n      application: seata-server\n      server-addr: 127.0.0.1:81\n      userName: nacos\n      password: nacos\n  service:\n    vgroup-mapping:\n      ManagerServiceGroup: default\n    disable-global-transaction: false\n  client:\n    rm:\n      report-success-enable: false', '562bdbdc7af58165e88d5dff10a1dd56', '2021-03-02 04:52:52', '2021-04-19 12:47:56', NULL, '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (221, 'redis.yaml', 'DEFAULT_GROUP', 'spring:\n  redis:\n    database: 0\n    password: Anzu928495870/\n    sentinel:\n      master: sen\n      nodes: 112.126.67.3:26379,112.126.67.3:26380,112.126.67.3:26381', '71df6aa7c71bd2bf02826821a4c4c87e', '2021-03-02 04:55:49', '2021-03-03 07:31:45', NULL, '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (234, 'datasource-user.yaml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:mysql://kyu92.top/gabriel_user?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\n    username: root\n    password: Anzu928495870/\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      max-lifetime: 1800', '01ff26f66219d7801e41ceb1cd26326c', '2021-03-03 07:48:31', '2021-03-22 14:21:34', NULL, '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (235, 'datasource-book.yaml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:mysql://kyu92.top/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\n    username: root\n    password: Anzu928495870/\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      max-lifetime: 1800', '695b78f23ee61d21334675200eb1c1b2', '2021-03-03 07:48:56', '2021-03-22 14:21:47', NULL, '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (242, 'ManagerServiceProvider-dev.yaml', 'DEFAULT_GROUP', 'spring:\n  banner:\n    charset: UTF-8', '81c4595019498eeb54c222756ea82506', '2021-03-26 01:32:49', '2021-04-19 14:18:11', NULL, '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (243, 'datasource-manager.yaml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:mysql://kyu92.top/gabriel_manager?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\n    username: root\n    password: Anzu928495870/\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      max-lifetime: 1800', '29870b1933e03ec88d4ac8fda2874c19', '2021-03-26 01:48:37', '2021-03-26 05:35:51', NULL, '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (251, 'logging.yaml', 'DEFAULT_GROUP', 'logging:\n  level:\n    root: info\n    com:\n      baomidou: warn\n  pattern:\n#    console: \'%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n\'\n    file: \'%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger- %msg%n\'\n  file:\n    max-history: 30\n    name: logs/${spring.application.name}_log.log\n    clean-history-on-start: false', 'c424e809ad09cb48680451acac40dca6', '2021-04-19 10:37:17', '2021-04-19 15:02:53', NULL, '0:0:0:0:0:0:0:1', '', '', 'log配置', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (253, 'ApiGateway-dev.yaml', 'DEFAULT_GROUP', 'spring:\r\n  banner:\r\n    charset: UTF-8', '5d4a5754e1df46c09c8413b18e2e0f91', '2021-04-19 10:58:56', '2021-04-19 10:58:56', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (254, 'sentinel.yaml', 'DEFAULT_GROUP', 'spring:\r\n  cloud:\r\n    sentinel:\r\n      transport:\r\n        dashboard: localhost:8080', '15669f5f44875700e8db644e3363f399', '2021-04-19 12:44:24', '2021-04-19 12:44:24', NULL, '0:0:0:0:0:0:0:1', '', '', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (259, 'actuator.yaml', 'DEFAULT_GROUP', 'management:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\'', '1881379326fe3835a39dc4848550ef40', '2021-04-19 14:13:42', '2021-04-19 14:13:42', NULL, '0:0:0:0:0:0:0:1', '', '', NULL, NULL, NULL, 'yaml', NULL);

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(64) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 363 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (224, 330, 'jwt.yaml', 'DEFAULT_GROUP', '', 'jwt:\r\n  expires: 86400\r\n  secret: 0o3L2U6tLByX42YjYnN25EMb8Piq1dKW', '4bf5b63ccd3dc9ef06cbbcce536922ea', '2021-03-21 11:09:31', '2021-03-21 03:09:28', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (235, 331, 'datasource-book.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n    url: jdbc:mysql://kyu92.top/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\r\n    username: root\r\n    password: Anzu928495870/\r\n    type: com.zaxxer.hikari.HikariDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    hikari:\r\n      max-lifetime: 1800', 'cd7970b7ac7b25627865e4bbb749989c', '2021-03-22 22:15:07', '2021-03-22 14:15:05', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (234, 332, 'datasource-user.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n    url: jdbc:mysql://kyu92.top/gabriel_user?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\r\n    username: root\r\n    password: Anzu928495870/\r\n    type: com.zaxxer.hikari.HikariDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    hikari:\r\n      max-lifetime: 1800', '23fc8f9d4865ec14b0a209cc8c7cb4a9', '2021-03-22 22:15:39', '2021-03-22 14:15:37', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (234, 333, 'datasource-user.yaml', 'DEFAULT_GROUP', '', 'spring:\n  datasource:\n    url: jdbc:mysql://kyu92.top/gabriel_user?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\n    username: root\n    password: Anzu928495870/\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      max-lifetime: 1800', 'df31ff7b479784aa17d176d9c06990d6', '2021-03-22 22:21:36', '2021-03-22 14:21:34', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (235, 334, 'datasource-book.yaml', 'DEFAULT_GROUP', '', 'spring:\n  datasource:\n    url: jdbc:mysql://kyu92.top/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\n    username: root\n    password: Anzu928495870/\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      max-lifetime: 1800', '89b29990482d0cf734414c09218c527d', '2021-03-22 22:21:49', '2021-03-22 14:21:47', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 335, 'datasouce-manager.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n    url: jdbc:mysql://kyu92.top/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\r\n    username: root\r\n    password: Anzu928495870/\r\n    type: com.zaxxer.hikari.HikariDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    hikari:\r\n      max-lifetime: 1800', 'cd7970b7ac7b25627865e4bbb749989c', '2021-03-26 09:30:08', '2021-03-26 01:30:07', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 336, 'ManagerServiceProvider-dev.yaml', 'DEFAULT_GROUP', '', 'management:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\'', '1881379326fe3835a39dc4848550ef40', '2021-03-26 09:32:50', '2021-03-26 01:32:49', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (241, 337, 'datasouce-manager.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n    url: jdbc:mysql://kyu92.top/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\r\n    username: root\r\n    password: Anzu928495870/\r\n    type: com.zaxxer.hikari.HikariDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    hikari:\r\n      max-lifetime: 1800', 'cd7970b7ac7b25627865e4bbb749989c', '2021-03-26 09:48:24', '2021-03-26 01:48:23', NULL, '127.0.0.1', 'D', '');
INSERT INTO `his_config_info` VALUES (0, 338, 'datasource-manager.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n    url: jdbc:mysql://kyu92.top/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\r\n    username: root\r\n    password: Anzu928495870/\r\n    type: com.zaxxer.hikari.HikariDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    hikari:\r\n      max-lifetime: 1800', 'cd7970b7ac7b25627865e4bbb749989c', '2021-03-26 09:48:38', '2021-03-26 01:48:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (243, 339, 'datasource-manager.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n    url: jdbc:mysql://kyu92.top/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\r\n    username: root\r\n    password: Anzu928495870/\r\n    type: com.zaxxer.hikari.HikariDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    hikari:\r\n      max-lifetime: 1800', 'cd7970b7ac7b25627865e4bbb749989c', '2021-03-26 10:14:31', '2021-03-26 02:14:29', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (243, 340, 'datasource-manager.yaml', 'DEFAULT_GROUP', '', 'spring:\n  datasource:\n    url: jdbc:mysql://kyu92.top/gabriel_book?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false\n    username: root\n    password: Anzu928495870/\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      max-lifetime: 1800', '695b78f23ee61d21334675200eb1c1b2', '2021-03-26 13:35:52', '2021-03-26 05:35:51', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 341, 'tencent-location-config.yaml', 'DEFAULT_GROUP', '', 'token: OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77', 'c38e3ce8f28d0f1a6e1a8df3313728b8', '2021-03-26 14:10:56', '2021-03-26 06:10:55', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (246, 342, 'tencent-location-config.yaml', 'DEFAULT_GROUP', '', 'token: OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77', 'c38e3ce8f28d0f1a6e1a8df3313728b8', '2021-03-26 14:12:12', '2021-03-26 06:12:10', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (246, 343, 'tencent-location-config.yaml', 'DEFAULT_GROUP', '', 'tencent:\n  location:\n    webService:\n      token: OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77', '82380521ca467050dfd21101636589bc', '2021-03-26 15:19:08', '2021-03-26 07:19:06', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (246, 344, 'tencent-location-config.yaml', 'DEFAULT_GROUP', '', 'tencent:\n  location:\n    webService:\n      token: OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77\n      secret: TuUFfh5mcaZ4zEnMNBdBSAT6UhPtv5Ss', 'eb54b71f00d93fb9c626b423fff5bc6c', '2021-03-26 15:41:11', '2021-03-26 07:41:10', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (242, 345, 'ManagerServiceProvider-dev.yaml', 'DEFAULT_GROUP', '', 'management:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\'', '1881379326fe3835a39dc4848550ef40', '2021-03-26 15:50:57', '2021-03-26 07:50:56', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 346, 'logging.yaml', 'DEFAULT_GROUP', '', 'logging:\r\n  level:\r\n    root: info\r\n    com:\r\n      baomidou: warn\r\n  pattern:\r\n#    console: \'%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n\'\r\n    file: \'%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger- %msg%n\'\r\n  file:\r\n    max-history: 30\r\n    name: logs/log.log\r\n    clean-history-on-start: true', '27a064869179fa3bd1f9503e5ced147b', '2021-04-19 18:37:18', '2021-04-19 10:37:17', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (251, 347, 'logging.yaml', 'DEFAULT_GROUP', '', 'logging:\r\n  level:\r\n    root: info\r\n    com:\r\n      baomidou: warn\r\n  pattern:\r\n#    console: \'%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n\'\r\n    file: \'%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger- %msg%n\'\r\n  file:\r\n    max-history: 30\r\n    name: logs/log.log\r\n    clean-history-on-start: true', '27a064869179fa3bd1f9503e5ced147b', '2021-04-19 18:42:09', '2021-04-19 10:42:07', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 348, 'ApiGateway-dev.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  banner:\r\n    charset: UTF-8', '5d4a5754e1df46c09c8413b18e2e0f91', '2021-04-19 18:58:57', '2021-04-19 10:58:56', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 349, 'sentinel.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  cloud:\r\n    sentinel:\r\n      transport:\r\n        dashboard: localhost:8080', '15669f5f44875700e8db644e3363f399', '2021-04-19 20:44:26', '2021-04-19 12:44:24', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (246, 350, 'tencent-location-config.yaml', 'DEFAULT_GROUP', '', 'tencent:\n  location:\n    webService:\n      token: 7A3BZ-EQNCJ-2HEF7-FWFGG-Y2JIE-2TBLO\n      secret: TuUFfh5mcaZ4zEnMNBdBSAT6UhPtv5Ss', 'eee0dbb06344a25b5ee4e08f99a8c405', '2021-04-19 20:46:58', '2021-04-19 12:46:57', NULL, '127.0.0.1', 'D', '');
INSERT INTO `his_config_info` VALUES (218, 351, 'seata.yaml', 'DEFAULT_GROUP', '', 'seata:\r\n  enabled: true\r\n  application-id: BookService\r\n  tx-service-group: ManagerServiceGroup\r\n  enable-auto-data-source-proxy: true\r\n  config:\r\n    nacos:\r\n      server-addr: localhost:81\r\n      group: SEATA_GROUP\r\n      username: nacos\r\n      password: nacos\r\n  registry:\r\n    nacos:\r\n      application: seata-server\r\n      server-addr: 127.0.0.1:8848\r\n      userName: nacos\r\n      password: nacos\r\n  service:\r\n    vgroup-mapping:\r\n      ManagerServiceGroup: default\r\n    disable-global-transaction: false\r\n  client:\r\n    rm:\r\n      report-success-enable: false', '83c9d0a757cd22a83da7442a1662fea8', '2021-04-19 20:47:57', '2021-04-19 12:47:56', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (167, 352, 'ManagerServiceConsumer-dev.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  application-id: ManagerService\n  tx-service-group: ManagerServiceGroup\n  enable-auto-data-source-proxy: true\n  config:\n    nacos:\n      server-addr: localhost:81\n      group: SEATA_GROUP\n      username: nacos\n      password: nacos\n  registry:\n    nacos:\n      application: seata-server\n      server-addr: 127.0.0.1:8848\n      userName: nacos\n      password: nacos\n  service:\n    vgroup-mapping:\n      ManagerServiceGroup: default\n    disable-global-transaction: false\n  client:\n    rm:\n      report-success-enable: false', '1c48697e1bc4557f13e4ec4c53e4c043', '2021-04-19 20:55:07', '2021-04-19 12:55:06', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (242, 353, 'ManagerServiceProvider-dev.yaml', 'DEFAULT_GROUP', '', 'management:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n\ntencent:\n  location:\n    webService:\n      use: true', '68e4f9284ce90120aa4bb1a87501d3c7', '2021-04-19 20:55:34', '2021-04-19 12:55:33', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (178, 354, 'UserServiceConsumer-dev.yaml', 'DEFAULT_GROUP', '', 'spring: \n  mail: \n    host: smtp.mxhichina.com\n    port: 25\n    username: postmaster@kyu92.top\n    password: Anzu928495870/', '3cedd28a4c361dd2e85822b51487b583', '2021-04-19 22:11:53', '2021-04-19 14:11:51', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 355, 'actuator.yaml', 'DEFAULT_GROUP', '', 'management:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\'', '1881379326fe3835a39dc4848550ef40', '2021-04-19 22:13:44', '2021-04-19 14:13:42', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (95, 356, 'BookServiceProvider-dev.yaml', 'DEFAULT_GROUP', '', 'management:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'', '682c17a9f9fc46633825f6675e264304', '2021-04-19 22:17:04', '2021-04-19 14:17:02', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (171, 357, 'UserServiceProvider-dev.yaml', 'DEFAULT_GROUP', '', 'management:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'', '682c17a9f9fc46633825f6675e264304', '2021-04-19 22:17:29', '2021-04-19 14:17:27', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (178, 358, 'UserServiceConsumer-dev.yaml', 'DEFAULT_GROUP', '', 'management:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n        \nspring: \n  mail: \n    host: smtp.mxhichina.com\n    port: 25\n    username: postmaster@kyu92.top\n    password: Anzu928495870/', '6e82c6ba2d36157eeec2abba4ce0c864', '2021-04-19 22:17:52', '2021-04-19 14:17:51', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (242, 359, 'ManagerServiceProvider-dev.yaml', 'DEFAULT_GROUP', '', 'management:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'', '682c17a9f9fc46633825f6675e264304', '2021-04-19 22:18:13', '2021-04-19 14:18:11', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (224, 360, 'jwt.yaml', 'DEFAULT_GROUP', '', 'jwt:\n  expires: 604800\n  secret: 0o3L2U6tLByX42YjYnN25EMb8Piq1dKW', '6f03772eeacfd90a07787ce4ea0cc499', '2021-04-19 22:40:18', '2021-04-19 14:40:16', NULL, '0:0:0:0:0:0:0:1', 'D', '');
INSERT INTO `his_config_info` VALUES (227, 361, 'minio.yaml', 'DEFAULT_GROUP', '', 'minio:\r\n  endpoint: http://127.0.0.1:9000\r\n  accessKey: minio\r\n  secretKey: kyu92.top', 'd2f516fb3fddc4350a03d081a1332f5b', '2021-04-19 22:41:08', '2021-04-19 14:41:06', NULL, '0:0:0:0:0:0:0:1', 'D', '');
INSERT INTO `his_config_info` VALUES (251, 362, 'logging.yaml', 'DEFAULT_GROUP', '', 'logging:\n  level:\n    root: info\n    com:\n      baomidou: warn\n  pattern:\n#    console: \'%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n\'\n    file: \'%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger- %msg%n\'\n  file:\n    max-history: 30\n    name: logs/${spring.application.name}_log.log\n    clean-history-on-start: true', '62d0c0b66b0d546b34ee5daf923bc4ec', '2021-04-19 23:02:54', '2021-04-19 15:02:53', NULL, '0:0:0:0:0:0:0:1', 'U', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
