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


CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `modified` datetime(6) NOT NULL COMMENT '수정 일시',
  `modifier` bigint(20) NOT NULL COMMENT '수정자',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '역할 이름',
  `type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '역할 타입',
  `level_id` bigint(20) NOT NULL COMMENT '레벨 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ROLE__TYPE` (`type`),
  UNIQUE KEY `UK_ROLE__NAME` (`name`),
  KEY `FK_ROLE__LEVEL` (`level_id`),
  CONSTRAINT `FK_ROLE__LEVEL` FOREIGN KEY (`level_id`) REFERENCES `levels` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES roles WRITE;

INSERT INTO 
  roles (id, modified, modifier, name, type, level_id) 
VALUES 
  ('1', '2023-03-16 13:19:06', '1', '상담원', 'OPERATOR_1', '1'), 
  ('2', '2023-03-16 13:19:06', '1', '매니저', 'MANAGER_1', '2'), 
  ('3', '2023-03-16 13:19:06', '1', '관리자', 'ADMIN_1', '3'), 
  ('4', '2023-03-16 13:19:06', '1', '마스터', 'MASTER_1', '4');
UNLOCK TABLES;
