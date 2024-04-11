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


CREATE TABLE `member_office_hours` (
  `id` bigint(20) NOT NULL,
  `created` datetime(6) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `day_of_week` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `end_counsel_time` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `modified` datetime(6) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `start_counsel_time` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `member_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o0njdkgcl43ce3dkodf221rst` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES member_office_hours WRITE;

INSERT INTO 
  member_office_hours (id, created, creator, day_of_week, end_counsel_time, modified, modifier, start_counsel_time, member_id) 
VALUES 
  ('15649', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-16 14:25:00.890000', '15563', '8:0', '15563'), 
  ('15650', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-16 14:25:00.890000', '15563', '8:0', '1'), 
  ('15651', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI,SAT', '23:25', '2023-03-30 16:13:51.133000', '2', '8:0', '2'), 
  ('15652', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-21 10:49:02.134000', '3', '8:0', '3'), 
  ('15653', '2023-03-16 14:25:00.890000', '15563', 'TUE,WED,THUR,FRI,MON', '20:0', '2023-04-12 16:21:33.654000', '4', '8:0', '4'), 
  ('15654', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-04-06 19:00:47.237000', '5', '8:0', '5'), 
  ('15655', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-16 14:25:00.890000', '15563', '8:0', '15582'), 
  ('15656', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-17 17:30:15.612000', '15579', '8:0', '15579'), 
  ('15657', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-16 14:25:00.890000', '15563', '8:0', '15572'), 
  ('15658', '2023-03-16 14:25:00.890000', '15563', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-16 14:25:00.890000', '15563', '8:0', '15565'), 
  ('15997', '2023-03-17 14:00:04.688000', '1', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-24 16:26:05.156000', '15995', '8:0', '15995'), 
  ('16000', '2023-03-17 14:00:18.474000', '1', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-17 17:15:51.583000', '15998', '8:0', '15998'), 
  ('16004', '2023-03-17 14:00:44.244000', '1', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-17 14:00:44.244000', '1', '8:0', '16002'), 
  ('16284', '2023-03-20 14:13:11.107000', '1', 'MON,TUE,WED,THUR,FRI', '23:55', '2023-03-20 14:13:11.107000', '1', '8:0', '16282'), 
  ('16287', '2023-03-20 14:14:17.716000', '16282', 'MON,TUE,WED,THUR,FRI', '23:55', '2023-03-20 14:14:17.716000', '16282', '8:0', '16285'), 
  ('16296', '2023-03-20 14:14:59.634000', '16282', 'MON,TUE,WED,THUR,FRI', '23:55', '2023-03-20 16:49:10.811000', '16293', '8:0', '16293'), 
  ('16865', '2023-03-22 15:36:05.801000', '16002', 'MON,TUE,WED,THUR,FRI', '15:50', '2023-03-22 15:47:41.740000', '16862', '8:0', '16862');
UNLOCK TABLES;

