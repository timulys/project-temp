-- --------------------------------------------------------------
-- QueryPie Schema Export Script
-- https://www.querypie.com
--
-- Database: connectbranch
-- Generated At: 04/19/2023 04:55:18 +00:00
-- --------------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `team` (
  `id` bigint(20) NOT NULL COMMENT 'TEAM PK',
  `created` datetime(6) NOT NULL COMMENT '최초 생성일',
  `creator` bigint(20) NOT NULL COMMENT '생성 MEMBER PK',
  `member_count` int(11) DEFAULT '0' COMMENT '그룹 회원 수',
  `modified` datetime(6) NOT NULL COMMENT '수정일',
  `modifier` bigint(20) NOT NULL COMMENT '수정 MEMBER PK',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '상담 그룹 명',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_TEAM__NAME` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES team WRITE;

INSERT INTO 
  team (id, created, creator, member_count, modified, modifier, name) 
VALUES 
  ('1', '2023-03-16 13:19:02', '1', '0', '2023-03-16 13:19:02', '30', '기본 상담 그룹'), 
  ('15574', '2023-03-16 13:24:24.592000', '15563', '0', '2023-03-16 13:24:24.592000', '15563', '판교점 그룹'), 
  ('16042', '2023-03-17 14:35:09.850000', '15998', '0', '2023-03-17 14:35:09.851000', '15998', '상담그룹1'), 
  ('16288', '2023-03-20 14:14:32.494000', '16282', '0', '2023-03-20 14:14:32.494000', '16282', '코드클릭'), 
  ('16395', '2023-03-20 14:54:22.022000', '15998', '0', '2023-03-20 14:54:22.022000', '15998', '그룹1');
UNLOCK TABLES;

