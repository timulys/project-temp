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


CREATE TABLE `hotkey` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `content` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '사용할 문구 내용',
  `created` datetime(6) DEFAULT NULL COMMENT '생성한 시간',
  `enabled` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '사용여부',
  `first_hot_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '단축키 첫번째',
  `hotkey_code` int(11) NOT NULL COMMENT '단축키 두번째의 키코드',
  `member_id` bigint(20) DEFAULT NULL COMMENT '사용하는 직원 PK',
  `modified` datetime(6) DEFAULT NULL COMMENT '수정된 시간',
  `second_hot_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '단축키 두번째',
  PRIMARY KEY (`id`),
  KEY `IDX_HOTKEY_MEMBER` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES hotkey WRITE;

INSERT INTO 
  hotkey (id, content, created, enabled, first_hot_key, hotkey_code, member_id, modified, second_hot_key) 
VALUES 
  ('18560', '반갑습니다.', '2023-04-04 16:49:07.558000', 'Y', 'ALT', '49', '5', '2023-04-04 16:49:07.557000', '1'), 
  ('18561', '안녕하세요???', '2023-04-04 16:49:07.576000', 'Y', 'ALT', '50', '5', '2023-04-04 16:49:07.576000', '2'), 
  ('18562', '수정된건가요', '2023-04-04 16:49:07.580000', 'Y', 'ALT', '51', '5', '2023-04-04 16:49:07.580000', '3'), 
  ('18563', '안녕히 가세요!!', '2023-04-04 16:49:07.583000', 'Y', 'ALT', '52', '5', '2023-04-04 16:49:07.583000', '4'), 
  ('18564', '테스트용입니다!!.', '2023-04-04 16:49:07.587000', 'Y', 'ALT', '53', '5', '2023-04-04 16:49:07.587000', '5'), 
  ('18565', '테스트용입니까', '2023-04-04 16:49:07.591000', 'Y', 'ALT', '54', '5', '2023-04-04 16:49:07.591000', '6'), 
  ('18567', '테스트용입니까', '2023-04-04 16:49:07.598000', 'Y', 'ALT', '56', '5', '2023-04-04 16:49:07.598000', '8'), 
  ('18568', '테스트용입니까', '2023-04-04 16:49:07.602000', 'Y', 'ALT', '57', '5', '2023-04-04 16:49:07.602000', '9');
UNLOCK TABLES;

