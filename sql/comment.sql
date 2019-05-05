/*
Navicat MySQL Data Transfer

Source Server         : 123.207.136.28_3306
Source Server Version : 80015
Source Host           : 123.207.136.28:3306
Source Database       : system

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2019-05-02 16:31:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
