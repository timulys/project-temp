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


CREATE TABLE `upload` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `created` datetime(6) NOT NULL COMMENT '생성 일시',
  `creator` bigint(20) NOT NULL COMMENT '생성자',
  `mime_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'MIME 타입',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '이름',
  `original_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '원본 이름',
  `path` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '로컬 경로',
  `sizes` bigint(20) NOT NULL COMMENT '바이트 사이즈',
  `type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '업로드 타입',
  `url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES upload WRITE;

INSERT INTO 
  upload (id, created, creator, mime_type, name, original_name, path, sizes, type, url) 
VALUES 
  ('15643', '2023-03-16 13:48:49.750000', '3', 'image/png', '1995a3d0-4b6a-470c-8c19-f35fa2195fa2.png', '스크린샷 2023-03-14 오후 2.01.40.png', '/image/2023/03', '18201', 'guide', '/image/2023/03/1995a3d0-4b6a-470c-8c19-f35fa2195fa2.png'), 
  ('16125', '2023-03-17 17:14:32.642000', '4', 'image/png', 'b3caadaa-6d26-4a28-a4f0-8e6fa626e913.png', '1516502203.png', '/image/2023/03', '1485', 'memberFirstMessage', '/image/2023/03/b3caadaa-6d26-4a28-a4f0-8e6fa626e913.png'), 
  ('16126', '2023-03-17 17:15:37.662000', '15998', 'image/png', 'afadac6d-3063-4916-92bf-e77d871a94eb.png', '1516502203.png', '/image/2023/03', '1485', 'memberFirstMessage', '/image/2023/03/afadac6d-3063-4916-92bf-e77d871a94eb.png'), 
  ('16151', '2023-03-17 17:33:58.551000', '4', 'image/jpeg', '5671620a-8b4c-46b9-8ce5-9c5315b5b383.jpg', '20yt53u7y9611.jpg', '/image/2023/03', '744245', 'memberFirstMessage', '/image/2023/03/5671620a-8b4c-46b9-8ce5-9c5315b5b383.jpg'), 
  ('16166', '2023-03-17 17:45:21.927000', '4', 'image/jpeg', 'c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg', 'KakaoTalk_Image_2023-03-17-17-44-58.jpeg', '/image/2023/03', '134183', 'memberFirstMessage', '/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg'), 
  ('16639', '2023-03-21 10:41:31.466000', '2', 'text/html', '0d246dfc-f9f0-466d-b58b-570228558ccb.html', 'Google.html', '/file/2023/03', '152617', 'guide', '/file/2023/03/0d246dfc-f9f0-466d-b58b-570228558ccb.html'), 
  ('16709', '2023-03-21 18:05:22.161000', '5', 'image/png', 'fee91c70-2316-4daf-a381-e4e807c68b9f.png', 'Julie16.png', '/image/2023/03', '380519', 'memberFirstMessage', '/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png'), 
  ('17263', '2023-03-29 10:58:22.112000', '3', 'text/plain', 'be13521e-b8d7-4f50-b1fd-b2d37eb2d165.txt', '윈도우 frontend 설정.txt', '/file/2023/03', '595', 'notice', '/file/2023/03/be13521e-b8d7-4f50-b1fd-b2d37eb2d165.txt'), 
  ('17266', '2023-03-29 11:02:10.510000', '3', 'text/plain', 'ad1a0584-dc3b-410f-b6ae-fcb15618b743.txt', '윈도우 frontend 설정.txt', '/file/2023/03', '595', 'notice', '/file/2023/03/ad1a0584-dc3b-410f-b6ae-fcb15618b743.txt'), 
  ('17269', '2023-03-29 12:41:59.642000', '2', 'image/jpeg', 'dceb15d3-b018-40f8-8c55-fdb61185bfd5.jpg', 'KakaoTalk_20230308_111305561.jpg', '/image/2023/03', '429136', 'notice', '/image/2023/03/dceb15d3-b018-40f8-8c55-fdb61185bfd5.jpg'), 
  ('17312', '2023-03-29 13:39:00.885000', '2', 'image/jpeg', 'd0521655-8b55-457a-a6c0-cd4751a7bd49.jpg', 'KakaoTalk_20230308_111305561.jpg', '/image/2023/03', '429136', 'notice', '/image/2023/03/d0521655-8b55-457a-a6c0-cd4751a7bd49.jpg'), 
  ('17523', '2023-03-29 16:41:16.874000', '2', 'image/jpeg', 'bba828c3-dc42-4ba1-8fbe-f33484a32275.jpg', 'KakaoTalk_20230320_232653058_01.jpg', '/image/2023/03', '269722', 'notice', '/image/2023/03/bba828c3-dc42-4ba1-8fbe-f33484a32275.jpg'), 
  ('17579', '2023-03-30 10:08:24.901000', '5', 'image/png', 'c4531bd2-a222-4881-85bc-6616a3434ff7.png', '다운로드.png', '/image/2023/03', '8881', 'memberFirstMessage', '/image/2023/03/c4531bd2-a222-4881-85bc-6616a3434ff7.png'), 
  ('17580', '2023-03-30 10:08:45.694000', '5', 'image/png', '6bc55b6c-588a-4610-9284-df3cf464d242.png', '여기망했어요.png', '/image/2023/03', '889693', 'memberFirstMessage', '/image/2023/03/6bc55b6c-588a-4610-9284-df3cf464d242.png'), 
  ('17710', '2023-03-30 13:38:17.345000', '2', 'image/png', 'd509aad7-8bfb-4fe2-8449-711228c6e009.png', 'KakaoTalk_20230330_111156454.png', '/image/2023/03', '663', 'notice', '/image/2023/03/d509aad7-8bfb-4fe2-8449-711228c6e009.png'), 
  ('17712', '2023-03-30 13:38:17.357000', '2', 'image/png', 'e9bd0184-a485-4582-9447-2fdd1b4ec9d2.png', 'KakaoTalk_20230330_111154898.png', '/image/2023/03', '195', 'notice', '/image/2023/03/e9bd0184-a485-4582-9447-2fdd1b4ec9d2.png'), 
  ('17714', '2023-03-30 13:38:17.369000', '2', 'image/png', '661f95a6-6516-44be-8883-c93dfff250de.png', 'KakaoTalk_20230330_111156454.png', '/image/2023/03', '663', 'notice', '/image/2023/03/661f95a6-6516-44be-8883-c93dfff250de.png'), 
  ('17724', '2023-03-30 13:59:31.080000', '2', 'application/zip', '64304442-f6db-4493-b377-e100b20805e0.zip', 'apache-maven-3.9.0-bin.zip', '/file/2023/03', '9126298', 'notice', '/file/2023/03/64304442-f6db-4493-b377-e100b20805e0.zip'), 
  ('17726', '2023-03-30 13:59:31.094000', '2', 'application/octet-stream', 'f5d79437-4d11-4f23-ae24-ecbe6eaa1c02.doc', '윈도우 frontend 설정.doc', '/file/2023/03', '595', 'notice', '/file/2023/03/f5d79437-4d11-4f23-ae24-ecbe6eaa1c02.doc'), 
  ('18246', '2023-04-03 20:55:38.234000', '5', 'image/png', '00ac8fbc-2210-44e8-ac68-03c8d81d0ced.png', '스크린샷 2023-04-03 오후 8.54.19.png', '/image/2023/04', '126172', 'memberFirstMessage', '/image/2023/04/00ac8fbc-2210-44e8-ac68-03c8d81d0ced.png'), 
  ('18381', '2023-04-04 10:59:56.350000', '5', 'image/png', 'a63d9d18-db56-4639-93d5-dc936f7ac7cb.png', '스크린샷 2023-04-04 오전 10.59.39.png', '/image/2023/04', '21672', 'memberFirstMessage', '/image/2023/04/a63d9d18-db56-4639-93d5-dc936f7ac7cb.png'), 
  ('18422', '2023-04-04 11:58:09.699000', '4', 'image/jpeg', '2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg', 'test.jpg', '/image/2023/04', '2232', 'memberFirstMessage', '/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg'), 
  ('18744', '2023-04-05 15:35:27.627000', '2', 'image/png', '6bdefa4b-89b0-4a30-ab75-c484b993805e.png', '관리자용이미지.png', '/image/2023/04', '126172', 'memberFirstMessage', '/image/2023/04/6bdefa4b-89b0-4a30-ab75-c484b993805e.png'), 
  ('18745', '2023-04-05 15:35:57.939000', '2', 'image/png', 'a62ac6dc-252a-4c1b-a4f5-ff8efbbe8c95.png', '관리자용이미지.png', '/image/2023/04', '126172', 'memberFirstMessage', '/image/2023/04/a62ac6dc-252a-4c1b-a4f5-ff8efbbe8c95.png'), 
  ('18764', '2023-04-05 16:02:38.085000', '5', 'image/png', '3facf187-ee48-4180-a0d1-bab0ff6c7e59.png', '상담직원용이미지.png', '/image/2023/04', '572725', 'memberFirstMessage', '/image/2023/04/3facf187-ee48-4180-a0d1-bab0ff6c7e59.png'), 
  ('18855', '2023-04-05 20:54:39.628000', '2', 'image/png', '20aab4b7-3b46-4fcf-98b3-7470dfab2885.png', '관리자용이미지.png', '/image/2023/04', '126172', 'memberFirstMessage', '/image/2023/04/20aab4b7-3b46-4fcf-98b3-7470dfab2885.png'), 
  ('18863', '2023-04-05 20:55:35.533000', '5', 'image/png', '41c86cde-640b-4408-92b4-53d320463c4b.png', '상담직원용이미지.png', '/image/2023/04', '572725', 'memberFirstMessage', '/image/2023/04/41c86cde-640b-4408-92b4-53d320463c4b.png'), 
  ('18864', '2023-04-05 20:57:39.083000', '2', 'image/png', '39f7f7a3-4f84-425f-a027-a38ebd6c9114.png', '관리자용이미지.png', '/image/2023/04', '126172', 'memberFirstMessage', '/image/2023/04/39f7f7a3-4f84-425f-a027-a38ebd6c9114.png'), 
  ('18872', '2023-04-05 20:58:56.493000', '5', 'image/png', '774b65d3-b5f3-4dea-857b-8c3253fc9c64.png', '상담직원용이미지.png', '/image/2023/04', '572725', 'memberFirstMessage', '/image/2023/04/774b65d3-b5f3-4dea-857b-8c3253fc9c64.png'), 
  ('19015', '2023-04-06 14:25:12.290000', '4', 'image/png', '9c40a815-89b9-4024-8b2b-e14c60641f6d.png', 'Bootstrap_logo.svg.png', '/image/2023/04', '9743', 'memberFirstMessage', '/image/2023/04/9c40a815-89b9-4024-8b2b-e14c60641f6d.png'), 
  ('19057', '2023-04-06 17:47:17.486000', '3', 'text/plain', '785e8c65-3c4d-4d9a-8775-c747cd87540d.txt', '0e23218f-0dcd-47f8-a13a-40ec9d1ec81e.txt', '/file/2023/04', '15', 'guide', '/file/2023/04/785e8c65-3c4d-4d9a-8775-c747cd87540d.txt'), 
  ('19099', '2023-04-06 19:00:43.590000', '5', 'image/jpeg', 'd52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg', 'channels4_profile.jpg', '/image/2023/04', '64986', 'memberFirstMessage', '/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg'), 
  ('19149', '2023-04-06 19:47:24.836000', '2', 'application/octet-stream', 'c5db6971-0ce4-4e3f-ae28-e3034fd38c8c.json', '공지사항추가.json', '/file/2023/04', '240136', 'guide', '/file/2023/04/c5db6971-0ce4-4e3f-ae28-e3034fd38c8c.json'), 
  ('19152', '2023-04-06 19:48:22.416000', '2', 'application/octet-stream', '5c32bef4-372a-42c5-97ca-e45a055316c4.json', 'portal_20230329(금지어 공지사항).json', '/file/2023/04', '231938', 'guide', '/file/2023/04/5c32bef4-372a-42c5-97ca-e45a055316c4.json'), 
  ('19168', '2023-04-06 21:35:17.338000', '4', 'image/jpeg', '98cc4406-7865-4559-9ef1-651b225ddfcb.jpeg', 'KakaoTalk_Photo_2023-04-06-21-34-25 001.jpeg', '/image/2023/04', '141560', 'memberFirstMessage', '/image/2023/04/98cc4406-7865-4559-9ef1-651b225ddfcb.jpeg'), 
  ('19182', '2023-04-06 21:36:41.194000', '4', 'image/png', '7fb9eefa-2219-47bf-8f84-786d681b1a8a.png', 'KakaoTalk_Photo_2023-04-06-21-34-25 002.png', '/image/2023/04', '193282', 'memberFirstMessage', '/image/2023/04/7fb9eefa-2219-47bf-8f84-786d681b1a8a.png'), 
  ('19196', '2023-04-06 22:01:51.074000', '4', 'image/jpeg', '4e597361-d522-480e-bf40-7581b8f6fc37.jpg', 'a.jpg', '/image/2023/04', '64986', 'memberFirstMessage', '/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg'), 
  ('19251', '2023-04-07 15:18:19.759000', '3', 'image/png', '3ff1c7c3-3415-4bfd-ad84-aa74e85424d5.png', 'KakaoTalk_20230330_111156454.png', '/image/2023/04', '663', 'notice', '/image/2023/04/3ff1c7c3-3415-4bfd-ad84-aa74e85424d5.png'), 
  ('19253', '2023-04-07 15:18:19.786000', '3', 'image/jpeg', 'c4fb3a22-adc7-4017-bafb-ac92d4a59264.jpg', 'KakaoTalk_20230320_232653058_01.jpg', '/image/2023/04', '269722', 'notice', '/image/2023/04/c4fb3a22-adc7-4017-bafb-ac92d4a59264.jpg'), 
  ('19469', '2023-04-10 11:19:24.953000', '4', 'image/png', 'a90ba621-0235-4952-a1ce-42e5a4a713c4.png', 'KakaoTalk_Photo_2023-04-06-21-34-25 002.png', '/image/2023/04', '193282', 'memberFirstMessage', '/image/2023/04/a90ba621-0235-4952-a1ce-42e5a4a713c4.png'), 
  ('19910', '2023-04-11 10:47:01.995000', '4', 'image/png', 'b70e3dcb-1cff-467b-aa4b-f68b7f842d23.png', '김상담명함.png', '/image/2023/04', '193282', 'memberFirstMessage', '/image/2023/04/b70e3dcb-1cff-467b-aa4b-f68b7f842d23.png'), 
  ('19911', '2023-04-11 10:48:07.589000', '4', 'image/png', '037a8045-2322-47a0-a76e-efa4668f898f.png', '김상담명함.png', '/image/2023/04', '193282', 'memberFirstMessage', '/image/2023/04/037a8045-2322-47a0-a76e-efa4668f898f.png'), 
  ('20114', '2023-04-12 15:07:32.460000', '4', 'image/jpeg', '476eabf6-c3a7-4ca6-8672-fa2ec7ddcd1f.jpeg', '카카오엔터프라이즈.jpeg', '/image/2023/04', '141560', 'memberFirstMessage', '/image/2023/04/476eabf6-c3a7-4ca6-8672-fa2ec7ddcd1f.jpeg'), 
  ('20127', '2023-04-12 15:43:26.547000', '4', 'image/png', '4e038f36-c8e7-4549-8961-9645a2fe749c.png', '김상담명함.png', '/image/2023/04', '193282', 'memberFirstMessage', '/image/2023/04/4e038f36-c8e7-4549-8961-9645a2fe749c.png'), 
  ('20137', '2023-04-12 15:46:15.994000', '4', 'image/jpeg', '6b8be7fa-ee04-429a-949e-50c973d58b14.jpeg', '카카오엔터프라이즈.jpeg', '/image/2023/04', '141560', 'memberFirstMessage', '/image/2023/04/6b8be7fa-ee04-429a-949e-50c973d58b14.jpeg'), 
  ('20138', '2023-04-12 15:46:56.764000', '4', 'image/png', '65dafc2a-ab20-4313-a547-6019a715e616.png', '김상담명함.png', '/image/2023/04', '193282', 'memberFirstMessage', '/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png');
UNLOCK TABLES;

