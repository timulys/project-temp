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


CREATE TABLE `site` (
  `id` int(11) NOT NULL COMMENT 'PK',
  `branch_count` int(11) DEFAULT '0' COMMENT '브랜치 카운트',
  `channel_count` int(11) DEFAULT '0' COMMENT '채널 카운트',
  `member_count` int(11) DEFAULT '0' COMMENT '유저 카운트',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이름',
  `team_count` int(11) DEFAULT '0' COMMENT '상담그룹 카운트',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES site WRITE;

INSERT INTO site (id, branch_count, channel_count, member_count, name, team_count) VALUES ('1', '0', '0', '12', '커넥트 브랜치', '0');
UNLOCK TABLES;

