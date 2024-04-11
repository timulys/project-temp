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


CREATE TABLE `level_privilege` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `level_id` bigint(20) NOT NULL COMMENT '레벨 PK',
  `privilege_id` bigint(20) NOT NULL COMMENT '권한 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_LEVEL_PRIVILEGE` (`level_id`,`privilege_id`),
  KEY `FK_LEVEL_PRIVILEGE__PRIVILEGE` (`privilege_id`),
  CONSTRAINT `FK_LEVEL_PRIVILEGE__PRIVILEGE` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES level_privilege WRITE;

UNLOCK TABLES;

