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

CREATE TABLE `customer_anniversary` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `anniversary_day` date NOT NULL COMMENT '기념일 날짜',
  `created` datetime(6) NOT NULL COMMENT '등록 시간',
  `creator` bigint(20) NOT NULL COMMENT '등록 유저 PK',
  `modified` datetime(6) DEFAULT NULL COMMENT '수정 시간',
  `modifier` bigint(20) DEFAULT NULL COMMENT '수정 유저 PK',
  `anniversary_code` varchar(3) COLLATE utf8mb4_bin NOT NULL COMMENT '기념일 이름',
  `customer_id` bigint(20) NOT NULL COMMENT '고객 PK ',
  PRIMARY KEY (`id`),
  KEY `FKsafqoccb06oa5g9vabay09td5` (`anniversary_code`),
  KEY `FKiwdcqxfq0ufnfw04h6ufckdyq` (`customer_id`),
  CONSTRAINT `FKiwdcqxfq0ufnfw04h6ufckdyq` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKsafqoccb06oa5g9vabay09td5` FOREIGN KEY (`anniversary_code`) REFERENCES `anniversary` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES customer_anniversary WRITE;

INSERT INTO 
  customer_anniversary (id, anniversary_day, created, creator, modified, modifier, anniversary_code, customer_id) 
VALUES 
  ('1', '2023-04-06', '2023-04-12 08:11:48', '4', '2023-04-12 08:11:48', '4', '4', '4'), 
  ('2', '2023-04-13', '2023-04-12 08:50:25', '4', '2023-04-12 08:50:25', '4', '3', '4'), 
  ('3', '2023-04-09', '2023-04-12 08:50:52', '4', '2023-04-12 08:50:52', '4', '3', '6');
UNLOCK TABLES;

