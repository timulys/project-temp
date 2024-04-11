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


CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이름',
  `profile` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '프로필',
  `address` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '주소',
  `birthday` date DEFAULT NULL COMMENT '생년월일',
  `phone_number` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '연락처',
  PRIMARY KEY (`id`),
  KEY `IDX_CUSTOMER__SEARCH` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES customer WRITE;

INSERT INTO 
  customer (id, name, profile, address, birthday, phone_number) 
VALUES 
  ('1', 'DY', NULL, NULL, NULL, NULL), 
  ('2', '신기준', 'https://img.hankyung.com/photo/201903/AA.19067065.1.jpg', '서울시 양천구 목동중앙남로 16마길 35-6', '1989-03-06', '010-5506-7909'), 
  ('3', '황주영', 'https://img.hankyung.com/photo/201903/AA.19067065.1.jpg', '경기 시흥시 은행로 122 201호', '1993-07-26', '010-4512-8391'), 
  ('4', '유채화', '', '경기 의왕시 부곡시장1길 38 201호', '1990-04-17', '010-4865-1241'), 
  ('5', '정다운', '', '경남 통영시 도남동 봉평로 25', '1990-03-27', '010-9951-3201'), 
  ('6', '김해인', '', '서울 강서구 공항대로59길 64 505호 ', '1994-09-13', '010-9330-2210'), 
  ('7', '이강희', '', '서울 강남구 남부순환로363길 9-6 903호', '1975-12-23', '010-2341-9950'), 
  ('8', '정민수', '', '경기 수원시 팔달구 화양로50번길 30 101호', '1985-04-22', '010-8843-6519'), 
  ('9', '황성환', '', '경기 수원시 팔달구 화양로50번길 30 201호', '1950-04-22', '010-8800-6321'), 
  ('10', '홍수영', '', '경기 신왕시 오팔구 교육로20번길 30', '1965-10-22', '010-8800-1231'), 
  ('11', '이혜진', '', '경북 영천시 완산로 69', '1975-02-13', '010-1313-1222'), 
  ('12', '홍길동', '', '경북 영천시 완산로 69', '1992-04-18', '010-1313-1222'), 
  ('13', '함빛나리', '', '경북 영천시 완산로 69', '1992-02-18', '010-1313-1222'), 
  ('14', '강준', '', '경북 영천시 완산로 69', '1992-12-03', '010-1313-1222'), 
  ('15', '김진', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('16', '모다빈', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('17', '이기찬', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('18', '공유', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('19', '현성우', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('20', '남윤지', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('21', '하현우', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('22', '장훈', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('23', '서율', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222'), 
  ('24', '독고영재', '', '경북 영천시 완산로 69', '1990-03-03', '010-1313-1222');
UNLOCK TABLES;

