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


CREATE TABLE `team_member` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `member_id` bigint(20) NOT NULL COMMENT '회원 PK',
  `modified` datetime(6) NOT NULL COMMENT '수정일',
  `modifier` bigint(20) NOT NULL COMMENT '생성 Member PK',
  `team_id` bigint(20) NOT NULL COMMENT '소속 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_TEAM_MEMBER__MEMBER_TEAM` (`member_id`,`team_id`),
  KEY `IDX_TEAM_MEMBER__TEAM` (`team_id`),
  KEY `IDX_TEAM_MEMBER__MEMBER` (`member_id`),
  CONSTRAINT `FK_TEAM_MEMBER__TEAM` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES team_member WRITE;

INSERT INTO 
  team_member (id, member_id, modified, modifier, team_id) 
VALUES 
  ('1', '3', '2023-03-16 13:19:04', '1', '1'), 
  ('2', '4', '2023-03-16 13:19:04', '1', '1'), 
  ('3', '5', '2023-03-16 13:19:04', '1', '1'), 
  ('15578', '15572', '2023-03-16 13:24:32.812000', '15563', '15574'), 
  ('15581', '15579', '2023-03-16 13:25:04.330000', '15563', '15574'), 
  ('15584', '15582', '2023-03-16 13:25:20.636000', '15563', '15574'), 
  ('16292', '16285', '2023-03-20 14:14:43.148000', '16282', '16288'), 
  ('16295', '16293', '2023-03-20 14:14:59.620000', '16282', '16288'), 
  ('16403', '16002', '2023-03-20 14:59:49.848000', '16002', '16042'), 
  ('16405', '15998', '2023-03-20 14:59:55.965000', '16002', '16042'), 
  ('16821', '15995', '2023-03-22 14:56:23.160000', '16002', '16042'), 
  ('16864', '16862', '2023-03-22 15:36:05.784000', '16002', '16042');
UNLOCK TABLES;

