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


CREATE TABLE `member_customer` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `consult_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '상담진행여부',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '고객 PK',
  `favorites` bit(1) NOT NULL COMMENT '즐겨찾기여부',
  `member_id` bigint(20) DEFAULT NULL COMMENT '상담원 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7eyvvb5qun7bcd8tg3x45fewn` (`customer_id`,`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES member_customer WRITE;

INSERT INTO 
  member_customer (id, consult_type, customer_id, favorites, member_id) 
VALUES 
  ('2', 'target', '2', false, '4'), 
  ('3', 'target', '3', false, '4'), 
  ('4', 'target', '4', false, '4'), 
  ('5', 'target', '5', true, '4'), 
  ('6', 'target', '6', false, '4'), 
  ('7', 'target', '7', false, '4'), 
  ('8', 'target', '8', true, '4'), 
  ('9', 'target', '9', false, '4'), 
  ('10', 'target', '10', false, '4'), 
  ('11', 'target', '11', false, '4'), 
  ('12', 'target', true, true, '3'), 
  ('13', 'target', '2', false, '3'), 
  ('14', 'target', '4', false, '3'), 
  ('15', 'target', '5', false, '3'), 
  ('16', 'target', '6', false, '3'), 
  ('17', 'target', '7', false, '3'), 
  ('18', 'target', '8', true, '3'), 
  ('19', 'target', '9', false, '3'), 
  ('20', 'target', '10', false, '3'), 
  ('21', 'target', '3', false, '3'), 
  ('22', 'target', '23', false, '3'), 
  ('23', 'target', '22', false, '3'), 
  ('24', 'target', '21', false, '3'), 
  ('25', 'target', '19', false, '3'), 
  ('26', 'target', '20', false, '3'), 
  ('27', 'target', '18', false, '3'), 
  ('28', 'target', '17', false, '3'), 
  ('29', 'target', '16', false, '3'), 
  ('30', 'target', '15', false, '3'), 
  ('31', 'target', '14', false, '3'), 
  ('32', 'target', '13', false, '3');
UNLOCK TABLES;

