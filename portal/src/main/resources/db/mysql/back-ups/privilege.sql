-- --------------------------------------------------------------
-- QueryPie Schema Export Script
-- https://www.querypie.com
--
-- Database: connectbranch
-- Generated At: 04/19/2023 04:55:18 +00:00
-- --------------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `privilege` (
                             `id` bigint(20) NOT NULL COMMENT 'PK',
                             `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '권한 이름',
                             `type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '권한 타입',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `UK_gp5so3rt5kpu1hhv6vavrarcl` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES privilege WRITE;

INSERT INTO
    privilege (id, name, type)
VALUES
    ('100', '상담 포털 조회', 'READ_PORTAL'),
    ('110', '홈 조회', 'READ_HOME'),
    ('120', '고객 조회', 'READ_CUSTOMER'),
    ('121', '알림톡', 'WRITE_KAKAO_ALERT_TALK'),
    ('122', '친구톡', 'WRITE_KAKAO_FRIEND_TALK'),
    ('123', '선물하기 (카카오)', 'WRITE_KAKAO_GIFT'),
    ('124', '선물하기 (고객사)', 'WRITE_LEGACY_GIFT'),
    ('130', '이슈 조회', 'READ_ISSUE'),
    ('131', '이슈 수정', 'WRITE_ISSUE'),
    ('132', '상담 가이드 조회', 'READ_GUIDE'),
    ('133', '상품 설명서 조회', 'READ_MANUAL'),
    ('200', '상담 관리 조회', 'READ_MANAGE'),
    ('211', '대시보드 조회', 'READ_DASHBOARD'),
    ('221', '상담 지원 요청 수정', 'WRITE_SUPPORT'),
    ('222', '톡 발송 요청 수정', 'WRITE_TALK'),
    ('231', '상담 진행 목록 수정', 'WRITE_ISSUE_OPEN'),
    ('232', '상담 이력 수정', 'WRITE_ISSUE_HISTORY'),
    ('233', '톡 발송 이력 조회', 'READ_TALK_HISTORY'),
    ('234', '톡 예약 목록 수정', 'WRITE_TALK_TASK'),
    ('235', '파일 전송 이력 조회', 'READ_FILE_HISTORY'),
    ('241', '공지사항 수정', 'WRITE_NOTICE'),
    ('242', '상담 가이드 수정', 'WRITE_GUIDE'),
    ('243', '상담 가이드 분류 수정', 'WRITE_GUIDE_CATEGORY'),
    ('245', '상품 설명서 수정', 'WRITE_MANUAL'),
    ('246', '템플릿 관리 수정', 'WRITE_PLATFORM_TEMPLATE'),
    ('247', '상담 보조 컨텐츠 수정', 'WRITE_CONTENTS'),
    ('251', '톡 관리 수정', 'WRITE_TALK_MANAGE'),
    ('261', '통계 조회', 'READ_STATISTICS'),
    ('300', '시스템 설정 조회', 'READ_SYSTEM'),
    ('311', '권한 수정', 'WRITE_PRIVILEGE'),
    ('312', '브렌치 수정', 'WRITE_BRANCH'),
    ('313', '채널 수정', 'WRITE_CHANNEL'),
    ('314', '계정 수정', 'WRITE_ACCOUNT'),
    ('321', '근무 조건 수정', 'WRITE_WORK'),
    ('322', '상담 환경 수정', 'WRITE_COUNSEL'),
    ('323', '자동 메세지 수정', 'WRITE_AUTO_MESSAGE'),
    ('325', '상담 배분 수정', 'WRITE_ASSIGN'),
    ('391', '시스템 이용 내역 조회', 'READ_LOG');
UNLOCK TABLES;

