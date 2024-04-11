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


CREATE TABLE `branch_off_duty_hours` (
  `id` bigint(20) NOT NULL,
  `contents` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `created` datetime(6) NOT NULL,
  `creator` bigint(20) NOT NULL,
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'N',
  `end_created` datetime(6) NOT NULL,
  `modified` datetime(6) NOT NULL,
  `modifier` bigint(20) NOT NULL,
  `start_created` datetime(6) NOT NULL,
  `branch_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_BRANCH_OFF_DUTY_HOURS` (`branch_id`,`start_created`,`end_created`),
  KEY `IDX_BRANCH_OFF_DUTY_HOURS__SEARCH` (`branch_id`,`start_created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES branch_off_duty_hours WRITE;

INSERT INTO 
  branch_off_duty_hours (id, contents, created, creator, enabled, end_created, modified, modifier, start_created, branch_id) 
VALUES 
  ('17828', '휴가', '2023-03-30 17:21:01.949000', '2', 'Y', '2023-03-03 18:00:00', '2023-03-30 17:21:01.949000', '2', '2023-03-03 09:00:00', '1'), 
  ('17831', '체육대회', '2023-03-30 18:06:53.369000', '2', 'Y', '2023-03-04 18:00:00', '2023-03-30 18:06:53.369000', '2', '2023-03-04 09:00:00', '1'), 
  ('18115', '바빠서', '2023-04-03 18:48:56.568000', '2', 'Y', '2023-04-03 19:10:00', '2023-04-03 18:48:56.568000', '2', '2023-04-03 18:00:00', '1');
UNLOCK TABLES;

