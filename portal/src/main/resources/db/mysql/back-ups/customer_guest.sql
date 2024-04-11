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


CREATE TABLE `customer_guest` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `channel_id` bigint(20) NOT NULL COMMENT '채널 PK',
  `created` datetime(6) NOT NULL COMMENT '생성 일시',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이름',
  `user_key` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '유저키',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '식별 고객 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_GUEST__SEARCH` (`channel_id`,`user_key`),
  KEY `IDX_GUEST__CUSTOMER` (`customer_id`),
  KEY `IDX_GUEST__NAME` (`name`),
  CONSTRAINT `FK_GUEST__CUSTOMER` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES customer_guest WRITE;

INSERT INTO 
  customer_guest (id, channel_id, created, name, user_key, customer_id) 
VALUES 
  ('15567', '1', '2023-03-16 13:23:23.654000', '김도유', 'eZoIw3_X1-1o', '1'), 
  ('15585', '1', '2023-03-16 13:25:53.898000', '고객 15585', 'eIAJHkpns7Tk', NULL), 
  ('15606', '1', '2023-03-16 13:32:45.448000', '김해인', 'V1YlUfZfCoOC', '6'), 
  ('15659', '1', '2023-03-16 18:16:57.579000', '신기주', 'JiMq4xJhwKzI', '2'), 
  ('15688', '1', '2023-03-17 08:24:03.562000', '고객 15688', 'EMgARc1272-3', NULL), 
  ('15859', '1', '2023-03-17 11:18:17.364000', '고객 15859', 'RC-x4t7eiPvk', NULL), 
  ('16091', '1', '2023-03-17 15:27:19.976000', '이강희', 'b0SSt1A3l_wY', '7'), 
  ('16302', '1', '2023-03-20 14:21:48.792000', '고객 16302', 'ckS3TZ22EpT1', NULL), 
  ('16822', '1', '2023-03-22 15:14:04.070000', '고영수', 'bjmzqD45Qv7o', NULL), 
  ('16835', '1', '2023-03-22 15:27:50.633000', '한겨울', 'BlmV4kgouwgh', NULL), 
  ('16926', '1', '2023-03-24 16:27:34.300000', '정민수', 'Og7kEybZctNI', '8'), 
  ('17394', '1', '2023-03-29 14:02:28.389000', '고객 17394', 'N0UqtxpeJncF', NULL), 
  ('17634', '1', '2023-03-30 10:47:35.122000', '고객 17634', 'A326C2USD76l', NULL), 
  ('18581', '1', '2023-04-04 17:13:58.211000', '고객 18581', 'XeWxLxshWKUA', NULL), 
  ('20400', '1', '2023-04-14 13:46:41.301000', '고객 20400', 'cTKzzdSGKgVa', '4');
UNLOCK TABLES;

