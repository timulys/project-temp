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


CREATE TABLE `customer_promise` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `created` datetime(6) NOT NULL COMMENT '등록 시간',
  `modified` datetime(6) DEFAULT NULL COMMENT '수정 시간',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '고객 PK',
  `guide_category_id` bigint(20) DEFAULT NULL COMMENT '상품 PK',
  `product_id` bigint(20) DEFAULT NULL COMMENT '상품 PK',
  PRIMARY KEY (`id`),
  KEY `FK5s4a7nqhk6gxv34plbiahxtw2` (`customer_id`),
  KEY `FK4qs8ndo4lpdle3olww59fg8gw` (`guide_category_id`),
  KEY `FKd6go8urbk5cvki6y5ycjsoj7i` (`product_id`),
  CONSTRAINT `FK4qs8ndo4lpdle3olww59fg8gw` FOREIGN KEY (`guide_category_id`) REFERENCES `guide_category` (`id`),
  CONSTRAINT `FK5s4a7nqhk6gxv34plbiahxtw2` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKd6go8urbk5cvki6y5ycjsoj7i` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES customer_promise WRITE;

INSERT INTO 
  customer_promise (id, created, modified, customer_id, guide_category_id, product_id) 
VALUES 
  ('1', '2023-04-12 04:24:22', '2023-04-12 04:50:22', '1', NULL, '1'), 
  ('2', '2023-04-12 04:24:36', '2023-04-12 04:54:36', '1', NULL, '2'), 
  ('3', '2023-04-12 04:36:22', '2023-04-12 04:56:22', '2', NULL, '3'), 
  ('4', '2023-04-12 04:54:38', '2023-04-12 04:54:38', '3', NULL, '4'), 
  ('5', '2023-04-12 04:54:51', '2023-04-12 04:54:51', '2', NULL, '5');
UNLOCK TABLES;

