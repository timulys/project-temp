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


CREATE TABLE `channel_start_auto` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `absence_code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'code',
  `absence_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `absence_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `st_code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'code',
  `st_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `st_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `unable_code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'code',
  `unable_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `unable_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `waiting_code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'code',
  `waiting_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `waiting_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `welcom_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `welcom_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES channel_start_auto WRITE;

INSERT INTO channel_start_auto (id, absence_code, absence_enabled, absence_message, st_code, st_enabled, st_message, unable_code, unable_enabled, unable_message, waiting_code, waiting_enabled, waiting_message, welcom_enabled, welcom_message) VALUES ('1', 'S2', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', 'ST', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1:1채팅을 요청하셨습니다. 채팅 내용은 상담 품질 관리를 위해 {위탁사명}에 저장됩니다. 상담을 시작하려면 메시지를입력해주세요. 채팅을 원치 않으시면 “!종료”를 입력해주세요."
    } ]
  } ]
}', 'S2', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "off"
    } ]
  } ]
}', 'S4', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', 'N', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 {{상담직원명}} 입니다. 무엇을 도와드릴까요?"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/39f7f7a3-4f84-425f-a027-a38ebd6c9114.png",
      "display" : "image"
    } ]
  } ]
}');
UNLOCK TABLES;

