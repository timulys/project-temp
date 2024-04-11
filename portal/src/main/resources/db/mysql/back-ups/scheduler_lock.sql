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


CREATE TABLE `scheduler_lock` (
  `name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `lock_until` timestamp(3) NULL DEFAULT NULL,
  `locked_at` timestamp(3) NULL DEFAULT NULL,
  `locked_by` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES scheduler_lock WRITE;

INSERT INTO 
  scheduler_lock (name, lock_until, locked_at, locked_by) 
VALUES 
  ('CLOSE_WARNED_ISSUE', '2023-04-19 13:54:58.390', '2023-04-19 13:54:53.391', 'connect-branch-was.kep.k9d.in'), 
  ('ISSUE_URGENT_STATUS', '2023-04-19 13:54:58.406', '2023-04-19 13:54:53.407', 'connect-branch-was.kep.k9d.in'), 
  ('MAKE_STATISTICS_DATA', '2023-04-19 11:30:10', '2023-04-19 11:30:05.001', 'connect-branch-was.kep.k9d.in'), 
  ('RUN_LOCK_TEST', '2023-02-23 14:04:27.437', '2023-02-23 14:03:37.438', 'connect-branch-was.kep.k9d.in'), 
  ('SEND_DELAY_FIRST_REPLY', '2023-04-19 13:55:14.272', '2023-04-19 13:55:09.273', 'connect-branch-was.kep.k9d.in'), 
  ('SEND_DELAY_GUEST', '2023-04-19 13:54:58.408', '2023-04-19 13:54:53.409', 'connect-branch-was.kep.k9d.in'), 
  ('SEND_WARNING_DELAY_GUEST', '2023-04-19 13:55:13.889', '2023-04-19 13:55:08.890', 'connect-branch-was.kep.k9d.in'), 
  ('TRY_ASSIGN_OPENED_ISSUE', '2023-04-19 13:55:17.245', '2023-04-19 13:55:12.246', 'connect-branch-was.kep.k9d.in');
UNLOCK TABLES;

