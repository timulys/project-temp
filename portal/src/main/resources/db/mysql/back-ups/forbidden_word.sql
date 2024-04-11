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


CREATE TABLE `forbidden_word` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `created` datetime(6) DEFAULT NULL COMMENT '등록한 시간',
  `member_id` bigint(20) NOT NULL COMMENT '등록한 직원PK',
  `word` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '금칙어 단어',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES forbidden_word WRITE;

INSERT INTO 
  forbidden_word (id, created, member_id, word) 
VALUES 
  ('17812', '2023-03-30 15:58:53.392000', '2', '금지어1'), 
  ('17813', '2023-03-30 15:58:57.216000', '2', '금지어2'), 
  ('17814', '2023-03-30 16:00:03.812000', '2', '다나'), 
  ('17816', '2023-03-30 16:07:04.218000', '2', '저장'), 
  ('17817', '2023-03-30 16:07:42.079000', '2', '크림'), 
  ('17818', '2023-03-30 16:07:59.919000', '2', '코대원'), 
  ('17820', '2023-03-30 17:08:52.802000', '2', '등록'), 
  ('17821', '2023-03-30 17:12:24.344000', '2', '짜잔'), 
  ('17823', '2023-03-30 17:14:49.367000', '2', '생각'), 
  ('17824', '2023-03-30 17:16:27.446000', '2', '볼펜'), 
  ('17826', '2023-03-30 17:18:45.222000', '2', '오호'), 
  ('17829', '2023-03-30 17:23:05.619000', '2', '안함'), 
  ('17830', '2023-03-30 17:53:33.590000', '2', '너무너무너무너무너무너무너무너무너무너무 긴 단어'), 
  ('18000', '2023-04-03 14:38:49.969000', '3', '아몬드'), 
  ('19411', '2023-04-10 10:27:37.226000', '3', '바나나'), 
  ('19445', '2023-04-10 11:02:21.501000', '3', '옥수수'), 
  ('20113', '2023-04-12 14:05:57.639000', '3', '등등록'), 
  ('20696', '2023-04-18 13:00:07.205000', '3', '안녕');
UNLOCK TABLES;

