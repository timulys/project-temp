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


CREATE TABLE `issue_support_history` (
  `id` bigint(20) NOT NULL COMMENT '상담 이력 PK',
  `branch_id` bigint(20) DEFAULT NULL COMMENT '시스템전환시 브랜치 PK',
  `change_id` bigint(20) DEFAULT NULL COMMENT '상담직원선택 - 유저 PK, 시스템전환 - 소분류 카테고리 PK, 자동배정(전환승인) - 유저 PK',
  `change_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '상담직원전환 구분 값',
  `content` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '상담 검토/직원전환 요청/답변 내용',
  `created` datetime(6) NOT NULL COMMENT '등록 시간',
  `creator` bigint(20) NOT NULL COMMENT '등록 유저 PK',
  `issue_id` bigint(20) NOT NULL COMMENT '이슈 PK',
  `issue_support_id` bigint(20) DEFAULT NULL COMMENT '상담 지원 요청 PK',
  `status` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '상담 검토/직원전환 요청 상태',
  `type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '상담 요청 구분 - 상담검토요청, 상담직원전환요청',
  PRIMARY KEY (`id`),
  KEY `IDX_ISSUE_SUPPORT_HISTORY__SEARCH` (`issue_id`),
  KEY `IDX_ISSUE_SUPPORT_HISTORY__SEARCH_DETAIL` (`created`,`issue_id`),
  KEY `IDX_ISSUE_SUPPORT_HISTORY__SEARCH_SUPPORT` (`issue_support_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES issue_support_history WRITE;

INSERT INTO 
  issue_support_history (id, branch_id, change_id, change_type, content, created, creator, issue_id, issue_support_id, status, type) 
VALUES 
  ('15597', NULL, NULL, NULL, '검토 요청', '2023-03-16 13:28:33.252000', '4', '15568', '15596', 'request', 'question'), 
  ('15602', NULL, NULL, NULL, '코끼리', '2023-03-16 13:29:08.543000', '3', '15568', '15596', 'end', 'question'), 
  ('15685', NULL, NULL, NULL, 'ㅁㅁ', '2023-03-16 18:29:22.123000', '4', '15678', '15684', 'request', 'question'), 
  ('15751', NULL, NULL, NULL, '안녕하세요', '2023-03-17 10:14:21.377000', '5', '15715', '15750', 'request', 'question'), 
  ('15753', NULL, '3', NULL, NULL, '2023-03-17 10:15:20.018000', '3', '15715', '15750', 'receive', 'question'), 
  ('15847', NULL, '4', 'select', '2이에서 1로', '2023-03-17 11:14:08.435000', '5', '15836', '15846', 'request', 'change'), 
  ('15849', NULL, '4', 'select', NULL, '2023-03-17 11:14:27.073000', '3', '15836', '15846', 'change', 'change'), 
  ('16057', '1', '35', 'auto', '', '2023-03-17 14:37:27.600000', '4', '16050', '16056', 'request', 'change'), 
  ('16505', NULL, '15998', 'select', '', '2023-03-20 16:36:51.970000', '15995', '16498', '16504', 'request', 'change'), 
  ('16507', NULL, '15998', 'select', NULL, '2023-03-20 16:36:53.036000', '9000000001', '16498', '16504', 'auto', 'change'), 
  ('16532', NULL, '15998', 'select', '', '2023-03-20 16:39:34.474000', '15995', '16523', '16531', 'request', 'change'), 
  ('16534', NULL, '15998', 'select', NULL, '2023-03-20 16:39:35.523000', '9000000001', '16523', '16531', 'auto', 'change'), 
  ('16554', NULL, '15995', 'select', '', '2023-03-20 16:41:18.439000', '4', '16545', '16553', 'request', 'change'), 
  ('16564', NULL, '3', 'select', '', '2023-03-20 16:44:02.105000', '4', '16557', '16563', 'request', 'change'), 
  ('16580', NULL, '3', 'select', '', '2023-03-20 16:56:13.804000', '16293', '16304', '16579', 'request', 'change'), 
  ('16582', NULL, '3', 'select', NULL, '2023-03-20 16:56:14.855000', '9000000001', '16304', '16579', 'auto', 'change'), 
  ('16966', NULL, NULL, NULL, '상담 검토 요청해주세요 꼼꼼하게 봐주세요 확실하게 봐주세요 ', '2023-03-24 16:37:12.259000', '15995', '16948', '16965', 'request', 'question'), 
  ('16969', '15986', '35', 'auto', '상담직원전환 테스트 ', '2023-03-24 16:37:43.241000', '15995', '16948', '16968', 'request', 'change'), 
  ('16971', NULL, '15998', 'auto', NULL, '2023-03-24 16:37:44.374000', '9000000001', '16948', '16968', 'auto', 'change'), 
  ('18903', NULL, NULL, NULL, '전문 상담 내용이 많아 내용 검토가 필요해요', '2023-04-05 21:10:28.223000', '5', '18874', '18902', 'request', 'question'), 
  ('18905', NULL, '2', NULL, NULL, '2023-04-05 21:10:49.183000', '2', '18874', '18902', 'receive', 'question'), 
  ('19906', NULL, '5', 'select', 'ddd', '2023-04-11 10:38:47.890000', '4', '19670', '19905', 'request', 'change'), 
  ('20083', NULL, '5', 'select', NULL, '2023-04-12 11:08:57.142000', '3', '19670', '19905', 'end', 'change'), 
  ('20227', NULL, NULL, NULL, '고객님 욕설이 많아 상담이 어렵습니다 내용 검토 부탁드립니다. ', '2023-04-12 16:28:43.870000', '4', '20216', '20226', 'request', 'question'), 
  ('20229', NULL, '3', NULL, '제가 이어 받을께요', '2023-04-12 16:29:07.335000', '3', '20216', '20226', 'receive', 'question'), 
  ('20260', NULL, NULL, NULL, '강제종료해주세요 ', '2023-04-13 10:35:30.686000', '5', '19420', '20259', 'request', 'question'), 
  ('20262', NULL, NULL, NULL, NULL, '2023-04-13 10:36:15.772000', '3', '19420', '20259', 'end', 'question'), 
  ('20266', NULL, NULL, NULL, '강제종료해주세요 ', '2023-04-13 10:36:50.400000', '4', '20241', '20265', 'request', 'question'), 
  ('20269', NULL, NULL, NULL, NULL, '2023-04-13 10:37:18.120000', '3', '20241', '20265', 'end', 'question'), 
  ('20491', NULL, NULL, NULL, '나 잘 모르겠어 니가 좁 확인해봐 관리자
', '2023-04-14 14:46:28.093000', '4', '20460', '20490', 'request', 'question'), 
  ('20494', NULL, NULL, NULL, '그냥 니가 알아서해', '2023-04-14 14:47:46.626000', '3', '20460', '20490', 'finish', 'question'), 
  ('20497', NULL, '16293', 'select', '관리자 니가 잘 얘기 안해줘서 나 상담 못해 나보다 더 잘하는 상담직원에게 넘겨줘', '2023-04-14 14:48:38.404000', '4', '20460', '20496', 'request', 'change'), 
  ('20499', NULL, '3', 'select', NULL, '2023-04-14 14:49:17.129000', '3', '20460', '20496', 'receive', 'change'), 
  ('20727', NULL, NULL, NULL, '욕했어용 ', '2023-04-18 14:59:06.997000', '5', '20719', '20726', 'request', 'question'), 
  ('20729', NULL, NULL, NULL, '안했어용', '2023-04-18 14:59:51.973000', '3', '20719', '20726', 'reject', 'question');
UNLOCK TABLES;

