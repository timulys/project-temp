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


CREATE TABLE `counsel_env` (
  `id` bigint(20) NOT NULL,
  `branch_id` bigint(20) NOT NULL,
  `issue_auto_close_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT 'Y' COMMENT '근무 시간 종료후 진행중인방 종료',
  `issue_delay_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL,
  `issue_delay_minute` int(11) DEFAULT NULL,
  `issue_file_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL,
  `issue_file_mime_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `member_auto_transform_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT 'Y' COMMENT '상담직원 전환 자동 승인',
  `modified` datetime(6) NOT NULL,
  `modifier` bigint(20) NOT NULL,
  `request_block_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT 'N' COMMENT '상담 인입제한',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qbh6q3s1791us33xa0hsjq942` (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES counsel_env WRITE;

INSERT INTO 
  counsel_env (id, branch_id, issue_auto_close_enabled, issue_delay_enabled, issue_delay_minute, issue_file_enabled, issue_file_mime_type, member_auto_transform_enabled, modified, modifier, request_block_enabled) 
VALUES 
  ('1', '1', 'Y', 'Y', '10', 'Y', 'all', 'N', '2023-03-30 15:13:52.384000', '2', 'N'), 
  ('15560', '15556', 'Y', 'Y', '60', 'Y', 'all', 'Y', '2023-03-16 13:21:07.330000', '1', 'N'), 
  ('15990', '15986', 'Y', 'Y', '3', 'Y', 'all', 'Y', '2023-03-20 16:29:27.661000', '16002', 'N'), 
  ('16279', '16275', 'Y', 'Y', '60', 'Y', 'all', 'Y', '2023-03-20 14:17:45.802000', '16282', 'N');
UNLOCK TABLES;

