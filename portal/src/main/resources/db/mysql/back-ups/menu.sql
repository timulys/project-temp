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


CREATE TABLE `menu` (
                        `id` bigint(20) NOT NULL COMMENT 'PK',
                        `depth` int(11) NOT NULL COMMENT '계층',
                        `disabled_levels` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '선택 불가 레벨 목록 (CSV)',
                        `display` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '프론트 표현',
                        `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
                        `link` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '링크',
                        `master_enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '마스터 사용 여부',
                        `name1` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '1계층 이름',
                        `name2` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '2계층 이름',
                        `name3` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '3계층 이름',
                        `name4` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '4계층 이름',
                        `role_enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '역할 사용 여부',
                        `sort` int(11) NOT NULL COMMENT '정렬',
                        `top_id` bigint(20) DEFAULT NULL COMMENT '1계층 메뉴 PK',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES menu WRITE;

INSERT INTO
    menu (id, depth, disabled_levels, display, enabled, link, master_enabled, name1, name2, name3, name4, role_enabled, sort, top_id)
VALUES
    ('100', '1', NULL, NULL, 'Y', NULL, 'N', '상담포털', NULL, NULL, NULL, 'Y', '100', '100'),
    ('110', '2', NULL, NULL, 'Y', NULL, 'N', '상담포털', '홈', NULL, NULL, 'Y', '110', '100'),
    ('120', '2', NULL, NULL, 'Y', NULL, 'N', '상담포털', '고객목록', NULL, NULL, 'Y', '120', '100'),
    ('121', '3', NULL, NULL, 'Y', NULL, 'N', '상담포털', '고객목록', '알림톡', NULL, 'Y', '121', '100'),
    ('122', '3', NULL, NULL, 'Y', NULL, 'N', '상담포털', '고객목록', '친구톡', NULL, 'Y', '122', '100'),
    ('123', '3', NULL, NULL, 'Y', NULL, 'N', '상담포털', '고객목록', '선물하기', NULL, 'Y', '123', '100'),
    ('130', '2', NULL, NULL, 'Y', NULL, 'N', '상담포털', '대화목록', NULL, NULL, 'Y', '130', '100'),
    ('131', '3', NULL, NULL, 'Y', NULL, 'N', '상담포털', '대화목록', '상담이력', NULL, 'Y', '131', '100'),
    ('132', '3', NULL, NULL, 'Y', NULL, 'N', '상담포털', '대화목록', '상담가이드', NULL, 'Y', '132', '100'),
    ('133', '3', NULL, NULL, 'Y', NULL, 'N', '상담포털', '대화목록', '상품설명서', NULL, 'Y', '133', '100'),
    ('200', '1', NULL, NULL, 'Y', NULL, 'N', '상담관리', NULL, NULL, NULL, 'Y', '200', '200'),
    ('210', '2', 'OPERATOR', NULL, 'Y', NULL, 'N', '상담관리', '대시보드', NULL, NULL, 'Y', '210', '200'),
    ('211', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '상담관리', '대시보드', '대시보드', NULL, 'Y', '211', '200'),
    ('220', '2', NULL, NULL, 'Y', NULL, 'N', '상담관리', '요청관리', NULL, NULL, 'Y', '220', '200'),
    ('221', '3', NULL, NULL, 'Y', NULL, 'N', '상담관리', '요청관리', '상담 지원 요청', NULL, 'Y', '221', '200'),
    ('222', '3', NULL, NULL, 'Y', NULL, 'N', '상담관리', '요청관리', '톡 발송 요청', NULL, 'Y', '222', '200'),
    ('230', '2', NULL, NULL, 'Y', NULL, 'N', '상담관리', '상담', NULL, NULL, 'Y', '230', '200'),
    ('231', '3', NULL, NULL, 'Y', NULL, 'N', '상담관리', '상담', '상담 진행 목록', NULL, 'Y', '231', '200'),
    ('232', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '상담관리', '상담', '상담 이력', NULL, 'Y', '232', '200'),
    ('233', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '상담관리', '상담', '톡 발송 이력', NULL, 'Y', '233', '200'),
    ('234', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '상담관리', '상담', '톡 발송 예약 목록', NULL, 'Y', '234', '200'),
    ('235', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '상담관리', '상담', '파일 전송 이력', NULL, 'Y', '235', '200'),
    ('240', '2', NULL, NULL, 'Y', NULL, 'N', '상담관리', '상담지원', NULL, NULL, 'Y', '240', '200'),
    ('241', '3', NULL, NULL, 'Y', NULL, 'N', '상담관리', '상담지원', '공지사항', NULL, 'Y', '241', '200'),
    ('242', '3', NULL, NULL, 'Y', NULL, 'N', '상담관리', '상담지원', '상담가이드', NULL, 'Y', '242', '200'),
    ('245', '3', NULL, NULL, 'N', NULL, 'N', '상담관리', '상담지원', '상품설명서', NULL, 'Y', '245', '200'),
    ('246', '3', NULL, NULL, 'Y', NULL, 'N', '상담관리', '상담지원', '템플릿 관리', NULL, 'Y', '246', '200'),
    ('247', '3', NULL, NULL, 'Y', NULL, 'N', '상담관리', '상담지원', '상담 보조 콘텐츠', NULL, 'Y', '247', '200'),
    ('250', '2', NULL, NULL, 'N', NULL, 'N', '상담관리', '톡 발송 관리', NULL, NULL, 'Y', '250', '200'),
    ('251', '3', NULL, NULL, 'N', NULL, 'N', '상담관리', '톡 발송 관리', '톡 발송 관리', NULL, 'Y', '251', '200'),
    ('260', '2', NULL, NULL, 'N', NULL, 'N', '상담관리', '통계', NULL, NULL, 'Y', '260', '200'),
    ('261', '3', NULL, NULL, 'N', NULL, 'N', '상담관리', '통계', '통계', NULL, 'Y', '261', '200'),
    ('300', '1', 'OPERATOR', NULL, 'Y', NULL, 'Y', '시스템설정', NULL, NULL, NULL, 'Y', '300', '300'),
    ('310', '2', 'OPERATOR', NULL, 'Y', NULL, 'Y', '시스템설정', '계정 관리', NULL, NULL, 'Y', '310', '300'),
    ('311', '3', 'OPERATOR', NULL, 'Y', NULL, 'Y', '시스템설정', '계정 관리', '권한 관리', NULL, 'N', '311', '300'),
    ('312', '3', 'OPERATOR', NULL, 'Y', NULL, 'Y', '시스템설정', '계정 관리', '브랜치/채널 관리', NULL, 'N', '312', '300'),
    ('314', '3', 'OPERATOR', NULL, 'Y', NULL, 'Y', '시스템설정', '계정 관리', '계정 관리', NULL, 'Y', '314', '300'),
    ('320', '2', 'OPERATOR', NULL, 'Y', NULL, 'N', '시스템설정', '상담 설정', NULL, NULL, 'Y', '320', '300'),
    ('321', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '시스템설정', '상담 설정', '근무 조건 설정', NULL, 'Y', '321', '300'),
    ('322', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '시스템설정', '상담 설정', '상담 환경 설정', NULL, 'Y', '322', '300'),
    ('323', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '시스템설정', '상담 설정', '자동메세지 설정', NULL, 'Y', '323', '300'),
    ('325', '3', 'OPERATOR', NULL, 'Y', NULL, 'N', '시스템설정', '상담 설정', '상담 배분 설정', NULL, 'Y', '325', '300'),
    ('390', '2', 'OPERATOR', NULL, 'N', NULL, 'N', '시스템설정', '서비스 관리', NULL, NULL, 'Y', '390', '300'),
    ('391', '3', 'OPERATOR', NULL, 'N', NULL, 'Y', '시스템설정', '서비스 관리', '서비스 이용 내역', NULL, 'Y', '391', '300');
UNLOCK TABLES;

