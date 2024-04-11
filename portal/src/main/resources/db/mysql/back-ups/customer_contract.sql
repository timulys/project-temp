-- --------------------------------------------------------------
-- QueryPie Schema Export Script
-- https://www.querypie.com
--
-- Database: connectbranch
-- Generated At: 04/19/2023 04:55:17 +00:00
-- --------------------------------------------------------------

drop table customer_contract;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `customer_contract` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `contract_end_date` date DEFAULT NULL COMMENT '계약 종료 날짜',
  `contract_start_date` date DEFAULT NULL COMMENT '계약 시작 날짜',
  `contracted` bit(1) NOT NULL COMMENT '계약여부',
  `created` datetime(6) DEFAULT NULL COMMENT '등록날짜',
  `modified` datetime(6) DEFAULT NULL COMMENT '수정날짜',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '고객 PK',
  `guide_category_id` bigint(20) DEFAULT NULL COMMENT '상품 PK',
  `member_id` bigint(20) DEFAULT NULL COMMENT '계약진행 직원 PK',
  `product_id` bigint(20) DEFAULT NULL COMMENT '상품 PK',
  PRIMARY KEY (`id`),
  KEY `FKfh3cfo5dsi4i9ndfyig8iwowq` (`customer_id`),
  KEY `FKpn2x1evsg3j6y6caxe4f68fxs` (`guide_category_id`),
  KEY `FKa7mpj4pl7d8auivadw7pgq4we` (`member_id`),
  KEY `FKndtt85gcdut1sij6nmr7unfm0` (`product_id`),
  CONSTRAINT `FKa7mpj4pl7d8auivadw7pgq4we` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
  CONSTRAINT `FKfh3cfo5dsi4i9ndfyig8iwowq` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKndtt85gcdut1sij6nmr7unfm0` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKpn2x1evsg3j6y6caxe4f68fxs` FOREIGN KEY (`guide_category_id`) REFERENCES `guide_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES customer_contract WRITE;

INSERT INTO 
  customer_contract (id, contract_end_date, contract_start_date, contracted, created, modified, customer_id, guide_category_id, member_id, product_id) 
VALUES 
  ('1', '2023-09-20', '2023-04-30', '1', '2023-04-12 07:02:31', '2023-04-12 07:02:31', '2', '19613', '4', '2'), 
  ('2', '2023-12-25', '2023-04-20', '1', '2023-04-14 13:41:53', '2023-04-14 13:41:53', '4', NULL, '3', '2');
UNLOCK TABLES;

