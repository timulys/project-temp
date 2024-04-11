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


CREATE TABLE `member` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `branch_id` bigint(20) NOT NULL COMMENT '브랜치 PK',
  `created` datetime(6) NOT NULL COMMENT '생성 일시',
  `creator` bigint(20) NOT NULL COMMENT '생성자',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL COMMENT '사용 여부',
  `first_message` varchar(4000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '웰컴 메세지',
  `managed` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '관리 가능 여부',
  `max_counsel` int(11) DEFAULT '0' COMMENT '최대 상담건수',
  `modified` datetime(6) NOT NULL COMMENT '수정 일시',
  `modifier` bigint(20) NOT NULL COMMENT '수정자',
  `nickname` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '이름',
  `password` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '로그인 비밀번호',
  `profile` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '프로파일',
  `setting` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '기타 환경설정',
  `status` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT 'on' COMMENT '상담 진행',
  `username` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '로그인 아이디',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gc3jmn7c2abyo3wf6syln5t2i` (`username`),
  KEY `IDX_MEMBER__SEARCH` (`branch_id`,`enabled`,`nickname`),
  KEY `IDX_MEMBER__USERNAME` (`username`),
  KEY `IDX_MEMBER__MANAGED` (`managed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES member WRITE;

INSERT INTO 
  member (id, branch_id, created, creator, enabled, first_message, managed, max_counsel, modified, modifier, nickname, password, profile, setting, status, username) 
VALUES 
  ('1', '1', '2023-03-16 13:19:03', '1', 'Y', NULL, 'N', '0', '2023-03-16 13:19:03', '1', '마스터1', '{noop}master1', NULL, NULL, 'off', 'master1'), 
  ('2', '1', '2023-03-16 13:19:03', '1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', 'Y', '0', '2023-03-30 16:13:51.129000', '2', '관리자1', '{bcrypt}$2a$10$oyQjNBK8Kv9Bg49abtxgEunUbn7oQ0cG4f07hLUkuh13cw0/9huxW', NULL, NULL, 'on', 'admin1'), 
  ('3', '1', '2023-03-16 13:19:03', '1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', 'Y', '50', '2023-03-21 10:49:02.131000', '3', '매니저1', '{noop}manager1', NULL, '{
  "enter_message_enabled" : true,
  "message_autocomplete_enabled" : false,
  "forbidden_word_enabled" : true
}', 'on', 'manager1'), 
  ('4', '1', '2023-03-16 13:19:03', '1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', 'Y', '50', '2023-04-12 16:21:33.652000', '4', '상담원1', '{noop}member1', NULL, '{
  "enter_message_enabled" : true,
  "message_autocomplete_enabled" : false
}', 'on', 'member1'), 
  ('5', '1', '2023-03-16 13:19:03', '1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg",
      "display" : "image"
    } ]
  } ]
}', 'Y', '50', '2023-04-06 19:00:47.232000', '5', '상담원2', '{noop}member2', NULL, '{
  "enter_message_enabled" : true,
  "message_autocomplete_enabled" : true
}', 'on', 'member2'), 
  ('15563', '15556', '2023-03-16 13:21:35.605000', '1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text"
    } ]
  } ]
}', 'Y', '50', '2023-03-16 14:25:00.882000', '15563', '판교점 관리자2', '{bcrypt}$2a$10$lrcRre86ADnP52qwBZGri.oxC5kzEhMYUJUg7iA9z8hq/jvW/3jCa', NULL, NULL, 'off', 'admin2'), 
  ('15565', '1', '2023-03-16 13:23:10.418000', '1', 'Y', NULL, 'Y', '50', '2023-03-16 13:23:10.426000', '1', '판교점 매니저2', '{bcrypt}$2a$10$UoCS582m1oVOrw19uXXnUep5FYn2NIkLsAC8Zv2sum6ExY.9Svxmu', NULL, NULL, 'off', 'manager2'), 
  ('15572', '15556', '2023-03-16 13:24:12.404000', '15563', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text"
    } ]
  } ]
}', 'Y', '50', '2023-03-16 13:24:32.779000', '15563', '판교점 매니저3', '{bcrypt}$2a$10$C/B5pzSRgk0kgFuFZARzquKhvSAOuT46WfnipZFKaFrzA3oq9pYy2', NULL, NULL, 'off', 'manager3'), 
  ('15579', '15556', '2023-03-16 13:25:04.300000', '15563', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text"
    } ]
  } ]
}', 'Y', '50', '2023-03-17 17:30:15.609000', '15579', '판교점 상담원4', '{bcrypt}$2a$10$uDBaZ/2GfzyBJj7h7BACde.lIpfVAKBIiyzStF7egue28L8stulQi', NULL, NULL, 'on', 'member4'), 
  ('15582', '15556', '2023-03-16 13:25:20.605000', '15563', 'Y', NULL, 'Y', '50', '2023-03-16 13:25:20.613000', '15563', '판교점 상담원5', '{bcrypt}$2a$10$v3mZj9unQDGFTq52YSsVE.LdZiDGQdw1v3jnq05xv796BO6hjZXSu', NULL, NULL, 'on', 'member5'), 
  ('15995', '15986', '2023-03-17 14:00:04.643000', '1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', 'Y', '50', '2023-03-24 16:26:05.152000', '15995', 'kepmember1', '{bcrypt}$2a$10$OqFAWtmablAucideauYtLOWg9AKJSKspoAzb5tNV0TeYdnQP1vHOa', NULL, '{
  "enter_message_enabled" : true,
  "message_autocomplete_enabled" : false
}', 'on', 'kepmember1'), 
  ('15998', '15986', '2023-03-17 14:00:18.347000', '1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎㅎㅎㅎ"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/afadac6d-3063-4916-92bf-e77d871a94eb.png",
      "display" : "image"
    } ]
  } ]
}', 'Y', '50', '2023-03-20 14:59:55.948000', '16002', 'kepmanager1', '{bcrypt}$2a$10$xAUOCOheOQGN.PhD9.z5QeHmmv/avJ9MZapgzuT9FeFhWtEgaqAGe', NULL, NULL, 'on', 'kepmanager1'), 
  ('16002', '15986', '2023-03-17 14:00:44.211000', '1', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text"
    } ]
  } ]
}', 'Y', '50', '2023-03-20 14:59:49.830000', '16002', 'kepadmin1', '{bcrypt}$2a$10$5YbId7YbSc/Zuh/L3XPP3O9Ee5Th4MPGhu6OOKibw7Qja6I4r8Hc2', NULL, NULL, 'off', 'kepadmin1'), 
  ('16282', '16275', '2023-03-20 14:13:11.044000', '1', 'Y', NULL, 'Y', '50', '2023-03-20 14:13:11.051000', '1', 'codeadmin1', '{bcrypt}$2a$10$ohFsD2EA59xwsHVA1K2Mx.6m8BVGutY0aQmiAZUAFw7l966872NVm', NULL, NULL, 'off', 'codeadmin1'), 
  ('16285', '16275', '2023-03-20 14:14:17.668000', '16282', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text"
    } ]
  } ]
}', 'Y', '50', '2023-03-20 14:14:43.131000', '16282', 'codemanager1', '{bcrypt}$2a$10$haaFcSPCiXjQKpmBvudm9.mkwcL/y2d59.TICdb5k3VtpsPl4uFWa', NULL, NULL, 'off', 'codemanager1'), 
  ('16293', '16275', '2023-03-20 14:14:59.596000', '16282', 'Y', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담사 인사말"
    } ]
  } ]
}', 'Y', '50', '2023-03-20 16:49:10.808000', '16293', 'codemember1', '{bcrypt}$2a$10$gvNYd8NuU89LiEfPrgrZMOlO.8HOkqVa55ioXclj24aKk/P1awG.q', NULL, '{
  "enter_message_enabled" : true,
  "message_autocomplete_enabled" : false
}', 'on', 'codemember1'), 
  ('16862', '15986', '2023-03-22 15:36:05.757000', '16002', 'Y', NULL, 'Y', '50', '2023-03-22 15:47:41.736000', '16862', 'kepmember2', '{bcrypt}$2a$10$.h/EKqI25Db72zr/CaBMjuvKYAu.F8IbBLIFMlGeMZBo4LHx24FfW', NULL, NULL, 'on', 'kepmember2'), 
  ('9000000001', '1', '2023-03-16 13:19:02', '1', 'N', NULL, 'N', '0', '2023-03-16 13:19:02', '1', '시스템', '{bcrypt}$2a$10$WMhur0a3BIrHefd3w7Xlge2WowQDIr71WRtBXYOZ/7P/gLu1lKvim', NULL, NULL, 'off', 'system'), 
  ('9000000002', '1', '2023-03-16 13:19:02', '1', 'N', NULL, 'N', '0', '2023-03-16 13:19:02', '1', '오픈빌더', '{bcrypt}$2a$10$ddRQcGFDi/Ng0pwztKnTS.4vnTg1df1o4MNeWLWfBIAKh.ZZRpmR2', NULL, NULL, 'off', 'openbuilder');
UNLOCK TABLES;

