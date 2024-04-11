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


CREATE TABLE `branch_role` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `branch_id` bigint(20) NOT NULL COMMENT '브랜치 PK',
  `modified` datetime(6) NOT NULL COMMENT '수정 일시',
  `modifier` bigint(20) NOT NULL COMMENT '수정자',
  `role_id` bigint(20) NOT NULL COMMENT '역할 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_BRANCH_ROLE` (`branch_id`,`role_id`),
  KEY `FK_BRANCH_ROLE__ROLE` (`role_id`),
  CONSTRAINT `FK_BRANCH_ROLE__ROLE` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES branch_role WRITE;

INSERT INTO 
  branch_role (id, branch_id, modified, modifier, role_id) 
VALUES 
  ('1', '1', '2023-03-16 13:19:06', '1', '1'), 
  ('2', '1', '2023-03-16 13:19:07', '1', '2'), 
  ('3', '1', '2023-03-16 13:19:07', '1', '3'), 
  ('15557', '15556', '2023-03-16 13:21:07.306000', '1', '1'), 
  ('15558', '15556', '2023-03-16 13:21:07.306000', '1', '2'), 
  ('15559', '15556', '2023-03-16 13:21:07.306000', '1', '3'), 
  ('16068', '15986', '2023-03-17 14:47:44.590000', '4', '2'), 
  ('16069', '15986', '2023-03-17 14:47:44.590000', '4', '3'), 
  ('16070', '15986', '2023-03-17 14:47:44.590000', '4', '1'), 
  ('16276', '16275', '2023-03-20 14:12:31.241000', '1', '1'), 
  ('16277', '16275', '2023-03-20 14:12:31.241000', '1', '2'), 
  ('16278', '16275', '2023-03-20 14:12:31.241000', '1', '3');
UNLOCK TABLES;

