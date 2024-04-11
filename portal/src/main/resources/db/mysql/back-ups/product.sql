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


CREATE TABLE `product` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `depth` int(11) NOT NULL COMMENT '카테고리 depth',
  `is_open` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '브랜치 전체 오픈',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이름',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '부모 카테고리 PK',
  PRIMARY KEY (`id`),
  KEY `FKex0w3197qk5odu5gcsa5orx96` (`parent_id`),
  CONSTRAINT `FKex0w3197qk5odu5gcsa5orx96` FOREIGN KEY (`parent_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES product WRITE;

INSERT INTO 
  product (id, depth, is_open, name, parent_id) 
VALUES 
  ('1', '3', 'N', 'Kakao i GPT', NULL), 
  ('2', '3', 'N', '카카오i클라우드', NULL), 
  ('3', '3', 'N', '카카오i커넥트메세지', NULL), 
  ('4', '3', 'N', '카카오워크', NULL), 
  ('5', '3', 'N', '카카오i라스', NULL), 
  ('6', '3', 'N', '카카오i커넥트톡', NULL), 
  ('7', '3', 'N', '카카오i(AI/Machine Learning)', NULL), 
  ('8', '3', 'N', '카카오i서치', NULL), 
  ('9', '3', 'N', '카카오i커넥트 플레이스', NULL), 
  ('10', '3', 'N', 'AI전화음성봇/웹챗봇', NULL), 
  ('11', '3', 'N', '카카오i커넥트올웨이즈', NULL);
UNLOCK TABLES;

