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


CREATE TABLE `issue_support` (
  `id` bigint(20) NOT NULL COMMENT '상담 지원 요청 PK',
  `answer` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '그룹장 답변 내용',
  `answer_modified` datetime(6) DEFAULT NULL COMMENT '그룹장 답변 시간',
  `answerer` bigint(20) DEFAULT NULL COMMENT '그룹장 유저 PK',
  `auto_id` bigint(20) DEFAULT NULL COMMENT '시스템전환시 자동 배정된 유저 PK',
  `branch_id` bigint(20) DEFAULT NULL COMMENT '시스템전환시 브랜치 PK',
  `change_id` bigint(20) DEFAULT NULL COMMENT '상담직원선택 - 유저 PK, 시스템전환 - 소분류 카테고리 PK',
  `change_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '상담직원전환 구분 값',
  `created` datetime(6) NOT NULL COMMENT '등록 시간',
  `creator` bigint(20) NOT NULL COMMENT '등록 유저 PK',
  `question` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '상담 검토/직원전환 요청 내용',
  `question_modified` datetime(6) DEFAULT NULL COMMENT '상담 검토/직원전환 요청 시간',
  `questioner` bigint(20) DEFAULT NULL COMMENT '상담 검토/직원전환 요청 유저 PK',
  `status` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '상담 검토/직원전환 요청 상태',
  `type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '상담 요청 구분 - 상담검토요청, 상담직원전환요청',
  `issue_id` bigint(20) NOT NULL COMMENT '이슈 PK',
  PRIMARY KEY (`id`),
  KEY `IDX_ISSUE_SUPPORT__SEARCH` (`question_modified`,`questioner`),
  KEY `FK_ISSUE_SUPPORT__ISSUE` (`issue_id`),
  KEY `IDX_ISSUE_SUPPORT__SEARCH_MEMBER` (`questioner`,`question_modified`,`created`),
  KEY `IDX_ISSUE_SUPPORT__SEARCH_DATE` (`question_modified`,`questioner`,`created`),
  CONSTRAINT `FK_ISSUE_SUPPORT__ISSUE` FOREIGN KEY (`issue_id`) REFERENCES `issue` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES issue_support WRITE;

INSERT INTO 
  issue_support (id, answer, answer_modified, answerer, auto_id, branch_id, change_id, change_type, created, creator, question, question_modified, questioner, status, type, issue_id) 
VALUES 
  ('15596', '코끼리', '2023-03-16 13:29:08.543000', '3', NULL, NULL, NULL, NULL, '2023-03-16 13:28:33.216000', '4', '검토 요청', '2023-03-16 13:28:33.216000', '4', 'end', 'question', '15568'), 
  ('15684', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2023-03-16 18:29:22.094000', '4', 'ㅁㅁ', '2023-03-16 18:29:22.094000', '4', 'request', 'question', '15678'), 
  ('15750', NULL, '2023-03-17 10:15:20.018000', '3', NULL, NULL, '3', NULL, '2023-03-17 10:14:21.370000', '5', '안녕하세요', '2023-03-17 10:14:21.370000', '5', 'receive', 'question', '15715'), 
  ('15846', NULL, '2023-03-17 11:14:27.073000', '3', NULL, NULL, '4', 'select', '2023-03-17 11:14:08.428000', '5', '2이에서 1로', '2023-03-17 11:14:08.428000', '5', 'change', 'change', '15836'), 
  ('16056', NULL, NULL, NULL, NULL, '1', '35', 'auto', '2023-03-17 14:37:27.594000', '4', '', '2023-03-17 14:37:27.594000', '4', 'request', 'change', '16050'), 
  ('16504', NULL, '2023-03-20 16:36:51.946000', '9000000001', NULL, NULL, '15998', 'select', '2023-03-20 16:36:51.944000', '15995', '', '2023-03-20 16:36:51.944000', '15995', 'auto', 'change', '16498'), 
  ('16531', NULL, '2023-03-20 16:39:34.469000', '9000000001', NULL, NULL, '15998', 'select', '2023-03-20 16:39:34.467000', '15995', '', '2023-03-20 16:39:34.467000', '15995', 'auto', 'change', '16523'), 
  ('16553', NULL, NULL, NULL, NULL, NULL, '15995', 'select', '2023-03-20 16:41:18.433000', '4', '', '2023-03-20 16:41:18.433000', '4', 'request', 'change', '16545'), 
  ('16563', NULL, NULL, NULL, NULL, NULL, '3', 'select', '2023-03-20 16:44:02.099000', '4', '', '2023-03-20 16:44:02.099000', '4', 'request', 'change', '16557'), 
  ('16579', NULL, '2023-03-20 16:56:13.799000', '9000000001', NULL, NULL, '3', 'select', '2023-03-20 16:56:13.798000', '16293', '', '2023-03-20 16:56:13.798000', '16293', 'auto', 'change', '16304'), 
  ('16965', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2023-03-24 16:37:12.232000', '15995', '상담 검토 요청해주세요 꼼꼼하게 봐주세요 확실하게 봐주세요 ', '2023-03-24 16:37:12.232000', '15995', 'request', 'question', '16948'), 
  ('16968', NULL, '2023-03-24 16:37:43.236000', '9000000001', NULL, '15986', '35', 'auto', '2023-03-24 16:37:43.234000', '15995', '상담직원전환 테스트 ', '2023-03-24 16:37:43.234000', '15995', 'auto', 'change', '16948'), 
  ('18902', NULL, '2023-04-05 21:10:49.183000', '2', NULL, NULL, '2', NULL, '2023-04-05 21:10:28.194000', '5', '전문 상담 내용이 많아 내용 검토가 필요해요', '2023-04-05 21:10:28.194000', '5', 'receive', 'question', '18874'), 
  ('19905', NULL, '2023-04-12 11:08:57.141000', '3', NULL, NULL, '5', 'select', '2023-04-11 10:38:47.863000', '4', 'ddd', '2023-04-11 10:38:47.863000', '4', 'end', 'change', '19670'), 
  ('20226', '제가 이어 받을께요', '2023-04-12 16:29:07.334000', '3', NULL, NULL, '3', NULL, '2023-04-12 16:28:43.865000', '4', '고객님 욕설이 많아 상담이 어렵습니다 내용 검토 부탁드립니다. ', '2023-04-12 16:28:43.865000', '4', 'receive', 'question', '20216'), 
  ('20259', NULL, '2023-04-13 10:36:15.771000', '3', NULL, NULL, NULL, NULL, '2023-04-13 10:35:30.677000', '5', '강제종료해주세요 ', '2023-04-13 10:35:30.677000', '5', 'end', 'question', '19420'), 
  ('20265', NULL, '2023-04-13 10:37:18.120000', '3', NULL, NULL, NULL, NULL, '2023-04-13 10:36:50.395000', '4', '강제종료해주세요 ', '2023-04-13 10:36:50.395000', '4', 'end', 'question', '20241'), 
  ('20490', '그냥 니가 알아서해', '2023-04-14 14:47:46.626000', '3', NULL, NULL, NULL, NULL, '2023-04-14 14:46:28.057000', '4', '나 잘 모르겠어 니가 좁 확인해봐 관리자
', '2023-04-14 14:46:28.057000', '4', 'finish', 'question', '20460'), 
  ('20496', NULL, '2023-04-14 14:49:17.129000', '3', NULL, NULL, '3', 'select', '2023-04-14 14:48:38.397000', '4', '관리자 니가 잘 얘기 안해줘서 나 상담 못해 나보다 더 잘하는 상담직원에게 넘겨줘', '2023-04-14 14:48:38.397000', '4', 'receive', 'change', '20460'), 
  ('20726', '안했어용', '2023-04-18 14:59:51.973000', '3', NULL, NULL, NULL, NULL, '2023-04-18 14:59:06.973000', '5', '욕했어용 ', '2023-04-18 14:59:06.973000', '5', 'reject', 'question', '20719');
UNLOCK TABLES;

