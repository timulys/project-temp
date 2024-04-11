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


CREATE TABLE `branch_channel` (
  `id` bigint(20) NOT NULL,
  `created` datetime(6) NOT NULL COMMENT '생성일',
  `creator` bigint(20) NOT NULL COMMENT '생성 회원 PK',
  `owned` varchar(1) COLLATE utf8mb4_bin DEFAULT 'N' COMMENT '메인 브랜치 여부',
  `branch_id` bigint(20) NOT NULL COMMENT '브랜치 PK',
  `channel_id` bigint(20) NOT NULL COMMENT '채널 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_BRANCH_CHANNEL` (`branch_id`,`channel_id`),
  KEY `IDX_BRANCH_CHANNEL__BRANCH` (`branch_id`),
  KEY `IDX_BRANCH_CHANNEL__CHANNEL` (`channel_id`,`owned`),
  CONSTRAINT `FK_BRANCH_CHANNEL__BRANCH` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`),
  CONSTRAINT `FK_CHANNEL_BRANCH__CHANNEL` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES branch_channel WRITE;

INSERT INTO 
  branch_channel (id, created, creator, owned, branch_id, channel_id) 
VALUES 
  ('1', '2023-03-16 13:19:01', '1', 'Y', '1', '1'), 
  ('15562', '2023-03-16 13:21:07.365000', '1', 'N', '15556', '1'), 
  ('15992', '2023-03-17 13:52:37.606000', '1', 'N', '15986', '1'), 
  ('16281', '2023-03-20 14:12:31.291000', '1', 'N', '16275', '1');
UNLOCK TABLES;

