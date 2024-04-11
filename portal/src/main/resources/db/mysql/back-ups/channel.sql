-- --------------------------------------------------------------
-- QueryPie Schema Export Script
-- https://www.querypie.com
--
-- Database: connectbranch
-- Generated At: 04/19/2023 04:55:16 +00:00
-- --------------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `channel` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `branch_id` bigint(20) DEFAULT NULL COMMENT '대표 브랜치',
  `modified` datetime(6) DEFAULT NULL COMMENT '수정 일시',
  `modifier` bigint(20) NOT NULL COMMENT '수정자',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이름',
  `platform` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '플랫폼',
  `service_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '서비스 아이디',
  `service_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '서비스 키',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_CHANNEL__PLATFORM` (`platform`,`service_key`),
  KEY `IDX_CHANNEL__BRANCH` (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES channel WRITE;

INSERT INTO channel (id, branch_id, modified, modifier, name, platform, service_id, service_key) VALUES ('1', '1', '2023-03-16 13:19:01', '1', '기본 채널', 'kakao_counsel_talk', '@mtgy7lukmza1oou', 'b209820559c7542b49a0ec3d8b9e746e9297d77e');
UNLOCK TABLES;

