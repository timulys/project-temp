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


CREATE TABLE `menu_privilege` (
                                  `id` bigint(20) NOT NULL COMMENT 'PK',
                                  `menu_id` bigint(20) NOT NULL COMMENT '메뉴 PK',
                                  `privilege_id` bigint(20) NOT NULL COMMENT '권한 PK',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `UK_MENU_PRIVILEGE` (`menu_id`,`privilege_id`),
                                  KEY `FK_MENU_PRIVILEGE__PRIVILEGE` (`privilege_id`),
                                  CONSTRAINT `FK_MENU_PRIVILEGE__PRIVILEGE` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES menu_privilege WRITE;

INSERT INTO
    menu_privilege (id, menu_id, privilege_id)
VALUES
    ('101', '100', '100'),
    ('102', '110', '110'),
    ('103', '120', '120'),
    ('104', '120', '121'),
    ('105', '120', '122'),
    ('106', '120', '123'),
    ('107', '120', '124'),
    ('108', '130', '130'),
    ('109', '131', '131'),
    ('110', '132', '132'),
    ('111', '133', '133'),
    ('200', '200', '200'),
    ('211', '211', '211'),
    ('221', '221', '221'),
    ('222', '222', '222'),
    ('231', '231', '231'),
    ('232', '232', '232'),
    ('233', '233', '233'),
    ('234', '234', '234'),
    ('235', '235', '235'),
    ('241', '241', '241'),
    ('242', '242', '242'),
    ('243', '242', '243'),
    ('245', '245', '245'),
    ('246', '246', '246'),
    ('247', '247', '247'),
    ('251', '251', '251'),
    ('261', '261', '261'),
    ('300', '300', '300'),
    ('311', '311', '311'),
    ('312', '312', '312'),
    ('313', '312', '313'),
    ('314', '314', '314'),
    ('321', '321', '321'),
    ('322', '322', '322'),
    ('323', '323', '323'),
    ('325', '325', '325'),
    ('391', '391', '391');
UNLOCK TABLES;

