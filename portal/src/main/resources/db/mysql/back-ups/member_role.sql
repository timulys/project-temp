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


CREATE TABLE `member_role` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `member_id` bigint(20) NOT NULL COMMENT '유저 PK',
  `modified` datetime(6) NOT NULL COMMENT '수정일시',
  `modifier` bigint(20) NOT NULL COMMENT '수정자',
  `role_id` bigint(20) NOT NULL COMMENT '역할 PK',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_MEMBER__MEMBER_ROLE` (`member_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES member_role WRITE;

INSERT INTO 
  member_role (id, member_id, modified, modifier, role_id) 
VALUES 
  ('1', '1', '2023-03-16 13:19:07', '1', '4'),
  ('2', '2', '2023-03-16 13:19:07', '1', '3'), 
  ('3', '3', '2023-03-16 13:19:07', '1', '2'), 
  ('4', '4', '2023-03-16 13:19:07', '1', '1'), 
  ('5', '5', '2023-03-16 13:19:07', '1', '1'), 
  ('15564', '15563', '2023-03-16 13:21:35.632000', '1', '3'), 
  ('15566', '15565', '2023-03-16 13:23:10.434000', '1', '2'), 
  ('15577', '15572', '2023-03-16 13:24:32.787000', '15563', '2'), 
  ('15580', '15579', '2023-03-16 13:25:04.314000', '15563', '1'), 
  ('15583', '15582', '2023-03-16 13:25:20.619000', '15563', '1'), 
  ('16283', '16282', '2023-03-20 14:13:11.058000', '1', '3'), 
  ('16291', '16285', '2023-03-20 14:14:43.137000', '16282', '2'), 
  ('16294', '16293', '2023-03-20 14:14:59.608000', '16282', '1'), 
  ('16402', '16002', '2023-03-20 14:59:49.835000', '16002', '3'), 
  ('16404', '15998', '2023-03-20 14:59:55.954000', '16002', '2'),
  ('16820', '15995', '2023-03-22 14:56:23.119000', '16002', '1'), 
  ('16863', '16862', '2023-03-22 15:36:05.770000', '16002', '1');
UNLOCK TABLES;

