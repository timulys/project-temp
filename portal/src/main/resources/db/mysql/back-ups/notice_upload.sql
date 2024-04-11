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


CREATE TABLE `notice_upload` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `notice_id` bigint(20) DEFAULT NULL COMMENT '공지 PK',
  `upload_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrdoux92pulddmer7dxw9swr1j` (`notice_id`),
  KEY `FKh6aw76j7pkb87wigp8cdqoi7a` (`upload_id`),
  CONSTRAINT `FKh6aw76j7pkb87wigp8cdqoi7a` FOREIGN KEY (`upload_id`) REFERENCES `upload` (`id`),
  CONSTRAINT `FKrdoux92pulddmer7dxw9swr1j` FOREIGN KEY (`notice_id`) REFERENCES `notice` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES notice_upload WRITE;

INSERT INTO 
  notice_upload (id, notice_id, upload_id) 
VALUES 
  ('17264', '17262', '17263'), 
  ('17267', '17265', '17266'), 
  ('17270', '17268', '17269'), 
  ('17313', '17311', '17312'), 
  ('17524', '17522', '17523'), 
  ('17711', '17709', '17710'), 
  ('17713', '17709', '17712'), 
  ('17715', '17709', '17714'), 
  ('17725', '17723', '17724'), 
  ('17727', '17723', '17726'), 
  ('19252', '19250', '19251'), 
  ('19254', '19250', '19253');
UNLOCK TABLES;

