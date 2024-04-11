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

CREATE TABLE `assign_method` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL COMMENT '사용유무',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SERVICE ASSIGN 명',
  `signature` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SERVICE ASSIGN 파일 명',
  `sort` int(11) NOT NULL COMMENT '정렬',
  PRIMARY KEY (`id`),
  KEY `IDX_ASSIGN__SORT` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES assign_method WRITE;

INSERT INTO 
  assign_method (id, enabled, name, signature, sort) 
VALUES 
  ('10', 'Y', '시스템 근무시간 체크', 'assignBySystemOfficeHours', '10'), 
  ('20', 'Y', '상담 불가 체크', 'assignByDisabled', '20'), 
  ('30', 'Y', '배정 대기 건수 체크', 'assignByOpenedCount', '30'), 
  ('40', 'Y', '브랜치 상담원 구하기', 'assignByBranchMember', '60'), 
  ('50', 'Y', '카테고리 상담원 구하기', 'assignByIssueCategoryMember', '50'), 
  ('60', 'Y', '상담원 한명 구하기 ', 'assignByGetMember', '40'), 
  ('70', 'Y', '상담원 근무시간 체크', 'assignByMemberOfficeHours', '70'), 
  ('80', 'N', '상담원 근무외 시간 체크', 'assignOffDutyHours', '80'), 
  ('1000', 'Y', '상담원 랜덤 한명구하기', 'assignByMemberRandom', '1000');
UNLOCK TABLES;

