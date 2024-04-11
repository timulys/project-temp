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


CREATE TABLE `issue_extra` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `evaluation_modified` datetime(6) DEFAULT NULL COMMENT '고객 상담 평가 생성 일시',
  `evaluation_score` int(11) DEFAULT NULL COMMENT '고객 상담 평가, 점수',
  `guest_id` bigint(20) DEFAULT NULL COMMENT '비식별 고객 PK',
  `inflow` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이슈 생성시, 파라미터 중 유입경로 (Stringify JSON)',
  `inflow_modified` datetime(6) DEFAULT NULL COMMENT '이슈 생성시, 파라미터 중 유입경로 생성 일시',
  `issue_category_id` bigint(20) DEFAULT NULL COMMENT '후처리 분류',
  `memo` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '메모',
  `memo_modified` datetime(6) DEFAULT NULL COMMENT '메모 수정 일시',
  `parameter` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이슈 생성시, 파라미터 (Stringify JSON)',
  `summary` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '후처리 요약',
  `summary_modified` datetime(6) DEFAULT NULL COMMENT '후처리 수정 일시',
  PRIMARY KEY (`id`),
  KEY `IDX_ISSUE_EXTRA__SEARCH` (`guest_id`,`inflow`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES issue_extra WRITE;

INSERT INTO 
  issue_extra (id, evaluation_modified, evaluation_score, guest_id, inflow, inflow_modified, issue_category_id, memo, memo_modified, parameter, summary, summary_modified) 
VALUES 
  ('15607', NULL, NULL, '15606', NULL, NULL, NULL, NULL, NULL, '{"mocked":true}', NULL, NULL), 
  ('15644', NULL, NULL, '15606', NULL, NULL, '36', NULL, NULL, NULL, '', '2023-03-16 13:53:51.036000'), 
  ('15658', NULL, NULL, '15567', NULL, NULL, '35', NULL, NULL, NULL, 'ㄷㄷ', '2023-03-16 17:17:17.481000'), 
  ('15760', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-17 10:17:01.353000', '35', NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"5","bid":"1"}', '~~~ 상담 진행되었습니다.', '2023-03-17 10:26:25.619000'), 
  ('15772', NULL, NULL, '15659', NULL, NULL, '35', NULL, NULL, NULL, '요약합니다.', '2023-03-17 10:34:18.437000'), 
  ('15773', NULL, NULL, '15606', NULL, NULL, NULL, NULL, NULL, '{"mid":"1"}', NULL, NULL), 
  ('15790', NULL, NULL, '15688', NULL, NULL, '35', NULL, NULL, NULL, '요약합니다.', '2023-03-17 10:38:38.615000'), 
  ('15937', NULL, NULL, '15606', NULL, NULL, '35', NULL, NULL, NULL, '요약 완료', '2023-03-17 13:26:32.964000'), 
  ('15938', NULL, NULL, '15659', NULL, NULL, '35', NULL, NULL, NULL, 'ㅓㅓㅓ', '2023-03-17 13:29:49.154000'), 
  ('15947', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-17 13:32:48.128000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"3","bid":"1"}', NULL, NULL), 
  ('15952', NULL, NULL, '15606', NULL, NULL, NULL, 'ㅇㅇㅇㅇㅇㅇㅇ', '2023-03-17 13:36:10.411000', NULL, NULL, NULL), 
  ('15955', NULL, NULL, '15606', NULL, NULL, NULL, NULL, NULL, '{"path":"test"}', NULL, NULL), 
  ('15966', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-17 13:43:05.441000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('15973', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-17 13:43:36.533000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"5","bid":"1"}', NULL, NULL), 
  ('15980', NULL, NULL, '15606', NULL, NULL, NULL, NULL, NULL, '{"path":"test"}', NULL, NULL), 
  ('15985', NULL, NULL, '15659', NULL, NULL, '35', NULL, NULL, NULL, '요약해주세요', '2023-03-17 13:50:44.067000'), 
  ('16006', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-17 14:02:31.462000', '35', NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', 'ㅇ요약', '2023-03-20 16:36:01.450000'), 
  ('16027', NULL, NULL, '15606', NULL, NULL, NULL, NULL, NULL, '{"path":"test"}', NULL, NULL), 
  ('16049', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-17 14:36:47.450000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16071', NULL, NULL, '15567', NULL, NULL, NULL, '메모', '2023-03-17 14:59:45.726000', NULL, NULL, NULL), 
  ('16076', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-17 15:10:50.005000', NULL, '됩나까', '2023-03-20 15:09:00.536000', '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', NULL, NULL), 
  ('16191', NULL, NULL, '15585', NULL, NULL, NULL, NULL, NULL, '{"path":"httt","mid":"4","bid":"1"}', NULL, NULL), 
  ('16274', NULL, NULL, '15585', NULL, NULL, '35', NULL, NULL, NULL, '3월 20일 요약', '2023-03-20 13:08:49.855000'), 
  ('16303', NULL, NULL, '16302', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 14:21:48.817000', NULL, 'test222', '2023-03-29 10:25:48.995000', '{"path":"nzc2qzu2odfvbm5","mid":"16293","bid":"16275"}', NULL, NULL), 
  ('16326', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 14:26:18.767000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"16293","bid":"16275"}', NULL, NULL), 
  ('16338', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 14:27:46.240000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16357', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 14:41:50.409000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16372', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 14:48:04.548000', NULL, '되나여', '2023-03-20 15:08:52.877000', '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', NULL, NULL), 
  ('16453', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 16:25:54.581000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5"}', NULL, NULL), 
  ('16460', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 16:27:11.070000', NULL, '메모', '2023-03-20 18:10:34.676000', '{"path":"nzc2qzu2odfvbm5"}', NULL, NULL), 
  ('16467', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 16:28:21.742000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', NULL, NULL), 
  ('16497', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 16:36:26.263000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', NULL, NULL), 
  ('16522', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 16:38:39.041000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', NULL, NULL), 
  ('16539', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 16:40:16.141000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16544', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 16:40:58.951000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16556', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 16:43:29.313000', NULL, '메모', '2023-03-20 18:10:26.548000', '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16596', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 19:57:21.385000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16615', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 20:13:58.844000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16618', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 20:14:42.551000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16625', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 20:17:18.368000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16632', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-20 20:31:43.644000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('16734', NULL, NULL, '15567', NULL, NULL, NULL, NULL, NULL, '{"mid":"5","bid":"1"}', NULL, NULL), 
  ('16741', NULL, NULL, '15567', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-21 18:33:00.559000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('16750', NULL, NULL, '15567', NULL, NULL, NULL, NULL, NULL, '{"mid":"5","bid":"1"}', NULL, NULL), 
  ('16823', NULL, NULL, '16822', NULL, NULL, NULL, NULL, NULL, '{"path":"test"}', NULL, NULL), 
  ('16830', NULL, NULL, '16091', NULL, NULL, NULL, NULL, NULL, '{"path":"test"}', NULL, NULL), 
  ('16836', NULL, NULL, '16835', NULL, NULL, NULL, NULL, NULL, '{"path":"test"}', NULL, NULL), 
  ('16843', NULL, NULL, '15606', NULL, NULL, NULL, NULL, NULL, '{"path":"test"}', NULL, NULL), 
  ('16848', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-22 15:31:19.962000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', NULL, NULL), 
  ('16868', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-22 15:40:09.137000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"16862","bid":"15986"}', NULL, NULL), 
  ('16881', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-22 15:41:28.157000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"16862","bid":"15986"}', NULL, NULL), 
  ('16927', NULL, NULL, '16926', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-24 16:27:34.333000', '35', NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', '상담 끝', '2023-03-24 16:28:47.237000'), 
  ('16947', NULL, NULL, '16926', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-24 16:29:36.915000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', NULL, NULL), 
  ('16978', NULL, NULL, '16926', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-24 16:38:21.356000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"15995","bid":"15986"}', NULL, NULL), 
  ('17207', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-27 14:35:53.282000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17220', NULL, NULL, '16926', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-03-27 19:17:15.238000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('17235', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-28 19:17:21.405000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17255', NULL, NULL, '16091', NULL, NULL, NULL, 'test', '2023-03-29 10:24:43.030000', NULL, NULL, NULL), 
  ('17326', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-29 13:40:51.029000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17346', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-29 13:48:05.480000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17366', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-29 13:52:01.452000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17377', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-29 13:54:35.002000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17385', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-29 14:01:42.938000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17390', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-29 14:02:10.116000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17395', NULL, NULL, '17394', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-29 14:02:28.415000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17400', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-29 14:02:31.166000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17600', NULL, NULL, '17394', NULL, NULL, NULL, NULL, NULL, '{"mid":"3"}', NULL, NULL), 
  ('17604', NULL, NULL, '17394', NULL, NULL, NULL, NULL, NULL, '{"mid":"1"}', NULL, NULL), 
  ('17608', NULL, NULL, '17394', NULL, NULL, NULL, NULL, NULL, '{"mid":"4"}', NULL, NULL), 
  ('17613', NULL, NULL, '16835', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-30 10:30:58.755000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17635', NULL, NULL, '17634', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-30 10:47:35.142000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17744', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-03-30 14:53:40.549000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17957', NULL, NULL, '15659', NULL, NULL, NULL, 'test', '2023-04-03 09:10:04.203000', NULL, NULL, NULL), 
  ('17975', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 14:07:03.680000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('17992', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 14:37:56.746000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18013', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 14:44:37.501000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18020', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 14:45:33.605000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18047', NULL, NULL, '15606', NULL, NULL, NULL, '메모 테스트2222', '2023-04-03 15:12:49.281000', NULL, NULL, NULL), 
  ('18068', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 18:39:00.972000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18099', NULL, NULL, '16091', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-03 18:45:58.116000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('18232', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 20:50:29.462000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18236', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 20:50:50.910000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18247', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 20:56:18.208000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18257', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 20:57:20.239000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18274', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-03 21:03:56.831000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18331', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 10:44:24.714000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18341', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 10:50:27.644000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18347', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 10:52:35.173000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18359', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 10:54:06.937000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18367', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 10:55:37.126000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18373', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 10:58:28.557000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18382', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 11:00:18.505000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18450', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:06:14.819000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18460', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:11:13.017000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18470', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:17:31.550000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18481', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:18:58.951000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18490', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:21:07.007000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18503', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:25:16.206000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18511', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:27:35.905000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18523', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:43:57.204000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18531', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:44:57.738000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18539', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:45:41.428000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18550', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 16:46:33.605000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18582', NULL, NULL, '18581', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-04 17:13:58.235000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18625', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 10:24:01.857000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18640', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 14:34:26.527000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18655', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 14:37:29.388000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18662', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 14:38:34.840000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18667', NULL, NULL, '15659', NULL, NULL, NULL, 'ㅇ니ㅏ러ㅣㄴ아럼ㄴ', '2023-04-05 14:38:41.153000', NULL, NULL, NULL), 
  ('18670', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 14:42:43.443000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18678', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 14:45:34.244000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18690', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 14:58:58.971000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18697', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:00:11.860000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18706', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:03:38.865000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18716', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:15:45.517000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18723', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:20:16.964000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18732', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:29:24.246000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18739', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:32:07.499000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18746', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:36:10.415000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18751', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:44:30.348000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18759', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 15:56:33.590000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18765', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 16:03:01.495000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18781', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 16:05:37.791000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18786', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 16:05:52.608000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18795', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 16:07:58.703000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18810', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 16:22:43.789000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('18817', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 17:01:11.811000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18824', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 18:07:36.313000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18838', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 18:49:07.394000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18845', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 20:42:01.523000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18856', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 20:54:59.564000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18865', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 20:58:05.568000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18873', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 20:59:13.947000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18911', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-05 21:13:03.726000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18974', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 09:30:41.805000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('18991', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 10:54:10.743000', NULL, '메모
테스트 
입니다', '2023-04-06 14:09:30.057000', '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19010', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 14:23:03.835000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19023', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 15:41:26.144000', NULL, 'fghfgh', '2023-04-06 20:05:39.234000', '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19069', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 18:45:03.752000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19080', NULL, NULL, '15585', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 18:46:20.079000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('19089', NULL, NULL, '15585', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 18:52:09.423000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('19100', NULL, NULL, '15585', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 19:01:12.087000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('19116', NULL, NULL, '15585', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 19:28:25.292000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('19124', NULL, NULL, '15567', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 19:39:18.254000', NULL, 'ㅠㅠㅏㅓ', '2023-04-09 15:58:04.307000', '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19133', NULL, NULL, '15585', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 19:45:21.141000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('19138', NULL, NULL, '15585', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 19:45:59.904000', NULL, NULL, NULL, '{"path":"channel","mid":"5","bid":"1"}', NULL, NULL), 
  ('19169', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 21:35:36.176000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19183', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 21:36:51.977000', '35', NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', '첫 인사말 확인', '2023-04-06 21:56:48.562000'), 
  ('19197', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-06 22:02:22.950000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19212', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-07 12:41:17.007000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19323', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-07 18:53:38.524000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19336', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-09 15:53:35.061000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('19381', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-10 09:22:12.884000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('19404', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-10 10:19:59.948000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('19452', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-10 11:11:24.120000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('19462', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-10 11:17:21.292000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19483', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-10 13:55:34.192000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('19520', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-10 13:57:33.390000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('19535', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-10 14:54:09.515000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('19820', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 09:05:52.498000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19833', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 09:23:22.868000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19847', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 10:10:30.561000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19866', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 10:18:43.345000', '35', '회사에서 커넥트 올웨이즈 도입을 고려하고 있음
기능 설명 이후 내부 검토', '2023-04-11 10:31:04.619000', '{"path":"channel","mid":"4","bid":"1"}', '커넥트 올웨이즈 기능 설명 이후 고객 무응답 자동 종료', '2023-04-11 10:42:15.180000'), 
  ('19912', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 10:48:35.918000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19934', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 11:00:42.663000', NULL, NULL, NULL, '{"path":"channel","mid":"2","bid":"1"}', NULL, NULL), 
  ('19950', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 15:42:19.094000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('19959', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 15:43:46.092000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20008', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-11 17:05:41.422000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20038', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-12 09:01:00.247000', NULL, NULL, NULL, '{"path":"channel","mid":"3","bid":"1"}', NULL, NULL), 
  ('20068', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-12 09:02:28.437000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"3","bid":"1"}', NULL, NULL), 
  ('20086', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-12 13:28:49.521000', '35', NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', '업데이트', '2023-04-12 13:35:01.199000'), 
  ('20093', NULL, NULL, '15606', '{
  "path" : "nzc2qzu2odfvbm5",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5"
}', '2023-04-12 13:29:18.663000', NULL, NULL, NULL, '{"path":"nzc2qzu2odfvbm5","mid":"4","bid":"1"}', NULL, NULL), 
  ('20122', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-12 15:42:22.382000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20128', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-12 15:43:50.693000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20139', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-12 15:47:19.960000', '35', '커넥트올웨이즈 도입고려하고 있음', '2023-04-12 15:56:25.846000', '{"path":"channel","mid":"4","bid":"1"}', '서비스 설명 완료
내부 검토 이후 다시 상담 희망함', '2023-04-12 15:59:40.447000'), 
  ('20158', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-12 16:02:25.096000', NULL, '기능 관련 상세 설명 요청함', '2023-04-12 16:03:13.429000', '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20169', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-12 16:09:49.186000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20215', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-12 16:25:22.687000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20240', NULL, NULL, '16091', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-12 18:08:16.068000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20272', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-13 10:38:19.666000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20312', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-13 11:06:07.129000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20355', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-13 14:30:56.531000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20366', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-13 16:43:55.846000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20459', NULL, NULL, '16926', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-14 14:42:16.982000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20469', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-14 14:43:36.017000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20478', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-14 14:44:30.630000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20546', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-17 09:27:14.921000', NULL, NULL, NULL, '{"path":"channel","mid":"3","bid":"1"}', NULL, NULL), 
  ('20559', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-17 09:30:25.494000', NULL, NULL, NULL, '{"path":"channel","mid":"3","bid":"1"}', NULL, NULL), 
  ('20566', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-17 09:36:19.201000', NULL, NULL, NULL, '{"path":"channel","mid":"3","bid":"1"}', NULL, NULL), 
  ('20573', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-17 09:39:07.058000', NULL, NULL, NULL, '{"path":"channel","mid":"3","bid":"1"}', NULL, NULL), 
  ('20576', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-17 09:45:27.885000', NULL, NULL, NULL, '{"path":"channel","mid":"3","bid":"1"}', NULL, NULL), 
  ('20592', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-17 09:52:12.066000', NULL, NULL, NULL, '{"path":"channel","mid":"3","bid":"1"}', NULL, NULL), 
  ('20611', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-17 16:34:15.749000', NULL, '메모테스트', '2023-04-18 17:13:25.025000', '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL), 
  ('20883', NULL, NULL, '15606', '{
  "path" : "channel",
  "name" : "connectbranch",
  "value" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_channel"
}', '2023-04-19 09:59:04.852000', NULL, NULL, NULL, '{"path":"channel","mid":"4","bid":"1"}', NULL, NULL);
UNLOCK TABLES;

