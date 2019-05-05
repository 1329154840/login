/*
Navicat MySQL Data Transfer

Source Server         : 123.207.136.28_3306
Source Server Version : 80015
Source Host           : 123.207.136.28:3306
Source Database       : system

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2019-05-02 16:37:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
