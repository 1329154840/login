/*
Navicat MySQL Data Transfer

Source Server         : 123.207.136.28_3306
Source Server Version : 80015
Source Host           : 123.207.136.28:3306
Source Database       : system

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2019-05-02 16:36:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for travel_notes
-- ----------------------------
DROP TABLE IF EXISTS `travel_notes`;
CREATE TABLE `travel_notes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `textname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `text` text COLLATE utf8_unicode_ci,
  `view_num` int(11) DEFAULT '0',
  `comment_num` int(11) DEFAULT '0',
  `poll_num` int(11) DEFAULT '0',
  `zhaiyao` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
