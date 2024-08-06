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


CREATE TABLE `channel_env` (
  `id` bigint(20) NOT NULL,
  `assign_standby_enabled` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `assign_standby_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `assign_standby_number` int(11) DEFAULT NULL,
  `created` datetime(6) DEFAULT NULL COMMENT '생성일',
  `creator` bigint(20) DEFAULT NULL COMMENT '생성 회원 PK',
  `customer_connection` varchar(255) COLLATE utf8mb4_bin DEFAULT 'category' COMMENT '고객 연결 방식',
  `evaluation_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `evaluation_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `impossible_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '상담 불가 안내 메세지',
  `max_issue_category_depth` int(11) DEFAULT '0' COMMENT '상담 배분 분류 최대 단계',
  `member_assign` varchar(255) COLLATE utf8mb4_bin DEFAULT 'basic' COMMENT '상담직원 배정 방식',
  `member_direct_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT 'Y' COMMENT '상담직원 직접 연결 허용 Y : 사용 , N : 사용안함',
  `modified` datetime(6) NOT NULL COMMENT '수정일',
  `modifier` bigint(20) NOT NULL COMMENT '수정 회원 PK',
  `request_block_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT 'N' COMMENT '상담 인입 제한',
  `channel_id` bigint(20) NOT NULL COMMENT '채널 PK',
  `channel_end_auto_id` bigint(20) NOT NULL COMMENT '상담시작 종료 PK',
  `channel_start_auto_id` bigint(20) NOT NULL COMMENT '상담시작 종료 PK',
  PRIMARY KEY (`id`),
  KEY `FK_CHANNEL_ENV__CHANNEL` (`channel_id`),
  KEY `FK_CHANNEL_ENV__CHANNEL_END_AUTO` (`channel_end_auto_id`),
  KEY `FK_CHANNEL_ENV__CHANNEL_START_AUTO` (`channel_start_auto_id`),
  CONSTRAINT `FK_CHANNEL_ENV__CHANNEL` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`),
  CONSTRAINT `FK_CHANNEL_ENV__CHANNEL_END_AUTO` FOREIGN KEY (`channel_end_auto_id`) REFERENCES `channel_end_auto` (`id`),
  CONSTRAINT `FK_CHANNEL_ENV__CHANNEL_START_AUTO` FOREIGN KEY (`channel_start_auto_id`) REFERENCES `channel_start_auto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES channel_env WRITE;

INSERT INTO channel_env (id, assign_standby_enabled, assign_standby_message, assign_standby_number, created, creator, customer_connection, evaluation_enabled, evaluation_message, impossible_message, max_issue_category_depth, member_assign, member_direct_enabled, modified, modifier, request_block_enabled, channel_id, channel_end_auto_id, channel_start_auto_id) VALUES ('1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "off"
    } ]
  } ]
}', NULL, '2023-03-16 13:19:08', '1', 'custom', 'N', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담에 만족하셨나요? 상담품질 개선을 위해 만족도 평가를 부탁드립니다^^"
    } ]
  } ]
}', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "off"
    } ]
  } ]
}', '0', 'category', 'Y', '2023-04-05 20:58:44.531000', '2', 'N', '1', '1', '1');
UNLOCK TABLES;

