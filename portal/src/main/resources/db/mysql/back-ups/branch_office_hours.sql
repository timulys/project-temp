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


CREATE TABLE `branch_office_hours` (
  `id` bigint(20) NOT NULL,
  `created` datetime(6) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `day_of_week` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `end_counsel_time` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `modified` datetime(6) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `start_counsel_time` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `branch_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k2sk0nctf9qksewouvp2yul3b` (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES branch_office_hours WRITE;

INSERT INTO 
  branch_office_hours (id, created, creator, day_of_week, end_counsel_time, modified, modifier, start_counsel_time, branch_id) 
VALUES 
  ('1', '2023-03-16 13:19:02', '1', 'MON,TUE,WED,THUR,FRI,SAT,SUN', '23:55', '2023-04-06 09:32:35.889000', '2', '8:0', '1'), 
  ('15561', '2023-03-16 13:21:07.349000', '1', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-16 13:21:07.349000', '1', '8:0', '15556'), 
  ('15991', '2023-03-17 13:52:37.595000', '1', 'MON,TUE,WED,THUR,FRI', '23:0', '2023-03-22 15:46:59.481000', '16002', '8:0', '15986'), 
  ('16280', '2023-03-20 14:12:31.281000', '1', 'MON,TUE,WED,THUR,FRI', '23:55', '2023-03-20 14:12:31.281000', '1', '8:0', '16275');
UNLOCK TABLES;

