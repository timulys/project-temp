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


CREATE TABLE `channel_end_auto` (
  `id` bigint(20) NOT NULL,
  `guest_delay_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `guest_delay_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `guest_delay_minute` int(11) DEFAULT NULL,
  `guest_notice_delay_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `guest_notice_delay_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `guest_notice_delaye_minute` int(11) DEFAULT NULL,
  `guide_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL,
  `guide_notice_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL,
  `guide_number` int(11) DEFAULT '5',
  `guide_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `member_delay_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `member_delay_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  `member_delay_minute` int(11) DEFAULT NULL,
  `register_enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Y : 활성 , N : 비활성',
  `register_message` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '전송할 메시지',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES channel_end_auto WRITE;

INSERT INTO channel_end_auto (id, guest_delay_enabled, guest_delay_message, guest_delay_minute, guest_notice_delay_enabled, guest_notice_delay_message, guest_notice_delaye_minute, guide_message, guide_notice_message, guide_number, guide_type, member_delay_enabled, member_delay_message, member_delay_minute, register_enabled, register_message) VALUES ('1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', '10', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', '5', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    } ]
  } ]
}', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', '5', 'notice', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', '7', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}');
UNLOCK TABLES;

