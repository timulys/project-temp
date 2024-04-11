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


CREATE TABLE `team_office_hours` (
  `id` bigint(20) NOT NULL,
  `created` datetime(6) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `day_of_week` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `end_counsel_time` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `modified` datetime(6) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `start_counsel_time` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_TEAM_OFFICE_HOURS__SEARCH` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES team_office_hours WRITE;

UNLOCK TABLES;

