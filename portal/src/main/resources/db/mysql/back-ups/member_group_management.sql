-- --------------------------------------------------------------
-- QueryPie Schema Export Script
-- https://www.querypie.com
--
-- Database: connectbranch
-- Generated At: 04/19/2023 04:55:17 +00:00
-- --------------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `member_group_management` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `created` datetime(6) DEFAULT NULL COMMENT '생성 일시',
  `enable` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '사용 여부',
  `member_id` bigint(20) DEFAULT NULL COMMENT '유저 PK',
  `member_group_id` bigint(20) DEFAULT NULL COMMENT '유저-그룹 매칭 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_MEMBER_GROUP_MANAGEMENT` (`member_group_id`,`member_id`),
  KEY `FK_MEMBER_GROUP_MANAGEMENT__MEMBER` (`member_id`),
  CONSTRAINT `FK_MEMBER_GROUP_MANAGEMENT__MEMBER` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
  CONSTRAINT `FK_MEMBER_GROUP_MANAGEMENT__MEMBER_GROUP` FOREIGN KEY (`member_group_id`) REFERENCES `member_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES member_group_management WRITE;

UNLOCK TABLES;

