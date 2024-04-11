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


CREATE TABLE `guest_memo` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `content` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '메모내용',
  `created` datetime(6) NOT NULL COMMENT '등록 시간',
  `guest_id` bigint(20) DEFAULT NULL COMMENT '고객 PK',
  `member_id` bigint(20) DEFAULT NULL COMMENT '상담원 PK',
  `modified` datetime(6) NOT NULL COMMENT '수정 시간',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4wsqnt5yqclxqv97sda2s834u` (`guest_id`,`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES guest_memo WRITE;

INSERT INTO 
  guest_memo (id, content, created, guest_id, member_id, modified) 
VALUES 
  ('19532', '작성중인 고객메모를 저장하시겠습니까?

정말요?', '2023-04-10 14:44:28.219000', '15859', '5', '2023-04-11 14:37:09.296000'), 
  ('19700', '고객
메
모
입
니
다
헤
헤
헤
헤
고
객 
메모
에용
고!
객!
ㅁㅔ!
모!
입!
니!
당!
히
히
텀
블!
럿!
', '2023-04-10 20:05:57.300000', '15659', '4', '2023-04-10 20:05:57.300000'), 
  ('20082', '고객메모 테스트트틑22222222233333344444444445555', '2023-04-12 10:40:10.087000', '16091', '3', '2023-04-12 10:57:11.964000'), 
  ('20105', '고객메모', '2023-04-12 13:41:49.459000', '15606', '3', '2023-04-12 13:41:49.459000'), 
  ('20343', '알림톡 겁나 빠른 메모테스튜ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ', '2023-04-13 11:12:08.593000', '16926', '4', '2023-04-14 10:04:16.534000'), 
  ('20364', '고객메모 dkfsjlkfjaslkfja', '2023-04-13 15:03:31.434000', '15606', '4', '2023-04-14 18:03:55.199000'), 
  ('20608', 'ㄹ
ㄹ
ㄹ
ㄹ
ㄴ
ㄴ
ㄴ
ㄴ
ㄴ
ㄴㄴ
ㄴ
ㄴ
ㄴ
ㄴ
ㄴ
ㄴ
', '2023-04-17 10:22:54.889000', '15659', '3', '2023-04-17 10:42:11.158000'), 
  ('20610', '메모ㅇㅇㄴㅇㄴㅇㄹㅁㄴㅇㅁㄴㅇㄹㄴㅇㄹ', '2023-04-17 14:05:28.010000', '15859', '4', '2023-04-17 15:58:31.375000');
UNLOCK TABLES;

