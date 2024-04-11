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


CREATE TABLE `issue_category` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `channel_id` bigint(20) NOT NULL COMMENT '채널 PK',
  `depth` int(11) NOT NULL COMMENT '계층',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
  `exposed` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '전체 오픈 여부',
  `modified` datetime(6) NOT NULL COMMENT '수정 일시',
  `modifier` bigint(20) NOT NULL COMMENT '수정자',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이름',
  `sort` int(11) NOT NULL COMMENT '정렬',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '상위 분류 PK',
  PRIMARY KEY (`id`),
  KEY `IDX_ISSUE_CATEGORY__SEARCH` (`channel_id`,`enabled`,`depth`,`name`),
  KEY `IDX_ISSUE_CATEGORY__PARENT` (`channel_id`,`parent_id`,`enabled`),
  KEY `FK_ISSUE_CATEGORY__PARENT` (`parent_id`),
  CONSTRAINT `FK_ISSUE_CATEGORY__PARENT` FOREIGN KEY (`parent_id`) REFERENCES `issue_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES issue_category WRITE;

INSERT INTO 
  issue_category (id, channel_id, depth, enabled, exposed, modified, modifier, name, sort, parent_id) 
VALUES 
  ('1', '1', '1', 'Y', 'Y', '2023-03-16 13:19:08', '1', '대분류1', '1', NULL), 
  ('2', '1', '1', 'Y', 'Y', '2023-03-16 13:19:08', '1', '대분류2', '2', NULL), 
  ('3', '1', '1', 'Y', 'Y', '2023-03-16 13:19:08', '1', '대분류3', '3', NULL), 
  ('4', '1', '2', 'Y', 'Y', '2023-03-16 13:19:08', '1', '중분류11', '1', '1'), 
  ('5', '1', '2', 'Y', 'Y', '2023-03-16 13:19:09', '1', '중분류12', '2', '1'), 
  ('6', '1', '2', 'Y', 'Y', '2023-03-16 13:19:09', '1', '중분류13', '3', '1'), 
  ('7', '1', '2', 'Y', 'Y', '2023-03-16 13:19:09', '1', '중분류21', '1', '2'), 
  ('8', '1', '2', 'Y', 'Y', '2023-03-16 13:19:09', '1', '중분류22', '2', '2'), 
  ('9', '1', '2', 'Y', 'Y', '2023-03-16 13:19:09', '1', '중분류23', '3', '2'), 
  ('10', '1', '3', 'Y', 'Y', '2023-03-16 13:19:09', '1', '소분류221', '1', '8'), 
  ('11', '1', '3', 'Y', 'Y', '2023-03-16 13:19:09', '1', '소분류222', '2', '8'), 
  ('12', '1', '3', 'Y', 'Y', '2023-03-16 13:19:10', '1', '소분류231', '3', '9'), 
  ('13', '1', '2', 'Y', 'Y', '2023-03-16 13:19:10', '1', '중분류31', '1', '3'), 
  ('14', '1', '2', 'Y', 'Y', '2023-03-16 13:19:10', '1', '중분류32', '2', '3'), 
  ('21', '1', '3', 'Y', 'Y', '2023-03-16 13:19:10', '1', '소분류211', '1', '7'), 
  ('22', '1', '3', 'Y', 'Y', '2023-03-16 13:19:11', '1', '소분류212', '2', '7'), 
  ('23', '1', '3', 'Y', 'Y', '2023-03-16 13:19:11', '1', '소분류232', '1', '9'), 
  ('24', '1', '3', 'Y', 'Y', '2023-03-16 13:19:11', '1', '소분류311', '2', '13'), 
  ('31', '1', '3', 'Y', 'Y', '2023-03-16 13:19:10', '1', '소분류312', '1', '13'), 
  ('32', '1', '3', 'Y', 'Y', '2023-03-16 13:19:10', '1', '소분류321', '2', '14'), 
  ('33', '1', '3', 'Y', 'Y', '2023-03-16 13:19:10', '1', '소분류322', '1', '14'), 
  ('34', '1', '3', 'Y', 'Y', '2023-03-16 13:19:10', '1', '소분류323', '2', '14'), 
  ('35', '1', '3', 'Y', 'Y', '2023-03-16 13:19:11', '1', '소분류111', '3', '4'), 
  ('36', '1', '3', 'Y', 'Y', '2023-03-16 13:19:11', '1', '소분류112', '3', '4'), 
  ('37', '1', '3', 'Y', 'Y', '2023-03-16 13:19:11', '1', '소분류121', '3', '5'), 
  ('38', '1', '3', 'Y', 'Y', '2023-03-16 13:19:11', '1', '소분류122', '3', '5'), 
  ('39', '1', '3', 'Y', 'Y', '2023-03-16 13:19:11', '1', '소분류131', '3', '6'), 
  ('40', '1', '3', 'Y', 'Y', '2023-03-16 13:19:12', '1', '소분류132', '3', '6');
UNLOCK TABLES;

