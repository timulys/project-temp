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


CREATE TABLE `counsel_inflow_env` (
  `id` bigint(20) NOT NULL,
  `branch_id` bigint(20) NOT NULL COMMENT 'branch PK',
  `enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT 'Y' COMMENT '사용 여부',
  `inflow_path_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '상담 유입경로 대상 official : 공식 채널 , unlimited : 제한없음',
  `modified` datetime(6) NOT NULL COMMENT '수정일',
  `modifier` bigint(20) NOT NULL COMMENT '수정 회원 PK',
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '상담 유입경로 명',
  `params` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '상담 유입경로',
  `val` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '상담 유입경로 값',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_COUNSEL_INFLOW_ENV__BRANCH_PARAMS` (`branch_id`,`params`),
  KEY `IDX_COUNSEL_INFLOW_ENV__BRANCH` (`branch_id`),
  KEY `IDX_COUNSEL_INFLOW_ENV__ENABLED` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES counsel_inflow_env WRITE;

INSERT INTO 
  counsel_inflow_env (id, branch_id, enabled, inflow_path_type, modified, modifier, name, params, val) 
VALUES 
  ('15699', '1', 'N', 'official', '2023-03-30 15:13:52.425000', '2', 'connectbranch', 'channel', 'https://bizmessage.kakao.com/chat/open/{{channel_name}}?extra=path_channel'), 
  ('15700', '1', 'N', 'official', '2023-03-30 15:13:52.455000', '2', 'connectbranch', 'nzc2qzu2odfvbm5', 'https://bizmessage.kakao.com/chat/open/{{channel_name}}?extra=path_nzc2qzu2odfvbm5'), 
  ('16005', '15986', 'Y', 'official', '2023-03-20 16:29:27.701000', '16002', 'connectbranch', 'nzc2qzu2odfvbm5', 'https://bizmessage.kakao.com/chat/open/{{channel_name}}?extra=path_nzc2qzu2odfvbm5'), 
  ('16297', '16275', 'Y', 'official', '2023-03-20 14:17:45.840000', '16282', 'connectbranch', 'nzc2qzu2odfvbm5', 'https://bizmessage.kakao.com/chat/open/{{channel_name}}?extra=path_nzc2qzu2odfvbm5');
UNLOCK TABLES;

