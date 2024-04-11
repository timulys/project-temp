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


CREATE TABLE `branch_team` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `created` datetime(6) NOT NULL COMMENT '생성 일시',
  `creator` bigint(20) NOT NULL COMMENT '생성자',
  `branch_id` bigint(20) NOT NULL,
  `member_id` bigint(20) NOT NULL COMMENT '그룹장 PK',
  `team_id` bigint(20) NOT NULL COMMENT '소속 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_BRANCH_TEAM__BRANCH` (`branch_id`,`team_id`),
  KEY `IDX_BRANCH_TEAM__BRANCH` (`branch_id`),
  KEY `FK_BRANCH_TEAM__MEMBER` (`member_id`),
  KEY `FK_BRANCH_TEAM__TEAM` (`team_id`),
  CONSTRAINT `FK_BRANCH_TEAM__BRANCH` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
  CONSTRAINT `FK_BRANCH_TEAM__MEMBER` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
  CONSTRAINT `FK_BRANCH_TEAM__TEAM` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES branch_team WRITE;

INSERT INTO 
  branch_team (id, created, creator, branch_id, member_id, team_id) 
VALUES 
  ('1', '2023-03-16 13:19:04', '1', '1', '3', '1'), 
  ('15575', '2023-03-16 13:24:24.592000', '15563', '15556', '15572', '15574'), 
  ('16043', '2023-03-17 14:35:09.851000', '15998', '15986', '15998', '16042'), 
  ('16289', '2023-03-20 14:14:32.494000', '16282', '16275', '16285', '16288'), 
  ('16396', '2023-03-20 14:54:22.022000', '15998', '15986', '15998', '16395');
UNLOCK TABLES;

