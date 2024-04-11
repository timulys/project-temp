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


CREATE TABLE `issue_category_member` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `branch_id` bigint(20) NOT NULL COMMENT '브랜치 PK',
  `channel_id` bigint(20) NOT NULL COMMENT '채널 PK',
  `issue_category_id` bigint(20) NOT NULL COMMENT '분류 PK',
  `member_id` bigint(20) NOT NULL COMMENT '유저 PK',
  `modified` datetime(6) NOT NULL COMMENT '수정 일시',
  `modifier` bigint(20) NOT NULL COMMENT '수정자',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_ISSUE_CATEGORY_MEMBER__SEARCH` (`branch_id`,`channel_id`,`issue_category_id`,`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES issue_category_member WRITE;

UNLOCK TABLES;

