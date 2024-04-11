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


CREATE TABLE `levels` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '레벨 이름',
  `type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '레벨 타입',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_LEVEL__TYPE` (`type`),
  UNIQUE KEY `UK_LEVEL__NAME` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES levels WRITE;

INSERT INTO 
  levels (id, name, type) 
VALUES 
  ('1', '상담원', 'OPERATOR'), 
  ('2', '매니저', 'MANAGER'), 
  ('3', '관리자', 'ADMIN'), 
  ('4', '마스터', 'MASTER');
UNLOCK TABLES;

