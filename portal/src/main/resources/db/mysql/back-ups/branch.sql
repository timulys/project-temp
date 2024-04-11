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

CREATE TABLE `branch` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `assign` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `created` datetime(6) NOT NULL COMMENT '생성 일시',
  `creator` bigint(20) NOT NULL COMMENT '생성자',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
  `first_message_type` varchar(20) COLLATE utf8mb4_bin DEFAULT 'member',
  `head_quarters` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'N' COMMENT '본사 여부',
  `max_counsel` int(11) DEFAULT '0' COMMENT '최대 상담 건수',
  `max_counsel_type` varchar(255) COLLATE utf8mb4_bin DEFAULT 'batch' COMMENT '최대 상담건수 타입 batch : 일괄 , individual : 개별',
  `max_member_counsel` int(11) DEFAULT '0' COMMENT '신규직원 최대 상담 건수 default',
  `modified` datetime(6) NOT NULL COMMENT '수정 일시',
  `modifier` bigint(20) DEFAULT NULL COMMENT '수정자',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이름',
  `off_duty_hours` varchar(1) COLLATE utf8mb4_bin DEFAULT 'Y' COMMENT '근무예외 시간 사용 여부',
  `status` varchar(255) COLLATE utf8mb4_bin DEFAULT 'on' COMMENT '상담 여부',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2qdmejoguc37exo9i2fjxb0qo` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES branch WRITE;

INSERT INTO 
  branch (id, assign, created, creator, enabled, first_message_type, head_quarters, max_counsel, max_counsel_type, max_member_counsel, modified, modifier, name, off_duty_hours, status, max_guide_category_depth)
VALUES 
  ('1', 'member', '2023-03-16 13:19:01', '1', 'Y', 'member', 'Y', '50', 'individual', '50', '2023-04-06 09:32:35.883000', '2', '본사', 'N', 'on', 3),
  ('15556', 'branch', '2023-03-16 13:21:07.253000', '1', 'Y', 'member', 'N', '50', 'batch', '100', '2023-03-16 13:21:07.253000', '1', '판교점', 'Y', 'on', 0),
  ('15986', 'member', '2023-03-17 13:52:37.548000', '1', 'Y', 'member', 'N', '50', 'individual', '50', '2023-03-22 15:46:59.472000', '16002', 'kep점', 'Y', 'on', 0),
  ('16275', 'branch', '2023-03-20 14:12:31.225000', '1', 'Y', 'member', 'N', '50', 'individual', '50', '2023-03-20 14:12:31.225000', '1', '코드클릭점', 'Y', 'on', 0);
UNLOCK TABLES;

