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


CREATE TABLE `issue_log` (
  `id` bigint(20) NOT NULL COMMENT 'PK',
  `assigner` bigint(20) DEFAULT NULL COMMENT '담당 유저, 유저 PK',
  `created` datetime(6) NOT NULL COMMENT '생성 일시',
  `creator` bigint(20) NOT NULL COMMENT '생성자',
  `issue_id` bigint(20) NOT NULL COMMENT '이슈 PK',
  `payload` varchar(4000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '로그 페이로드',
  `relative_id` bigint(20) DEFAULT NULL COMMENT '연관된 로그 PK',
  `status` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '상태 (send: 발신, fail: 실패, receive: 수신, read: 읽음)',
  PRIMARY KEY (`id`),
  KEY `IDX_ISSUE_LOG__SEARCH` (`issue_id`,`id`,`created`),
  KEY `IDX_ISSUE_LOG__ASSIGNER` (`issue_id`,`assigner`,`id`,`created`),
  KEY `IDX_ISSUE_LOG__ISSUE` (`issue_id`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

LOCK TABLES issue_log WRITE;

INSERT INTO 
  issue_log (id, assigner, created, creator, issue_id, payload, relative_id, status) 
VALUES 
  ('15571', '9000000001', '2023-03-16 13:23:26.480000', '9000000001', '15568', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15589', '9000000001', '2023-03-16 13:25:56.134000', '9000000001', '15586', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15590', '4', '2023-03-16 13:26:26.614000', '15567', '15568', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "du"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:26:26.515Z"
  }
}', NULL, 'receive'), 
  ('15591', '9000000001', '2023-03-16 13:26:26.653000', '9000000001', '15568', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15592', '9000000001', '2023-03-16 13:26:52.954000', '9000000001', '15568', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15593', '4', '2023-03-16 13:26:52.960000', '4', '15568', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "접수"
    } ]
  } ]
}', NULL, 'send'), 
  ('15594', '9000000001', '2023-03-16 13:27:24.211000', '9000000001', '15586', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15595', '5', '2023-03-16 13:27:24.216000', '5', '15586', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ]
}', NULL, 'send'), 
  ('15599', '9000000001', '2023-03-16 13:28:36.868000', '9000000001', '15568', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('15604', '9000000001', '2023-03-16 13:29:24.822000', '9000000001', '15586', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15610', '9000000001', '2023-03-16 13:32:46.641000', '15606', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:32:44.37Z"
  }
}', NULL, 'receive'), 
  ('15611', '9000000001', '2023-03-16 13:32:46.672000', '9000000001', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15613', '9000000001', '2023-03-16 13:32:47.717000', '9000000001', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15614', '4', '2023-03-16 13:32:48.864000', '15606', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:32:48.8Z"
  }
}', NULL, 'receive'), 
  ('15615', '9000000001', '2023-03-16 13:32:48.895000', '9000000001', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15616', '4', '2023-03-16 13:32:55.406000', '15606', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "헤이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:32:55.35Z"
  }
}', NULL, 'receive'), 
  ('15617', '9000000001', '2023-03-16 13:32:55.436000', '9000000001', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15618', '9000000001', '2023-03-16 13:33:25.524000', '9000000001', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15619', '4', '2023-03-16 13:33:25.528000', '4', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('15620', '9000000001', '2023-03-16 13:33:42.593000', '9000000001', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('15623', '4', '2023-03-16 13:33:47.454000', '15606', '15608', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:33:47.37Z"
  }
}', NULL, 'receive'), 
  ('15626', '9000000001', '2023-03-16 13:34:04.675000', '15606', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종료 안아닌데!"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:34:04.621Z"
  }
}', NULL, 'receive'), 
  ('15627', '9000000001', '2023-03-16 13:34:04.704000', '9000000001', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15628', '9000000001', '2023-03-16 13:34:11.150000', '15606', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:34:11.065Z"
  }
}', NULL, 'receive'), 
  ('15629', '9000000001', '2023-03-16 13:34:11.182000', '9000000001', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15630', '9000000001', '2023-03-16 13:34:22.384000', '15606', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "싱시간"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:34:22.337Z"
  }
}', NULL, 'receive'), 
  ('15631', '9000000001', '2023-03-16 13:34:22.414000', '9000000001', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15633', '9000000001', '2023-03-16 13:34:25.341000', '9000000001', '15586', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15635', '9000000001', '2023-03-16 13:34:28.346000', '9000000001', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15636', '4', '2023-03-16 13:34:40.922000', '15606', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : " 실시간 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:34:40.876Z"
  }
}', NULL, 'receive'), 
  ('15637', '9000000001', '2023-03-16 13:34:40.959000', '9000000001', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15638', '9000000001', '2023-03-16 13:34:48.882000', '9000000001', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15639', '4', '2023-03-16 13:34:48.887000', '4', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('15640', '9000000001', '2023-03-16 13:36:55.608000', '9000000001', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15642', '4', '2023-03-16 13:38:05.013000', '15606', '15624', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T04:38:04.941Z"
  }
}', NULL, 'receive'), 
  ('15648', '9000000001', '2023-03-16 14:23:47.916000', '9000000001', '15645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15650', '5', '2023-03-16 14:25:05.545000', '15567', '15645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "DY 테스트 중"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T05:25:05.452Z"
  }
}', NULL, 'receive'), 
  ('15651', '9000000001', '2023-03-16 14:25:05.562000', '9000000001', '15645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15652', '9000000001', '2023-03-16 14:25:23.507000', '9000000001', '15645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15653', '5', '2023-03-16 14:25:23.511000', '5', '15645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "접수"
    } ]
  } ]
}', NULL, 'send'), 
  ('15654', '9000000001', '2023-03-16 14:27:27.132000', '9000000001', '15645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15657', '9000000001', '2023-03-16 14:32:27.408000', '9000000001', '15645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15663', '9000000001', '2023-03-16 18:17:00.181000', '9000000001', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15664', '4', '2023-03-16 18:17:07.355000', '15659', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T09:17:07.281Z"
  }
}', NULL, 'receive'), 
  ('15665', '9000000001', '2023-03-16 18:17:07.385000', '9000000001', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15666', '4', '2023-03-16 18:17:18.426000', '15659', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T09:17:18.38Z"
  }
}', NULL, 'receive'), 
  ('15667', '9000000001', '2023-03-16 18:17:18.452000', '9000000001', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15668', '9000000001', '2023-03-16 18:17:30.007000', '9000000001', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15669', '4', '2023-03-16 18:17:30.012000', '4', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('15670', '4', '2023-03-16 18:17:32.350000', '4', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('15671', '9000000001', '2023-03-16 18:19:51.034000', '9000000001', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15673', '4', '2023-03-16 18:20:06.929000', '15659', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄱㄷㄱㄷ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-16T09:20:06.835Z"
  }
}', NULL, 'receive'), 
  ('15675', '4', '2023-03-16 18:26:20.700000', '4', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ee"
    } ]
  } ]
}', NULL, 'send'), 
  ('15677', '9000000001', '2023-03-16 18:26:21.243000', '9000000001', '15660', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15681', '9000000001', '2023-03-16 18:28:38.712000', '9000000001', '15678', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15682', '9000000001', '2023-03-16 18:28:52.651000', '9000000001', '15678', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15683', '4', '2023-03-16 18:28:52.656000', '4', '15678', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('15687', '9000000001', '2023-03-16 18:29:47.390000', '9000000001', '15678', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15692', '9000000001', '2023-03-17 08:24:05.776000', '9000000001', '15689', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15693', '9000000001', '2023-03-17 08:26:18.040000', '9000000001', '15689', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15694', '4', '2023-03-17 08:26:18.044000', '4', '15689', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('15695', '9000000001', '2023-03-17 08:28:42.801000', '9000000001', '15689', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15698', '9000000001', '2023-03-17 08:33:43.060000', '9000000001', '15689', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15704', '9000000001', '2023-03-17 09:32:19.510000', '9000000001', '15701', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15705', '5', '2023-03-17 09:32:36.607000', '15606', '15701', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T00:32:36.573Z"
  }
}', NULL, 'receive'), 
  ('15706', '9000000001', '2023-03-17 09:32:36.660000', '9000000001', '15701', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15708', '9000000001', '2023-03-17 09:39:44.672000', '9000000001', '15701', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('15710', '5', '2023-03-17 09:46:57.799000', '15606', '15701', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T00:46:57.686Z"
  }
}', NULL, 'receive'), 
  ('15711', '9000000001', '2023-03-17 09:46:57.824000', '9000000001', '15701', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15712', '5', '2023-03-17 09:48:21.637000', '15606', '15701', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T00:48:21.547Z"
  }
}', NULL, 'receive'), 
  ('15713', '9000000001', '2023-03-17 09:48:21.655000', '9000000001', '15701', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15717', '9000000001', '2023-03-17 09:50:44.189000', '15606', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T00:50:44.132Z"
  }
}', NULL, 'receive'), 
  ('15718', '9000000001', '2023-03-17 09:50:44.211000', '9000000001', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15720', '9000000001', '2023-03-17 09:50:45.256000', '9000000001', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15726', '9000000001', '2023-03-17 09:57:45.501000', '9000000001', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('15728', '9000000001', '2023-03-17 10:06:31.130000', '9000000001', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15729', '5', '2023-03-17 10:06:31.137000', '5', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5__bid_1__mid_5"
    } ]
  } ]
}', NULL, 'send'), 
  ('15730', '5', '2023-03-17 10:08:32.641000', '5', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㄴㅁㅇㄴㅇㅁㄴ언미ㅏ엄ㄴㅇ마ㅣㄴ"
    } ]
  } ]
}', NULL, 'send'), 
  ('15736', '9000000001', '2023-03-17 10:10:45.489000', '9000000001', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15742', '5', '2023-03-17 10:12:28.287000', '5', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('15743', '5', '2023-03-17 10:12:48.707000', '5', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    }, {
      "type" : "action"
    } ]
  } ]
}', NULL, 'send'), 
  ('15744', '5', '2023-03-17 10:13:05.230000', '5', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1번"
    } ]
  } ]
}', NULL, 'send'), 
  ('15746', '5', '2023-03-17 10:13:10.460000', '5', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "2번"
    } ]
  } ]
}', NULL, 'send'), 
  ('15748', '5', '2023-03-17 10:13:12.548000', '5', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "3번"
    } ]
  } ]
}', NULL, 'send'), 
  ('15755', '9000000001', '2023-03-17 10:15:20.068000', '9000000001', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15756', '9000000001', '2023-03-17 10:15:42.701000', '9000000001', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 매니저1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15757', '3', '2023-03-17 10:15:42.705000', '3', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요, 전달 받았습니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('15759', '9000000001', '2023-03-17 10:15:45.737000', '9000000001', '15715', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15764', '9000000001', '2023-03-17 10:17:03.512000', '9000000001', '15761', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15765', '9000000001', '2023-03-17 10:24:16.487000', '9000000001', '15761', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('15767', '9000000001', '2023-03-17 10:25:33.029000', '9000000001', '15761', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15768', '5', '2023-03-17 10:25:33.034000', '5', '15761', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('15769', '9000000001', '2023-03-17 10:25:36.755000', '9000000001', '15761', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('15776', '9000000001', '2023-03-17 10:35:22.304000', '9000000001', '15774', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('15780', '9000000001', '2023-03-17 10:35:34.631000', '9000000001', '15777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15781', '5', '2023-03-17 10:35:35.123000', '15606', '15777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "연결해줘ㅏ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:35:35.082Z"
  }
}', NULL, 'receive'), 
  ('15782', '9000000001', '2023-03-17 10:35:35.139000', '9000000001', '15777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15783', '9000000001', '2023-03-17 10:36:11.620000', '9000000001', '15777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15786', '9000000001', '2023-03-17 10:36:41.970000', '15606', '15784', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "놉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:36:41.914Z"
  }
}', NULL, 'receive'), 
  ('15787', '9000000001', '2023-03-17 10:36:41.986000', '9000000001', '15784', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15789', '9000000001', '2023-03-17 10:36:43.003000', '9000000001', '15784', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15791', '9000000001', '2023-03-17 10:39:20.199000', '9000000001', '15784', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15792', '4', '2023-03-17 10:39:20.204000', '4', '15784', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이상하네요."
    } ]
  } ]
}', NULL, 'send'), 
  ('15793', '4', '2023-03-17 10:39:47.566000', '15606', '15784', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:39:47.504Z"
  }
}', NULL, 'receive'), 
  ('15796', '9000000001', '2023-03-17 10:40:23.201000', '15606', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "연결좀"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:40:23.14Z"
  }
}', NULL, 'receive'), 
  ('15797', '9000000001', '2023-03-17 10:40:23.216000', '9000000001', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15798', '9000000001', '2023-03-17 10:40:25.750000', '15606', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "연결해주세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:40:25.707Z"
  }
}', NULL, 'receive'), 
  ('15799', '9000000001', '2023-03-17 10:40:25.766000', '9000000001', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15800', '9000000001', '2023-03-17 10:40:50.272000', '15606', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=mid_2"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:40:50.233Z"
  }
}', NULL, 'receive'), 
  ('15801', '9000000001', '2023-03-17 10:40:50.286000', '9000000001', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15803', '9000000001', '2023-03-17 10:40:52.346000', '9000000001', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15804', '5', '2023-03-17 10:41:00.071000', '15606', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:41:00.036Z"
  }
}', NULL, 'receive'), 
  ('15805', '9000000001', '2023-03-17 10:41:00.088000', '9000000001', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15806', '5', '2023-03-17 10:45:53.221000', '15606', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "내"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:45:53.16Z"
  }
}', NULL, 'receive'), 
  ('15807', '9000000001', '2023-03-17 10:45:53.264000', '9000000001', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15808', '5', '2023-03-17 10:45:55.562000', '15606', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:45:55.5Z"
  }
}', NULL, 'receive'), 
  ('15809', '9000000001', '2023-03-17 10:45:55.581000', '9000000001', '15794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15812', '9000000001', '2023-03-17 10:46:02.481000', '15606', '15810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:46:02.454Z"
  }
}', NULL, 'receive'), 
  ('15813', '9000000001', '2023-03-17 10:46:02.496000', '9000000001', '15810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15815', '9000000001', '2023-03-17 10:46:33.499000', '9000000001', '15810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15819', '9000000001', '2023-03-17 10:47:50.813000', '9000000001', '15816', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15820', '5', '2023-03-17 10:47:50.950000', '15606', '15816', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T01:47:50.902Z"
  }
}', NULL, 'receive'), 
  ('15821', '9000000001', '2023-03-17 10:47:50.965000', '9000000001', '15816', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15822', '9000000001', '2023-03-17 10:48:13.873000', '9000000001', '15816', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15823', '5', '2023-03-17 10:48:13.877000', '5', '15816', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('15824', '9000000001', '2023-03-17 10:50:16.622000', '9000000001', '15816', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15827', '9000000001', '2023-03-17 10:55:16.872000', '9000000001', '15816', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15831', '9000000001', '2023-03-17 11:00:21.329000', '9000000001', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15832', '4', '2023-03-17 11:00:21.597000', '15606', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안됑"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:00:21.562Z"
  }
}', NULL, 'receive'), 
  ('15833', '9000000001', '2023-03-17 11:00:21.613000', '9000000001', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15834', '9000000001', '2023-03-17 11:02:47.710000', '9000000001', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('15839', '9000000001', '2023-03-17 11:08:47.076000', '9000000001', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15840', '9000000001', '2023-03-17 11:09:24.034000', '9000000001', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15841', '5', '2023-03-17 11:09:24.038000', '5', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('15842', '9000000001', '2023-03-17 11:11:47.300000', '9000000001', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15844', '5', '2023-03-17 11:13:49.236000', '5', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "전환"
    } ]
  } ]
}', NULL, 'send'), 
  ('15845', '5', '2023-03-17 11:13:55.213000', '5', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이에서 일로"
    } ]
  } ]
}', NULL, 'send'), 
  ('15853', '9000000001', '2023-03-17 11:14:28.182000', '9000000001', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15854', '4', '2023-03-17 11:14:49.316000', '15659', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:14:49.246Z"
  }
}', NULL, 'receive'), 
  ('15855', '9000000001', '2023-03-17 11:14:49.333000', '9000000001', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15856', '4', '2023-03-17 11:16:21.621000', '15606', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오ㅑ요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:16:21.536Z"
  }
}', NULL, 'receive'), 
  ('15857', '9000000001', '2023-03-17 11:16:21.638000', '9000000001', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15858', '9000000001', '2023-03-17 11:17:05.299000', '9000000001', '15836', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15863', '9000000001', '2023-03-17 11:18:19.552000', '9000000001', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15864', '5', '2023-03-17 11:18:21.621000', '15859', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:18:21.558Z"
  }
}', NULL, 'receive'), 
  ('15865', '9000000001', '2023-03-17 11:18:21.644000', '9000000001', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15866', '5', '2023-03-17 11:19:01.661000', '15859', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "연결된건가용?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:19:01.606Z"
  }
}', NULL, 'receive'), 
  ('15867', '9000000001', '2023-03-17 11:19:01.678000', '9000000001', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15868', '5', '2023-03-17 11:19:05.594000', '15859', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오호"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:19:05.533Z"
  }
}', NULL, 'receive'), 
  ('15869', '9000000001', '2023-03-17 11:19:05.610000', '9000000001', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15870', '5', '2023-03-17 11:19:07.847000', '15859', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아하"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:19:07.814Z"
  }
}', NULL, 'receive'), 
  ('15871', '9000000001', '2023-03-17 11:19:07.863000', '9000000001', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15873', '9000000001', '2023-03-17 11:20:13.183000', '9000000001', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15874', '4', '2023-03-17 11:20:13.188000', '4', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('15878', '5', '2023-03-17 11:20:40.582000', '15859', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "연결안댔나유?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:20:40.518Z"
  }
}', NULL, 'receive'), 
  ('15879', '9000000001', '2023-03-17 11:20:40.602000', '9000000001', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15881', '4', '2023-03-17 11:21:20.701000', '4', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dkssudgktpdyd"
    } ]
  } ]
}', NULL, 'send'), 
  ('15882', '9000000001', '2023-03-17 11:21:51.782000', '9000000001', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15883', '5', '2023-03-17 11:21:51.786000', '5', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세용"
    } ]
  } ]
}', NULL, 'send'), 
  ('15888', '9000000001', '2023-03-17 11:23:39.142000', '9000000001', '15828', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15889', '5', '2023-03-17 11:24:14.175000', '5', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('15890', '9000000001', '2023-03-17 11:25:05.825000', '9000000001', '15860', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('15895', '9000000001', '2023-03-17 11:25:54.668000', '15606', '15893', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T02:25:54.635Z"
  }
}', NULL, 'receive'), 
  ('15896', '9000000001', '2023-03-17 11:25:54.689000', '9000000001', '15893', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15898', '9000000001', '2023-03-17 11:26:21.540000', '9000000001', '15893', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15899', '9000000001', '2023-03-17 11:33:49.048000', '9000000001', '15893', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('15901', '9000000001', '2023-03-17 13:13:28.929000', '9000000001', '15893', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15902', '5', '2023-03-17 13:13:28.934000', '5', '15893', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('15905', '9000000001', '2023-03-17 13:15:43.213000', '15606', '15903', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:15:43.159Z"
  }
}', NULL, 'receive'), 
  ('15906', '9000000001', '2023-03-17 13:15:43.231000', '9000000001', '15903', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15908', '9000000001', '2023-03-17 13:15:44.084000', '9000000001', '15903', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15909', '9000000001', '2023-03-17 13:15:52.589000', '9000000001', '15903', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15910', '5', '2023-03-17 13:15:52.594000', '5', '15903', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('15911', '9000000001', '2023-03-17 13:18:20.734000', '9000000001', '15903', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15919', '9000000001', '2023-03-17 13:21:31.091000', '9000000001', '15903', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('15924', '9000000001', '2023-03-17 13:22:21.452000', '15606', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오오"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:22:21.389Z"
  }
}', NULL, 'receive'), 
  ('15925', '9000000001', '2023-03-17 13:22:21.470000', '9000000001', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15927', '9000000001', '2023-03-17 13:22:21.787000', '9000000001', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15928', '4', '2023-03-17 13:22:42.469000', '15606', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:22:42.428Z"
  }
}', NULL, 'receive'), 
  ('15929', '9000000001', '2023-03-17 13:22:42.490000', '9000000001', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15930', '9000000001', '2023-03-17 13:22:52.850000', '9000000001', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('15931', '4', '2023-03-17 13:22:52.855000', '4', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('15932', '9000000001', '2023-03-17 13:23:05.457000', '9000000001', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('15935', '9000000001', '2023-03-17 13:25:21.042000', '9000000001', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('15940', '9000000001', '2023-03-17 13:30:21.279000', '9000000001', '15922', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('15943', '9000000001', '2023-03-17 13:32:01.304000', '15606', '15941', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "캐네ㅔ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:32:01.234Z"
  }
}', NULL, 'receive'), 
  ('15944', '9000000001', '2023-03-17 13:32:01.321000', '9000000001', '15941', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15945', '9000000001', '2023-03-17 13:32:12.301000', '15606', '15941', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "내네네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:32:12.248Z"
  }
}', NULL, 'receive'), 
  ('15946', '9000000001', '2023-03-17 13:32:12.320000', '9000000001', '15941', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15951', '9000000001', '2023-03-17 13:32:50.280000', '9000000001', '15948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15953', '3', '2023-03-17 13:35:51.838000', '15606', '15948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "왓"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:35:51.78Z"
  }
}', NULL, 'receive'), 
  ('15954', '9000000001', '2023-03-17 13:35:51.854000', '9000000001', '15948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15960', '9000000001', '2023-03-17 13:37:10.760000', '15606', '15958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "얀걀"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:37:10.695Z"
  }
}', NULL, 'receive'), 
  ('15961', '9000000001', '2023-03-17 13:37:10.777000', '9000000001', '15958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15962', '9000000001', '2023-03-17 13:37:47.051000', '15606', '15958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:37:47.011Z"
  }
}', NULL, 'receive'), 
  ('15963', '9000000001', '2023-03-17 13:37:47.069000', '9000000001', '15958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15964', '9000000001', '2023-03-17 13:38:49.883000', '15606', '15958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "??"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:38:49.834Z"
  }
}', NULL, 'receive'), 
  ('15965', '9000000001', '2023-03-17 13:38:49.904000', '9000000001', '15958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15970', '9000000001', '2023-03-17 13:43:07.594000', '9000000001', '15967', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15971', '4', '2023-03-17 13:43:09.863000', '15606', '15967', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넴"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:43:09.795Z"
  }
}', NULL, 'receive'), 
  ('15972', '9000000001', '2023-03-17 13:43:09.884000', '9000000001', '15967', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15977', '9000000001', '2023-03-17 13:43:38.677000', '9000000001', '15974', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('15978', '5', '2023-03-17 13:43:40.407000', '15606', '15974', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:43:40.36Z"
  }
}', NULL, 'receive'), 
  ('15979', '9000000001', '2023-03-17 13:43:40.423000', '9000000001', '15974', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('15983', '9000000001', '2023-03-17 13:45:27.170000', '15606', '15981', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T04:45:27.131Z"
  }
}', NULL, 'receive'), 
  ('15984', '9000000001', '2023-03-17 13:45:27.188000', '9000000001', '15981', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16010', '9000000001', '2023-03-17 14:02:33.618000', '9000000001', '16007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16011', '9000000001', '2023-03-17 14:02:46.044000', '9000000001', '16007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 kepmember1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16012', '15995', '2023-03-17 14:02:46.049000', '15995', '16007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16020', '9000000001', '2023-03-17 14:04:52.199000', '9000000001', '16007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16022', '15995', '2023-03-17 14:05:10.633000', '15995', '16007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dddd"
    } ]
  } ]
}', NULL, 'send'), 
  ('16026', '9000000001', '2023-03-17 14:09:52.433000', '9000000001', '16007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16032', '9000000001', '2023-03-17 14:22:59.693000', '15606', '16030', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T05:22:59.66Z"
  }
}', NULL, 'receive'), 
  ('16033', '9000000001', '2023-03-17 14:22:59.711000', '9000000001', '16030', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16034', '9000000001', '2023-03-17 14:23:25.958000', '15606', '16030', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하니"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T05:23:25.9Z"
  }
}', NULL, 'receive'), 
  ('16035', '9000000001', '2023-03-17 14:23:25.974000', '9000000001', '16030', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16038', '9000000001', '2023-03-17 14:24:10.693000', '15606', '16036', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하하"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T05:24:10.656Z"
  }
}', NULL, 'receive'), 
  ('16039', '9000000001', '2023-03-17 14:24:10.711000', '9000000001', '16036', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16053', '9000000001', '2023-03-17 14:36:49.600000', '9000000001', '16050', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16064', '9000000001', '2023-03-17 14:43:55.887000', '9000000001', '16050', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16074', '4', '2023-03-17 15:10:28.752000', '15606', '16050', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "sp?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T06:10:28.695Z"
  }
}', NULL, 'receive'), 
  ('16075', '9000000001', '2023-03-17 15:10:28.769000', '9000000001', '16050', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16080', '9000000001', '2023-03-17 15:10:52.160000', '9000000001', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16089', '9000000001', '2023-03-17 15:17:57.403000', '9000000001', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16096', '9000000001', '2023-03-17 15:31:11.857000', '16091', '16094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dd"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T06:31:11.805Z"
  }
}', NULL, 'receive'), 
  ('16097', '9000000001', '2023-03-17 15:31:11.873000', '9000000001', '16094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16098', '9000000001', '2023-03-17 15:31:37.440000', '16091', '16094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "갑시다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T06:31:37.369Z"
  }
}', NULL, 'receive'), 
  ('16099', '9000000001', '2023-03-17 15:31:37.461000', '9000000001', '16094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16100', '9000000001', '2023-03-17 15:32:15.498000', '16091', '16094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T06:32:15.449Z"
  }
}', NULL, 'receive'), 
  ('16101', '9000000001', '2023-03-17 15:32:15.513000', '9000000001', '16094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16102', '9000000001', '2023-03-17 15:33:41.972000', '16091', '16094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T06:33:41.923Z"
  }
}', NULL, 'receive'), 
  ('16103', '9000000001', '2023-03-17 15:33:41.988000', '9000000001', '16094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16106', '9000000001', '2023-03-17 16:53:31.076000', '16091', '16104', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄷ닷"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T07:53:31.036Z"
  }
}', NULL, 'receive'), 
  ('16107', '9000000001', '2023-03-17 16:53:31.092000', '9000000001', '16104', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16108', '9000000001', '2023-03-17 16:54:12.842000', '16091', '16104', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T07:54:12.797Z"
  }
}', NULL, 'receive'), 
  ('16109', '9000000001', '2023-03-17 16:54:12.857000', '9000000001', '16104', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16110', '9000000001', '2023-03-17 16:54:25.420000', '16091', '16104', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T07:54:25.318Z"
  }
}', NULL, 'receive'), 
  ('16111', '9000000001', '2023-03-17 16:54:25.437000', '9000000001', '16104', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16112', '9000000001', '2023-03-17 16:55:03.262000', '16091', '16104', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T07:55:03.227Z"
  }
}', NULL, 'receive'), 
  ('16113', '9000000001', '2023-03-17 16:55:03.277000', '9000000001', '16104', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16118', '9000000001', '2023-03-17 16:56:33.582000', '16091', '16116', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "담시만"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T07:56:33.525Z"
  }
}', NULL, 'receive'), 
  ('16119', '9000000001', '2023-03-17 16:56:33.598000', '9000000001', '16116', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16120', '9000000001', '2023-03-17 16:56:44.525000', '16091', '16116', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "우짜냐"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T07:56:44.492Z"
  }
}', NULL, 'receive'), 
  ('16121', '9000000001', '2023-03-17 16:56:44.539000', '9000000001', '16116', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16122', '9000000001', '2023-03-17 16:58:40.908000', '16091', '16116', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇㄻ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T07:58:40.848Z"
  }
}', NULL, 'receive'), 
  ('16123', '9000000001', '2023-03-17 16:58:40.923000', '9000000001', '16116', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16129', '1', '2023-03-17 17:25:32.593000', '15585', '16127', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:25:32.539Z"
  }
}', NULL, 'receive'), 
  ('16130', '9000000001', '2023-03-17 17:25:32.611000', '9000000001', '16127', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16131', '1', '2023-03-17 17:26:02.768000', '15585', '16127', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:26:02.706Z"
  }
}', NULL, 'receive'), 
  ('16132', '9000000001', '2023-03-17 17:26:02.806000', '9000000001', '16127', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16135', '4', '2023-03-17 17:28:08.282000', '15585', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:28:08.223Z"
  }
}', NULL, 'receive'), 
  ('16136', '9000000001', '2023-03-17 17:28:08.301000', '9000000001', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16137', '4', '2023-03-17 17:28:09.732000', '15585', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:28:09.689Z"
  }
}', NULL, 'receive'), 
  ('16138', '9000000001', '2023-03-17 17:28:09.747000', '9000000001', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16139', '9000000001', '2023-03-17 17:28:27.758000', '9000000001', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16140', '4', '2023-03-17 17:28:27.762000', '4', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ggggg"
    } ]
  } ]
}', NULL, 'send'), 
  ('16143', '4', '2023-03-17 17:30:33.110000', '4', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dj"
    } ]
  } ]
}', NULL, 'send'), 
  ('16144', '9000000001', '2023-03-17 17:31:47.765000', '9000000001', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('16147', '4', '2023-03-17 17:31:55.460000', '4', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "!종료"
    } ]
  } ]
}', NULL, 'send'), 
  ('16148', '4', '2023-03-17 17:32:06.995000', '4', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대화중"
    } ]
  } ]
}', NULL, 'send'), 
  ('16149', '4', '2023-03-17 17:32:24.858000', '15585', '16133', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄴㄴ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:32:24.786Z"
  }
}', NULL, 'receive'), 
  ('16154', '4', '2023-03-17 17:34:56.840000', '15585', '16152', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:34:56.809Z"
  }
}', NULL, 'receive'), 
  ('16155', '9000000001', '2023-03-17 17:34:56.857000', '9000000001', '16152', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16156', '9000000001', '2023-03-17 17:35:20.959000', '9000000001', '16152', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16157', '4', '2023-03-17 17:35:20.964000', '4', '16152', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "가나다"
    } ]
  } ]
}', NULL, 'send'), 
  ('16158', '9000000001', '2023-03-17 17:37:27.586000', '9000000001', '16152', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16162', '9000000001', '2023-03-17 17:42:29.498000', '15585', '16160', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트듕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:42:29.441Z"
  }
}', NULL, 'receive'), 
  ('16163', '9000000001', '2023-03-17 17:42:29.518000', '9000000001', '16160', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16164', '9000000001', '2023-03-17 17:43:12.428000', '9000000001', '16160', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/5671620a-8b4c-46b9-8ce5-9c5315b5b383.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16165', '4', '2023-03-17 17:43:12.432000', '4', '16160', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ]
}', NULL, 'send'), 
  ('16167', '9000000001', '2023-03-17 17:45:27.944000', '9000000001', '16160', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16171', '4', '2023-03-17 17:46:08.700000', '15585', '16169', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:46:08.665Z"
  }
}', NULL, 'receive'), 
  ('16172', '9000000001', '2023-03-17 17:46:08.717000', '9000000001', '16169', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16173', '9000000001', '2023-03-17 17:46:17.860000', '9000000001', '16169', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16174', '4', '2023-03-17 17:46:17.864000', '4', '16169', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ]
}', NULL, 'send'), 
  ('16175', '9000000001', '2023-03-17 17:48:28.094000', '9000000001', '16169', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16177', '9000000001', '2023-03-17 17:50:38.949000', '16091', '16141', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄹㄹㄹ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:50:38.879Z"
  }
}', NULL, 'receive'), 
  ('16178', '9000000001', '2023-03-17 17:50:38.975000', '9000000001', '16141', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16179', '9000000001', '2023-03-17 17:50:42.460000', '16091', '16141', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "뢔 뢔"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T08:50:42.4Z"
  }
}', NULL, 'receive'), 
  ('16180', '9000000001', '2023-03-17 17:50:42.478000', '9000000001', '16141', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16182', '9000000001', '2023-03-17 17:53:28.331000', '9000000001', '16169', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16185', '9000000001', '2023-03-17 18:01:14.643000', '16091', '16141', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T09:01:14.59Z"
  }
}', NULL, 'receive'), 
  ('16186', '9000000001', '2023-03-17 18:01:14.659000', '9000000001', '16141', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16189', '9000000001', '2023-03-17 18:01:23.052000', '16091', '16187', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "빨리해내라"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T09:01:23.018Z"
  }
}', NULL, 'receive'), 
  ('16190', '9000000001', '2023-03-17 18:01:23.068000', '9000000001', '16187', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16195', '9000000001', '2023-03-17 18:01:44.297000', '9000000001', '16192', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16204', '9000000001', '2023-03-17 18:14:51.787000', '16091', '16200', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이상하네요."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T09:14:51.73Z"
  }
}', NULL, 'receive'), 
  ('16205', '9000000001', '2023-03-17 18:14:51.806000', '9000000001', '16200', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16206', '9000000001', '2023-03-17 18:15:37.947000', '16091', '16200', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T09:15:37.884Z"
  }
}', NULL, 'receive'), 
  ('16207', '9000000001', '2023-03-17 18:15:37.965000', '9000000001', '16200', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16210', '9000000001', '2023-03-17 18:16:38.378000', '16091', '16200', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "점사"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T09:16:38.332Z"
  }
}', NULL, 'receive'), 
  ('16211', '9000000001', '2023-03-17 18:16:38.395000', '9000000001', '16200', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16214', '15995', '2023-03-17 18:22:14.358000', '15606', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T09:22:14.289Z"
  }
}', NULL, 'receive'), 
  ('16215', '9000000001', '2023-03-17 18:22:14.374000', '9000000001', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16220', '9000000001', '2023-03-17 18:29:53.917000', '16091', '16218', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-17T09:29:53.859Z"
  }
}', NULL, 'receive'), 
  ('16221', '9000000001', '2023-03-17 18:29:53.934000', '9000000001', '16218', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16226', '9000000001', '2023-03-17 18:36:21.614000', '9000000001', '16212', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16227', '9000000001', '2023-03-17 18:36:21.614000', '9000000001', '16222', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16228', '9000000001', '2023-03-17 18:37:50.322000', '9000000001', '16222', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    } ]
  } ]
}', NULL, 'send'), 
  ('16229', '5', '2023-03-17 18:37:50.326000', '5', '16222', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아이고"
    } ]
  } ]
}', NULL, 'send'), 
  ('16233', '9000000001', '2023-03-17 18:38:55.824000', '9000000001', '16230', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16237', '9000000001', '2023-03-17 18:39:57.718000', '9000000001', '16234', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16241', '9000000001', '2023-03-17 18:42:32.787000', '9000000001', '16238', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16242', '9000000001', '2023-03-17 18:43:36.374000', '9000000001', '16212', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16247', '9000000001', '2023-03-17 18:49:22.401000', '9000000001', '16244', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16248', '9000000001', '2023-03-17 18:49:46.710000', '9000000001', '16238', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16253', '9000000001', '2023-03-17 18:50:08.772000', '9000000001', '16250', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16257', '9000000001', '2023-03-17 18:51:14.402000', '9000000001', '16254', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16258', '9000000001', '2023-03-17 18:58:17.603000', '9000000001', '16254', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16263', '9000000001', '2023-03-20 09:14:47.298000', '9000000001', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16264', '3', '2023-03-20 09:15:37.916000', '15688', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T00:15:37.802Z"
  }
}', NULL, 'receive'), 
  ('16265', '9000000001', '2023-03-20 09:15:37.939000', '9000000001', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16266', '3', '2023-03-20 09:16:56.891000', '15688', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 22"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T00:16:56.801Z"
  }
}', NULL, 'receive'), 
  ('16267', '9000000001', '2023-03-20 09:16:56.909000', '9000000001', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16272', '9000000001', '2023-03-20 09:23:56.999000', '9000000001', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16298', '15995', '2023-03-20 14:20:26.418000', '15606', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:20:26.312Z"
  }
}', NULL, 'receive'), 
  ('16299', '9000000001', '2023-03-20 14:20:26.438000', '9000000001', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16300', '15995', '2023-03-20 14:20:38.715000', '15606', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:20:38.678Z"
  }
}', NULL, 'receive'), 
  ('16301', '9000000001', '2023-03-20 14:20:38.733000', '9000000001', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16306', '9000000001', '2023-03-20 14:21:59.747000', '16302', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세용"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:21:59.647Z"
  }
}', NULL, 'receive'), 
  ('16307', '9000000001', '2023-03-20 14:21:59.763000', '9000000001', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16309', '9000000001', '2023-03-20 14:22:20.520000', '9000000001', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16321', '15995', '2023-03-20 14:24:47.345000', '15606', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:24:47.29Z"
  }
}', NULL, 'receive'), 
  ('16322', '9000000001', '2023-03-20 14:24:47.366000', '9000000001', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16323', '15995', '2023-03-20 14:25:02.562000', '15606', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "녱"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:25:02.528Z"
  }
}', NULL, 'receive'), 
  ('16324', '9000000001', '2023-03-20 14:25:02.577000', '9000000001', '16077', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16330', '9000000001', '2023-03-20 14:26:20.945000', '9000000001', '16327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16331', '16293', '2023-03-20 14:26:25.633000', '15606', '16327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "녱"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:26:25.58Z"
  }
}', NULL, 'receive'), 
  ('16332', '9000000001', '2023-03-20 14:26:25.655000', '9000000001', '16327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16333', '16293', '2023-03-20 14:26:43.418000', '15606', '16327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:26:43.382Z"
  }
}', NULL, 'receive'), 
  ('16334', '9000000001', '2023-03-20 14:26:43.435000', '9000000001', '16327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16342', '9000000001', '2023-03-20 14:27:48.390000', '9000000001', '16339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16343', '4', '2023-03-20 14:27:52.605000', '15606', '16339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:27:52.575Z"
  }
}', NULL, 'receive'), 
  ('16344', '9000000001', '2023-03-20 14:27:52.622000', '9000000001', '16339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16345', '9000000001', '2023-03-20 14:28:04.912000', '9000000001', '16339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16346', '4', '2023-03-20 14:28:04.917000', '4', '16339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹"
    } ]
  } ]
}', NULL, 'send'), 
  ('16348', '9000000001', '2023-03-20 14:29:47.122000', '9000000001', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16350', '9000000001', '2023-03-20 14:30:30.589000', '9000000001', '16339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16355', '9000000001', '2023-03-20 14:35:30.820000', '9000000001', '16339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16361', '9000000001', '2023-03-20 14:41:52.571000', '9000000001', '16358', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16362', '4', '2023-03-20 14:41:56.353000', '15606', '16358', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "호호"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:41:56.306Z"
  }
}', NULL, 'receive'), 
  ('16363', '9000000001', '2023-03-20 14:41:56.369000', '9000000001', '16358', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16364', '9000000001', '2023-03-20 14:42:18.583000', '9000000001', '16358', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16365', '4', '2023-03-20 14:42:18.588000', '4', '16358', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "옐"
    } ]
  } ]
}', NULL, 'send'), 
  ('16366', '9000000001', '2023-03-20 14:44:31.069000', '9000000001', '16358', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16368', '4', '2023-03-20 14:45:09.639000', '4', '16358', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안됨!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16376', '9000000001', '2023-03-20 14:48:06.707000', '9000000001', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16377', '15995', '2023-03-20 14:48:15.329000', '15606', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇ[?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:48:15.293Z"
  }
}', NULL, 'receive'), 
  ('16378', '9000000001', '2023-03-20 14:48:15.345000', '9000000001', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16379', '9000000001', '2023-03-20 14:48:23.293000', '9000000001', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text"
    } ]
  } ]
}', NULL, 'send'), 
  ('16380', '15995', '2023-03-20 14:48:23.298000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16381', '15995', '2023-03-20 14:48:25.145000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16382', '15995', '2023-03-20 14:48:26.564000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16383', '15995', '2023-03-20 14:48:34.322000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "에"
    } ]
  } ]
}', NULL, 'send'), 
  ('16384', '15995', '2023-03-20 14:49:04.450000', '15606', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄴ["
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:49:04.386Z"
  }
}', NULL, 'receive'), 
  ('16385', '15995', '2023-03-20 14:49:08.855000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16386', '15995', '2023-03-20 14:49:14.489000', '15606', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:49:14.432Z"
  }
}', NULL, 'receive'), 
  ('16387', '15995', '2023-03-20 14:49:21.970000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16388', '15995', '2023-03-20 14:49:22.330000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16389', '15995', '2023-03-20 14:49:22.674000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16390', '15995', '2023-03-20 14:49:23.122000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16391', '15995', '2023-03-20 14:49:23.381000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16392', '15995', '2023-03-20 14:49:34.160000', '15606', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:49:34.128Z"
  }
}', NULL, 'receive'), 
  ('16393', '16293', '2023-03-20 14:53:19.431000', '16302', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "모양"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T05:53:19.385Z"
  }
}', NULL, 'receive'), 
  ('16394', '9000000001', '2023-03-20 14:53:19.446000', '9000000001', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16410', '15995', '2023-03-20 15:08:33.281000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16411', '15995', '2023-03-20 15:08:36.594000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16412', '15995', '2023-03-20 15:08:41.023000', '15995', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅁㄴㅇㅁㄴㅇㅁㄴㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16413', '15995', '2023-03-20 15:09:32.080000', '15606', '16373', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T06:09:32.011Z"
  }
}', NULL, 'receive'), 
  ('16435', '9000000001', '2023-03-20 16:18:58.271000', '9000000001', '16432', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16436', '5', '2023-03-20 16:18:59.964000', '15606', '16432', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안옇사에요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:18:59.929Z"
  }
}', NULL, 'receive'), 
  ('16437', '9000000001', '2023-03-20 16:18:59.986000', '9000000001', '16432', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16438', '9000000001', '2023-03-20 16:20:55.170000', '9000000001', '16432', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    } ]
  } ]
}', NULL, 'send'), 
  ('16439', '5', '2023-03-20 16:20:55.174000', '5', '16432', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16440', '9000000001', '2023-03-20 16:21:00.922000', '9000000001', '16432', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('16446', '9000000001', '2023-03-20 16:22:04.812000', '15606', '16444', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:22:04.764Z"
  }
}', NULL, 'receive'), 
  ('16447', '9000000001', '2023-03-20 16:22:04.828000', '9000000001', '16444', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16449', '9000000001', '2023-03-20 16:22:05.145000', '9000000001', '16444', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16451', '4', '2023-03-20 16:25:39.814000', '15606', '16444', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "흥"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:25:39.748Z"
  }
}', NULL, 'receive'), 
  ('16452', '9000000001', '2023-03-20 16:25:39.830000', '9000000001', '16444', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16457', '9000000001', '2023-03-20 16:25:56.751000', '9000000001', '16454', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16458', '5', '2023-03-20 16:25:57.956000', '15606', '16454', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:25:57.905Z"
  }
}', NULL, 'receive'), 
  ('16459', '9000000001', '2023-03-20 16:25:57.971000', '9000000001', '16454', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16464', '9000000001', '2023-03-20 16:27:13.237000', '9000000001', '16461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16465', '4', '2023-03-20 16:27:13.901000', '15606', '16461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:27:13.841Z"
  }
}', NULL, 'receive'), 
  ('16466', '9000000001', '2023-03-20 16:27:13.917000', '9000000001', '16461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16471', '9000000001', '2023-03-20 16:28:23.902000', '9000000001', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16472', '15995', '2023-03-20 16:28:28.383000', '15606', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넴"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:28:28.334Z"
  }
}', NULL, 'receive'), 
  ('16473', '9000000001', '2023-03-20 16:28:28.399000', '9000000001', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16474', '9000000001', '2023-03-20 16:30:56.017000', '9000000001', '16254', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    } ]
  } ]
}', NULL, 'send'), 
  ('16475', '5', '2023-03-20 16:30:56.022000', '5', '16254', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넴"
    } ]
  } ]
}', NULL, 'send'), 
  ('16476', '5', '2023-03-20 16:31:31.716000', '5', '16254', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16477', '15995', '2023-03-20 16:31:35.040000', '15606', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:31:34.985Z"
  }
}', NULL, 'receive'), 
  ('16478', '9000000001', '2023-03-20 16:31:35.056000', '9000000001', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16479', '9000000001', '2023-03-20 16:32:01.892000', '9000000001', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text"
    } ]
  } ]
}', NULL, 'send'), 
  ('16480', '15995', '2023-03-20 16:32:01.896000', '15995', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "sp"
    } ]
  } ]
}', NULL, 'send'), 
  ('16481', '15995', '2023-03-20 16:32:23.806000', '15606', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "sp"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:32:23.758Z"
  }
}', NULL, 'receive'), 
  ('16482', '9000000001', '2023-03-20 16:33:33.932000', '9000000001', '16254', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16487', '15995', '2023-03-20 16:35:47.931000', '15995', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넴"
    } ]
  } ]
}', NULL, 'send'), 
  ('16488', '9000000001', '2023-03-20 16:35:48.964000', '9000000001', '16468', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('16493', '9000000001', '2023-03-20 16:36:06.232000', '15606', '16491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아뇽"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:36:06.176Z"
  }
}', NULL, 'receive'), 
  ('16494', '9000000001', '2023-03-20 16:36:06.248000', '9000000001', '16491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16496', '9000000001', '2023-03-20 16:36:06.908000', '9000000001', '16491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16501', '9000000001', '2023-03-20 16:36:28.435000', '9000000001', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16502', '15995', '2023-03-20 16:36:30.020000', '15606', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:36:29.957Z"
  }
}', NULL, 'receive'), 
  ('16503', '9000000001', '2023-03-20 16:36:30.041000', '9000000001', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16511', '9000000001', '2023-03-20 16:36:53.090000', '9000000001', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16512', '15998', '2023-03-20 16:37:11.308000', '15606', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오잉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:37:11.269Z"
  }
}', NULL, 'receive'), 
  ('16513', '9000000001', '2023-03-20 16:37:11.330000', '9000000001', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16515', '9000000001', '2023-03-20 16:37:29.911000', '9000000001', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎㅎㅎㅎ"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/afadac6d-3063-4916-92bf-e77d871a94eb.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16516', '15998', '2023-03-20 16:37:29.915000', '15998', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오잉?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16517', '15998', '2023-03-20 16:37:37.236000', '15998', '16498', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "!종료"
    } ]
  } ]
}', NULL, 'send'), 
  ('16521', '9000000001', '2023-03-20 16:38:34.184000', '9000000001', '16254', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16526', '9000000001', '2023-03-20 16:38:41.203000', '9000000001', '16523', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16527', '9000000001', '2023-03-20 16:38:54.182000', '9000000001', '16523', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text"
    } ]
  } ]
}', NULL, 'send'), 
  ('16528', '15995', '2023-03-20 16:38:54.186000', '15995', '16523', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dkssuddf"
    } ]
  } ]
}', NULL, 'send'), 
  ('16529', '15995', '2023-03-20 16:38:58.614000', '15606', '16523', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:38:58.561Z"
  }
}', NULL, 'receive'), 
  ('16530', '15995', '2023-03-20 16:39:27.449000', '15995', '16523', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16538', '9000000001', '2023-03-20 16:39:35.569000', '9000000001', '16523', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16542', '9000000001', '2023-03-20 16:40:21.020000', '15606', '16540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:40:20.975Z"
  }
}', NULL, 'receive'), 
  ('16543', '9000000001', '2023-03-20 16:40:21.042000', '9000000001', '16540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16548', '9000000001', '2023-03-20 16:41:01.099000', '9000000001', '16545', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16549', '4', '2023-03-20 16:41:02.145000', '15606', '16545', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:41:02.098Z"
  }
}', NULL, 'receive'), 
  ('16550', '9000000001', '2023-03-20 16:41:02.161000', '9000000001', '16545', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16551', '9000000001', '2023-03-20 16:41:07.632000', '9000000001', '16545', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16552', '4', '2023-03-20 16:41:07.636000', '4', '16545', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16560', '9000000001', '2023-03-20 16:43:31.464000', '9000000001', '16557', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16561', '9000000001', '2023-03-20 16:43:52.087000', '9000000001', '16557', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16562', '4', '2023-03-20 16:43:52.092000', '4', '16557', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "녱?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16567', '9000000001', '2023-03-20 16:46:04.425000', '9000000001', '16557', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16570', '4', '2023-03-20 16:46:26.086000', '4', '16557', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "123"
    } ]
  } ]
}', NULL, 'send'), 
  ('16572', '9000000001', '2023-03-20 16:49:20.911000', '9000000001', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담사 인사말"
    } ]
  } ]
}', NULL, 'send'), 
  ('16573', '16293', '2023-03-20 16:49:20.916000', '16293', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅂㅈㄷㅂㅈㄷ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16574', '16293', '2023-03-20 16:49:48.096000', '16302', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T07:49:46.99Z"
  }
}', NULL, 'receive'), 
  ('16576', '9000000001', '2023-03-20 16:51:04.694000', '9000000001', '16557', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16586', '9000000001', '2023-03-20 16:56:14.908000', '9000000001', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16599', '9000000001', '2023-03-20 19:57:29.491000', '15606', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T10:57:29.035Z"
  }
}', NULL, 'receive'), 
  ('16600', '9000000001', '2023-03-20 19:57:29.506000', '9000000001', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16601', '9000000001', '2023-03-20 19:57:50', '15606', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T10:57:49.967Z"
  }
}', NULL, 'receive'), 
  ('16602', '9000000001', '2023-03-20 19:57:50.022000', '9000000001', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16603', '9000000001', '2023-03-20 19:58:39.184000', '15606', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T10:58:38.723Z"
  }
}', NULL, 'receive'), 
  ('16604', '9000000001', '2023-03-20 19:58:39.199000', '9000000001', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16606', '9000000001', '2023-03-20 20:07:33.493000', '9000000001', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16607', '4', '2023-03-20 20:13:04.794000', '15606', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "녱?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T11:13:04.715Z"
  }
}', NULL, 'receive'), 
  ('16608', '9000000001', '2023-03-20 20:13:04.810000', '9000000001', '16597', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16611', '9000000001', '2023-03-20 20:13:23.860000', '15606', '16609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T11:13:23.796Z"
  }
}', NULL, 'receive'), 
  ('16612', '9000000001', '2023-03-20 20:13:23.874000', '9000000001', '16609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16614', '9000000001', '2023-03-20 20:13:24.347000', '9000000001', '16609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16621', '9000000001', '2023-03-20 20:14:46.377000', '15606', '16619', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T11:14:46.345Z"
  }
}', NULL, 'receive'), 
  ('16622', '9000000001', '2023-03-20 20:14:46.392000', '9000000001', '16619', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16623', '9000000001', '2023-03-20 20:14:48.672000', '15606', '16619', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T11:14:48.641Z"
  }
}', NULL, 'receive'), 
  ('16624', '9000000001', '2023-03-20 20:14:48.686000', '9000000001', '16619', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16628', '9000000001', '2023-03-20 20:17:21.307000', '15606', '16626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T11:17:21.259Z"
  }
}', NULL, 'receive'), 
  ('16629', '9000000001', '2023-03-20 20:17:21.324000', '9000000001', '16626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16630', '9000000001', '2023-03-20 20:31:19.474000', '15606', '16626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "마지막 이슈입니다.(내일 확인해주셔도 됩니다.)\n\n내 정보 수정에서 상담원 개별 근무 시간 설정 후,\n1) 설정한 상담 시간이 지나도 상담 가능으로 on 되어있음.\n2) 상담 가능/불가능 여부와 상관없이, 설정한 상담시간 이후에 고객이 해당 상담원에게 direct로 1:1 채팅 요청할 경우 member_id 가 null 로 배정 및 고객에게 안내문구 없음"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T11:31:19.389Z"
  }
}', NULL, 'receive'), 
  ('16631', '9000000001', '2023-03-20 20:31:19.492000', '9000000001', '16626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16635', '9000000001', '2023-03-20 20:31:48.645000', '15606', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "엥"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T11:31:48.58Z"
  }
}', NULL, 'receive'), 
  ('16636', '9000000001', '2023-03-20 20:31:48.662000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16637', '9000000001', '2023-03-20 20:35:31.181000', '15606', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZOJirjsMm/ONRemVZ8iqHfLV6Fe5HaU1/i_50c71bb04e7c.png",
      "display" : "image"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-20T11:35:31.128Z"
  }
}', NULL, 'receive'), 
  ('16638', '9000000001', '2023-03-20 20:35:31.195000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16647', '9000000001', '2023-03-21 10:45:16.321000', '9000000001', '16644', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16654', '9000000001', '2023-03-21 10:48:09.578000', '9000000001', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16655', '3', '2023-03-21 10:48:09.583000', '3', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㄹㄹㄴㅇㅁ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16656', '9000000001', '2023-03-21 10:48:14.881000', '9000000001', '16644', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16657', '3', '2023-03-21 10:48:14.885000', '3', '16644', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "인냥히쇼[여"
    } ]
  } ]
}', NULL, 'send'), 
  ('16658', '9000000001', '2023-03-21 10:48:15.029000', '9000000001', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16659', '3', '2023-03-21 10:48:15.032000', '3', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㄹㅇㅁㄴ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16660', '3', '2023-03-21 10:48:28.826000', '3', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16661', '3', '2023-03-21 10:48:31.674000', '3', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16662', '3', '2023-03-21 10:48:37.593000', '16302', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오잉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T01:48:35.669Z"
  }
}', NULL, 'receive'), 
  ('16663', '3', '2023-03-21 10:49:21.865000', '3', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16664', '3', '2023-03-21 10:49:25.512000', '3', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16665', '3', '2023-03-21 10:49:27.564000', '3', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "간단"
    } ]
  } ]
}', NULL, 'send'), 
  ('16666', '3', '2023-03-21 10:49:30.049000', '3', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "가ㅏㅏㅏ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16667', '3', '2023-03-21 10:49:38.334000', '3', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16668', '3', '2023-03-21 10:49:48.466000', '16302', '16304', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어딜 가시나요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T01:49:48.41Z"
  }
}', NULL, 'receive'), 
  ('16669', '3', '2023-03-21 10:49:55.870000', '3', '16644', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/file/2023/03/0d246dfc-f9f0-466d-b58b-570228558ccb.html",
      "display" : "text/html"
    } ]
  } ]
}', NULL, 'send'), 
  ('16671', '9000000001', '2023-03-21 10:52:02.155000', '9000000001', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16673', '9000000001', '2023-03-21 10:52:02.190000', '9000000001', '16644', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16676', '9000000001', '2023-03-21 10:57:02.424000', '9000000001', '16260', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16680', '9000000001', '2023-03-21 15:09:26.611000', '9000000001', '16677', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16681', '3', '2023-03-21 15:10:57.781000', '16091', '16677', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잠시만"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T06:10:57.725Z"
  }
}', NULL, 'receive'), 
  ('16682', '9000000001', '2023-03-21 15:10:57.802000', '9000000001', '16677', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16686', '9000000001', '2023-03-21 15:12:32.098000', '9000000001', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16687', '9000000001', '2023-03-21 15:16:59.133000', '9000000001', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    } ]
  } ]
}', NULL, 'send'), 
  ('16688', '5', '2023-03-21 15:16:59.137000', '5', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "죄송해요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16689', '5', '2023-03-21 15:18:23.022000', '5', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "거기까지"
    } ]
  } ]
}', NULL, 'send'), 
  ('16690', '5', '2023-03-21 15:18:30.897000', '5', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잠심"
    } ]
  } ]
}', NULL, 'send'), 
  ('16691', '5', '2023-03-21 15:18:46.776000', '5', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잠시만"
    } ]
  } ]
}', NULL, 'send'), 
  ('16692', '5', '2023-03-21 15:18:50.485000', '5', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇ;ㄱ["
    } ]
  } ]
}', NULL, 'send'), 
  ('16693', '5', '2023-03-21 15:18:53.768000', '5', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아닌데"
    } ]
  } ]
}', NULL, 'send'), 
  ('16694', '5', '2023-03-21 15:18:59.039000', '5', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16695', '9000000001', '2023-03-21 15:21:08.743000', '9000000001', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16698', '9000000001', '2023-03-21 15:26:08.966000', '9000000001', '16683', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16702', '9000000001', '2023-03-21 15:46:07.839000', '9000000001', '16699', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16703', '5', '2023-03-21 15:51:50.637000', '16091', '16699', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잠시만"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T06:51:50.567Z"
  }
}', NULL, 'receive'), 
  ('16704', '9000000001', '2023-03-21 15:51:50.653000', '9000000001', '16699', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16705', '9000000001', '2023-03-21 15:59:10.348000', '9000000001', '16699', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16707', '5', '2023-03-21 16:33:28.129000', '16091', '16699', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ''"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T07:33:28.026Z"
  }
}', NULL, 'receive'), 
  ('16708', '9000000001', '2023-03-21 16:33:28.150000', '9000000001', '16699', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16713', '9000000001', '2023-03-21 18:06:14.974000', '9000000001', '16710', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16714', '3', '2023-03-21 18:07:58.250000', '15567', '16710', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "123"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T09:07:58.123Z"
  }
}', NULL, 'receive'), 
  ('16715', '9000000001', '2023-03-21 18:07:58.270000', '9000000001', '16710', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16719', '9000000001', '2023-03-21 18:11:10.495000', '9000000001', '16716', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16723', '9000000001', '2023-03-21 18:11:43.020000', '9000000001', '16720', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16725', '9000000001', '2023-03-21 18:14:31.334000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16726', '4', '2023-03-21 18:15:22.352000', '15606', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "냅"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T09:15:21.246Z"
  }
}', NULL, 'receive'), 
  ('16727', '9000000001', '2023-03-21 18:15:22.375000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16731', '9000000001', '2023-03-21 18:16:35.425000', '9000000001', '16728', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16732', '3', '2023-03-21 18:18:39.658000', '15567', '16728', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "123"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T09:18:39.579Z"
  }
}', NULL, 'receive'), 
  ('16733', '9000000001', '2023-03-21 18:18:39.684000', '9000000001', '16728', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16738', '9000000001', '2023-03-21 18:19:38.570000', '9000000001', '16735', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16739', '9000000001', '2023-03-21 18:22:36.343000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16745', '9000000001', '2023-03-21 18:33:02.728000', '9000000001', '16742', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16746', '5', '2023-03-21 18:33:13.431000', '15567', '16742', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "123"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-21T09:33:13.366Z"
  }
}', NULL, 'receive'), 
  ('16747', '9000000001', '2023-03-21 18:33:13.450000', '9000000001', '16742', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16748', '9000000001', '2023-03-21 18:40:37.583000', '9000000001', '16742', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16753', '9000000001', '2023-03-21 18:41:37.069000', '9000000001', '16751', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('16757', '9000000001', '2023-03-22 09:53:42.183000', '9000000001', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16758', '9000000001', '2023-03-22 09:54:51.580000', '9000000001', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16759', '9000000001', '2023-03-22 09:54:51.588000', '9000000001', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16760', '3', '2023-03-22 09:54:51.592000', '3', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16761', '3', '2023-03-22 09:54:56.201000', '3', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잘 지내시죠?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16762', '3', '2023-03-22 09:55:00.298000', '16091', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아닌가요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T00:55:00.254Z"
  }
}', NULL, 'receive'), 
  ('16763', '3', '2023-03-22 09:55:06.198000', '3', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "몰라요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('16765', '9000000001', '2023-03-22 09:57:30.355000', '9000000001', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16768', '9000000001', '2023-03-22 10:02:30.586000', '9000000001', '16754', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16769', '9000000001', '2023-03-22 10:52:39.675000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16770', '9000000001', '2023-03-22 10:52:39.683000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16771', '4', '2023-03-22 10:52:39.687000', '4', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하하"
    } ]
  } ]
}', NULL, 'send'), 
  ('16772', '9000000001', '2023-03-22 10:55:01.811000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16774', '4', '2023-03-22 10:55:17.993000', '15606', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "놉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T01:55:16.831Z"
  }
}', NULL, 'receive'), 
  ('16776', '4', '2023-03-22 13:03:17.771000', '4', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('16778', '9000000001', '2023-03-22 13:03:30.492000', '9000000001', '16633', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('16781', '9000000001', '2023-03-22 13:46:11.831000', '15606', '16779', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안됑"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T04:46:10.768Z"
  }
}', NULL, 'receive'), 
  ('16782', '9000000001', '2023-03-22 13:46:11.852000', '9000000001', '16779', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16784', '9000000001', '2023-03-22 13:46:12.342000', '9000000001', '16779', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16785', '9000000001', '2023-03-22 13:47:21.156000', '9000000001', '16779', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16786', '9000000001', '2023-03-22 13:47:21.169000', '9000000001', '16779', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16787', '4', '2023-03-22 13:47:21.176000', '4', '16779', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('16788', '4', '2023-03-22 13:47:36.589000', '4', '16779', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16789', '9000000001', '2023-03-22 13:48:05.002000', '9000000001', '16779', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('16794', '9000000001', '2023-03-22 13:48:16.524000', '15606', '16792', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T04:48:16.47Z"
  }
}', NULL, 'receive'), 
  ('16795', '9000000001', '2023-03-22 13:48:16.542000', '9000000001', '16792', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16797', '9000000001', '2023-03-22 13:48:17.455000', '9000000001', '16792', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16800', '9000000001', '2023-03-22 13:50:14.990000', '15606', '16798', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "왜져"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T04:50:14.924Z"
  }
}', NULL, 'receive'), 
  ('16801', '9000000001', '2023-03-22 13:50:15.008000', '9000000001', '16798', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16803', '9000000001', '2023-03-22 13:50:16.160000', '9000000001', '16798', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16804', '9000000001', '2023-03-22 13:50:24.234000', '9000000001', '16798', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16805', '9000000001', '2023-03-22 13:50:24.243000', '9000000001', '16798', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/c7ff0038-04ab-4060-b4c0-a539075abccd.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16806', '4', '2023-03-22 13:50:24.247000', '4', '16798', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('16807', '9000000001', '2023-03-22 13:50:45.915000', '9000000001', '16798', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('16812', '9000000001', '2023-03-22 13:50:54.959000', '15606', '16810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T04:50:54.924Z"
  }
}', NULL, 'receive'), 
  ('16813', '9000000001', '2023-03-22 13:50:54.977000', '9000000001', '16810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16815', '9000000001', '2023-03-22 13:50:56.300000', '9000000001', '16810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16816', '5', '2023-03-22 13:51:01.804000', '15606', '16810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "로"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T04:51:01.72Z"
  }
}', NULL, 'receive'), 
  ('16817', '9000000001', '2023-03-22 13:51:01.818000', '9000000001', '16810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16818', '9000000001', '2023-03-22 13:58:02.128000', '9000000001', '16810', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16827', '9000000001', '2023-03-22 15:14:06.288000', '9000000001', '16824', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16828', '9000000001', '2023-03-22 15:21:35.910000', '9000000001', '16824', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16834', '9000000001', '2023-03-22 15:26:52.857000', '9000000001', '16831', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16840', '9000000001', '2023-03-22 15:27:52.828000', '9000000001', '16837', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16841', '5', '2023-03-22 15:28:20.581000', '16835', '16837', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하잉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:28:20.529Z"
  }
}', NULL, 'receive'), 
  ('16842', '9000000001', '2023-03-22 15:28:20.601000', '9000000001', '16837', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16847', '9000000001', '2023-03-22 15:28:27.084000', '9000000001', '16844', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16852', '9000000001', '2023-03-22 15:31:22.183000', '9000000001', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16853', '15995', '2023-03-22 15:31:28.864000', '15606', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:31:28.831Z"
  }
}', NULL, 'receive'), 
  ('16854', '9000000001', '2023-03-22 15:31:28.882000', '9000000001', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16855', '9000000001', '2023-03-22 15:31:32.522000', '9000000001', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16856', '9000000001', '2023-03-22 15:31:32.530000', '9000000001', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16857', '15995', '2023-03-22 15:31:32.534000', '15995', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네ㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('16858', '9000000001', '2023-03-22 15:33:34.510000', '9000000001', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16860', '9000000001', '2023-03-22 15:34:06.590000', '9000000001', '16831', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16866', '15995', '2023-03-22 15:36:39.737000', '15606', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "https://bizmessage.kakao.com/chat/open/@nzc2qzu2odfvbm5?extra=path_nzc2qzu2odfvbm5__bid_15986__mid_16862"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:36:39.663Z"
  }
}', NULL, 'receive'), 
  ('16867', '15995', '2023-03-22 15:36:47.680000', '15606', '16849', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:36:47.619Z"
  }
}', NULL, 'receive'), 
  ('16872', '9000000001', '2023-03-22 15:40:11.297000', '9000000001', '16869', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16873', '16862', '2023-03-22 15:40:13.472000', '15606', '16869', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dld"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:40:13.421Z"
  }
}', NULL, 'receive'), 
  ('16874', '9000000001', '2023-03-22 15:40:13.487000', '9000000001', '16869', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16875', '16862', '2023-03-22 15:40:25.866000', '16862', '16869', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('16876', '16862', '2023-03-22 15:40:35.232000', '16862', '16869', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16879', '9000000001', '2023-03-22 15:41:07.054000', '15606', '16877', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어겐"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:41:07.033Z"
  }
}', NULL, 'receive'), 
  ('16880', '9000000001', '2023-03-22 15:41:07.070000', '9000000001', '16877', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16885', '9000000001', '2023-03-22 15:41:30.310000', '9000000001', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16886', '16862', '2023-03-22 15:41:30.830000', '15606', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:41:30.781Z"
  }
}', NULL, 'receive'), 
  ('16887', '9000000001', '2023-03-22 15:41:30.847000', '9000000001', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16888', '16862', '2023-03-22 15:41:36.980000', '15606', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:41:36.934Z"
  }
}', NULL, 'receive'), 
  ('16889', '9000000001', '2023-03-22 15:41:37.005000', '9000000001', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16890', '16862', '2023-03-22 15:41:41.950000', '16862', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('16891', '9000000001', '2023-03-22 15:44:04.889000', '9000000001', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16893', '16862', '2023-03-22 15:47:20.764000', '16862', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "노노"
    } ]
  } ]
}', NULL, 'send'), 
  ('16894', '16862', '2023-03-22 15:47:56.909000', '15606', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : " 대롸중"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-22T06:47:56.857Z"
  }
}', NULL, 'receive'), 
  ('16904', '9000000001', '2023-03-23 21:58:00.398000', '9000000001', '16901', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16905', '9000000001', '2023-03-23 21:59:43.749000', '9000000001', '16901', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16906', '9000000001', '2023-03-23 21:59:43.760000', '9000000001', '16901', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('16908', '5', '2023-03-23 21:59:43.808000', '5', '16901', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미안해요."
    } ]
  } ]
}', NULL, 'send'), 
  ('16909', '5', '2023-03-23 22:00:00.593000', '16091', '16901', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니예요."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-23T13:00:00.301Z"
  }
}', NULL, 'receive'), 
  ('16917', '9000000001', '2023-03-24 15:23:19.666000', '9000000001', '16914', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16919', '9000000001', '2023-03-24 15:30:49.265000', '9000000001', '16914', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16931', '9000000001', '2023-03-24 16:27:36.552000', '9000000001', '16928', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16932', '15995', '2023-03-24 16:27:48.587000', '16926', '16928', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담 시작"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T07:27:48.449Z"
  }
}', NULL, 'receive'), 
  ('16933', '9000000001', '2023-03-24 16:27:48.606000', '9000000001', '16928', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16934', '9000000001', '2023-03-24 16:28:05.201000', '9000000001', '16928', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16935', '9000000001', '2023-03-24 16:28:05.210000', '9000000001', '16928', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16937', '15995', '2023-03-24 16:28:05.243000', '15995', '16928', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('16938', '9000000001', '2023-03-24 16:28:22.641000', '9000000001', '16928', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('16944', '9000000001', '2023-03-24 16:29:09.159000', '9000000001', '16941', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16945', '5', '2023-03-24 16:29:11.928000', '16926', '16941', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T07:29:11.879Z"
  }
}', NULL, 'receive'), 
  ('16946', '9000000001', '2023-03-24 16:29:11.949000', '9000000001', '16941', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16951', '9000000001', '2023-03-24 16:29:39.100000', '9000000001', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16952', '15995', '2023-03-24 16:29:43.670000', '16926', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T07:29:43.621Z"
  }
}', NULL, 'receive'), 
  ('16953', '9000000001', '2023-03-24 16:29:43.687000', '9000000001', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16954', '9000000001', '2023-03-24 16:29:52.963000', '9000000001', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16955', '9000000001', '2023-03-24 16:29:52.971000', '9000000001', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16957', '15995', '2023-03-24 16:29:53.003000', '15995', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그래그래"
    } ]
  } ]
}', NULL, 'send'), 
  ('16959', '15995', '2023-03-24 16:30:39.940000', '15995', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16960', '15995', '2023-03-24 16:32:02.883000', '15995', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16961', '15995', '2023-03-24 16:32:09.489000', '15995', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "123"
    } ]
  } ]
}', NULL, 'send'), 
  ('16962', '15995', '2023-03-24 16:32:25.422000', '15995', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "test 2"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "test",
        "data" : "test",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('16963', '9000000001', '2023-03-24 16:34:50.753000', '9000000001', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('16975', '9000000001', '2023-03-24 16:37:44.434000', '9000000001', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16976', '15998', '2023-03-24 16:38:01.592000', '16926', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "누구니?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T07:38:01.516Z"
  }
}', NULL, 'receive'), 
  ('16977', '9000000001', '2023-03-24 16:38:01.614000', '9000000001', '16948', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16982', '9000000001', '2023-03-24 16:38:23.560000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('16983', '15995', '2023-03-24 16:38:26.985000', '16926', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T07:38:26.929Z"
  }
}', NULL, 'receive'), 
  ('16984', '9000000001', '2023-03-24 16:38:27.002000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16986', '9000000001', '2023-03-24 16:45:53.844000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('16988', '15995', '2023-03-24 16:46:11.036000', '16926', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "뭐야뭐야! "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T07:46:10.956Z"
  }
}', NULL, 'receive'), 
  ('16989', '9000000001', '2023-03-24 16:46:11.060000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16990', '15995', '2023-03-24 16:46:14.172000', '16926', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "빨리 응대해줘"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T07:46:14.107Z"
  }
}', NULL, 'receive'), 
  ('16991', '9000000001', '2023-03-24 16:46:14.190000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16994', '15995', '2023-03-24 17:01:21.853000', '16926', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아직 안끊어졌니?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T08:01:21.778Z"
  }
}', NULL, 'receive'), 
  ('16995', '9000000001', '2023-03-24 17:01:21.874000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16996', '15995', '2023-03-24 17:01:24.605000', '16926', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오~ "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T08:01:24.541Z"
  }
}', NULL, 'receive'), 
  ('16997', '9000000001', '2023-03-24 17:01:24.630000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('16998', '9000000001', '2023-03-24 17:01:32.417000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('16999', '9000000001', '2023-03-24 17:01:32.426000', '9000000001', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('17001', '15995', '2023-03-24 17:01:32.456000', '15995', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이럴수가"
    } ]
  } ]
}', NULL, 'send'), 
  ('17002', '15995', '2023-03-24 17:01:49.833000', '15995', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "화면이 짤린다"
    } ]
  } ]
}', NULL, 'send'), 
  ('17003', '15995', '2023-03-24 17:01:52.637000', '15995', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "큰일이다 ㅠ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17004', '15995', '2023-03-24 17:02:08.127000', '16926', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "기획자 뭐하니"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T08:02:08.056Z"
  }
}', NULL, 'receive'), 
  ('17005', '15995', '2023-03-24 17:02:13.458000', '16926', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "화면 사이즈 안정해주니?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T08:02:13.419Z"
  }
}', NULL, 'receive'), 
  ('17006', '15995', '2023-03-24 17:02:24.100000', '15995', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "알아서 잘 해주세요 ㅠ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17007', '15995', '2023-03-24 17:02:29.707000', '16926', '16979', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "젠장 !!!!"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T08:02:29.658Z"
  }
}', NULL, 'receive'), 
  ('17016', '9000000001', '2023-03-24 18:10:50.916000', '9000000001', '16824', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17017', '9000000001', '2023-03-24 18:10:50.926000', '9000000001', '16824', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17019', '4', '2023-03-24 18:10:50.954000', '4', '16824', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잇"
    } ]
  } ]
}', NULL, 'send'), 
  ('17020', '9000000001', '2023-03-24 18:12:53.394000', '9000000001', '16824', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17023', '9000000001', '2023-03-24 18:17:53.659000', '9000000001', '16824', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17032', '9000000001', '2023-03-24 19:05:42.586000', '9000000001', '17029', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17034', '9000000001', '2023-03-24 19:13:01.422000', '9000000001', '17029', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17049', '9000000001', '2023-03-24 20:59:18.184000', '9000000001', '17046', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17050', '9000000001', '2023-03-24 21:00:03.986000', '9000000001', '17046', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17051', '9000000001', '2023-03-24 21:00:03.999000', '9000000001', '17046', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17053', '3', '2023-03-24 21:00:04.040000', '3', '17046', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "rmfodey"
    } ]
  } ]
}', NULL, 'send'), 
  ('17055', '3', '2023-03-24 21:00:10.193000', '3', '17046', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17056', '3', '2023-03-24 21:00:21.010000', '16091', '17046', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미남이세요."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T12:00:20.908Z"
  }
}', NULL, 'receive'), 
  ('17058', '3', '2023-03-24 21:05:50.765000', '16091', '17046', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그렇군요. ㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T12:05:50.693Z"
  }
}', NULL, 'receive'), 
  ('17062', '9000000001', '2023-03-24 21:06:06.436000', '9000000001', '17059', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17063', '9000000001', '2023-03-24 21:06:14.713000', '9000000001', '17059', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17064', '9000000001', '2023-03-24 21:06:14.724000', '9000000001', '17059', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17066', '3', '2023-03-24 21:06:14.754000', '3', '17059', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잠시만욧."
    } ]
  } ]
}', NULL, 'send'), 
  ('17067', '3', '2023-03-24 21:06:21.309000', '16091', '17059', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "기다렸어요,"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T12:06:21.262Z"
  }
}', NULL, 'receive'), 
  ('17071', '9000000001', '2023-03-24 21:07:31.086000', '9000000001', '17068', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17072', '9000000001', '2023-03-24 21:08:23.483000', '9000000001', '17068', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17073', '9000000001', '2023-03-24 21:08:23.493000', '9000000001', '17068', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17075', '5', '2023-03-24 21:08:23.525000', '5', '17068', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아이고"
    } ]
  } ]
}', NULL, 'send'), 
  ('17076', '5', '2023-03-24 21:08:31.042000', '5', '17068', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "죄송허요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17077', '5', '2023-03-24 21:08:52.267000', '16091', '17068', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아녀유."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T12:08:52.2Z"
  }
}', NULL, 'receive'), 
  ('17083', '9000000001', '2023-03-24 21:19:51.187000', '9000000001', '17080', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17084', '9000000001', '2023-03-24 21:20:01.596000', '9000000001', '17080', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17085', '9000000001', '2023-03-24 21:20:01.606000', '9000000001', '17080', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17087', '5', '2023-03-24 21:20:01.639000', '5', '17080', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄷㄷㄷㄱ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17089', '5', '2023-03-24 21:21:08.686000', '16091', '17080', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그러네요. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T12:21:08.611Z"
  }
}', NULL, 'receive'), 
  ('17094', '9000000001', '2023-03-24 21:35:23.136000', '9000000001', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17095', '9000000001', '2023-03-24 21:35:36.978000', '9000000001', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17096', '9000000001', '2023-03-24 21:35:36.991000', '9000000001', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17098', '5', '2023-03-24 21:35:37.049000', '5', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "장이야"
    } ]
  } ]
}', NULL, 'send'), 
  ('17099', '5', '2023-03-24 21:35:43.060000', '16091', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "멍이구나"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T12:35:42.963Z"
  }
}', NULL, 'receive'), 
  ('17100', '5', '2023-03-24 21:35:44.860000', '16091', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아닌가"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-24T12:35:44.795Z"
  }
}', NULL, 'receive'), 
  ('17101', '5', '2023-03-24 21:35:50.711000', '5', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅈ잘 사니?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17102', '9000000001', '2023-03-24 21:37:53.830000', '9000000001', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17106', '9000000001', '2023-03-24 21:42:54.342000', '9000000001', '17091', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17178', '9000000001', '2023-03-25 09:16:27.772000', '9000000001', '17176', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17182', '9000000001', '2023-03-25 09:18:00.749000', '9000000001', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17183', '9000000001', '2023-03-25 09:18:41.239000', '9000000001', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17184', '9000000001', '2023-03-25 09:18:41.253000', '9000000001', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17186', '5', '2023-03-25 09:18:41.294000', '5', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "eeee"
    } ]
  } ]
}', NULL, 'send'), 
  ('17187', '5', '2023-03-25 09:18:48.647000', '16091', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅅㅅㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-25T00:18:48.545Z"
  }
}', NULL, 'receive'), 
  ('17188', '5', '2023-03-25 09:18:52.347000', '5', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "yyyy"
    } ]
  } ]
}', NULL, 'send'), 
  ('17189', '9000000001', '2023-03-25 09:21:20.281000', '9000000001', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17191', '5', '2023-03-25 09:21:28.019000', '16091', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄹㄹ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-25T00:21:27.957Z"
  }
}', NULL, 'receive'), 
  ('17192', '5', '2023-03-25 09:21:30.455000', '16091', '17179', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅏㅏㅏ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-25T00:21:30.377Z"
  }
}', NULL, 'receive'), 
  ('17196', '9000000001', '2023-03-25 09:34:36.752000', '9000000001', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17197', '9000000001', '2023-03-25 09:35:21.024000', '9000000001', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17198', '9000000001', '2023-03-25 09:35:21.035000', '9000000001', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17200', '4', '2023-03-25 09:35:21.087000', '4', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그럼 위험한데"
    } ]
  } ]
}', NULL, 'send'), 
  ('17201', '4', '2023-03-25 09:35:25.405000', '4', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "뭔가가"
    } ]
  } ]
}', NULL, 'send'), 
  ('17202', '4', '2023-03-25 09:35:30.415000', '16091', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그렇군요."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-25T00:35:30.303Z"
  }
}', NULL, 'receive'), 
  ('17204', '4', '2023-03-25 09:40:44.969000', '16091', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "!ㄹㅇㄹㅇㅁㄹㅇㅇㄹㄴㄹㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-25T00:40:44.893Z"
  }
}', NULL, 'receive'), 
  ('17205', '4', '2023-03-25 09:40:46.953000', '16091', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㄹㅇㄹㅇㅁㅁㅇㄴ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-25T00:40:46.88Z"
  }
}', NULL, 'receive'), 
  ('17206', '4', '2023-03-25 09:40:52.542000', '4', '17193', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㄹㅇㅁㅇㄴㄹㄴㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17211', '9000000001', '2023-03-27 14:35:55.532000', '9000000001', '17208', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17212', '4', '2023-03-27 14:35:59.743000', '16926', '17208', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-27T05:35:59.674Z"
  }
}', NULL, 'receive'), 
  ('17213', '9000000001', '2023-03-27 14:35:59.763000', '9000000001', '17208', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17214', '9000000001', '2023-03-27 14:36:14.831000', '9000000001', '17208', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17215', '9000000001', '2023-03-27 14:36:14.839000', '9000000001', '17208', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17217', '4', '2023-03-27 14:36:14.865000', '4', '17208', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17218', '9000000001', '2023-03-27 14:38:26.371000', '9000000001', '17208', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17224', '9000000001', '2023-03-27 19:17:18.043000', '9000000001', '17221', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17225', '9000000001', '2023-03-27 19:24:45.359000', '9000000001', '17221', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17227', '9000000001', '2023-03-27 20:09:43.982000', '9000000001', '17221', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17228', '9000000001', '2023-03-27 20:09:43.991000', '9000000001', '17221', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17230', '4', '2023-03-27 20:09:44.055000', '4', '17221', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17231', '9000000001', '2023-03-27 20:11:46.401000', '9000000001', '17221', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17234', '9000000001', '2023-03-27 20:16:46.651000', '9000000001', '17221', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17239', '9000000001', '2023-03-28 19:17:23.572000', '9000000001', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17240', '9000000001', '2023-03-28 19:24:52.442000', '9000000001', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17242', '9000000001', '2023-03-28 19:55:53.296000', '9000000001', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17243', '9000000001', '2023-03-28 19:55:53.304000', '9000000001', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17245', '4', '2023-03-28 19:55:53.334000', '4', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "다시 테스트 "
    } ]
  } ]
}', NULL, 'send'), 
  ('17246', '9000000001', '2023-03-28 19:58:21.945000', '9000000001', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17248', '4', '2023-03-28 19:59:36', '16926', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하새요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-28T10:59:35.844Z"
  }
}', NULL, 'receive'), 
  ('17250', '4', '2023-03-28 20:01:49.452000', '4', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "djshasdf,"
    } ]
  } ]
}', NULL, 'send'), 
  ('17252', '9000000001', '2023-03-28 20:03:22.130000', '9000000001', '17236', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17274', '9000000001', '2023-03-29 12:45:03.286000', '9000000001', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17281', '5', '2023-03-29 12:45:59.956000', '16091', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이상하구만"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T03:45:59.858Z"
  }
}', NULL, 'receive'), 
  ('17282', '9000000001', '2023-03-29 12:45:59.983000', '9000000001', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17295', '5', '2023-03-29 12:47:17.480000', '16091', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "여"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T03:47:17.362Z"
  }
}', NULL, 'receive'), 
  ('17296', '9000000001', '2023-03-29 12:47:17.503000', '9000000001', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17297', '5', '2023-03-29 12:47:25.304000', '16091', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T03:47:25.238Z"
  }
}', NULL, 'receive'), 
  ('17298', '9000000001', '2023-03-29 12:47:25.326000', '9000000001', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17307', '5', '2023-03-29 12:53:14.902000', '16091', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T03:53:14.789Z"
  }
}', NULL, 'receive'), 
  ('17308', '9000000001', '2023-03-29 12:53:14.935000', '9000000001', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17309', '9000000001', '2023-03-29 13:00:18.687000', '9000000001', '17271', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17317', '9000000001', '2023-03-29 13:39:35.551000', '9000000001', '17314', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17330', '9000000001', '2023-03-29 13:40:53.209000', '9000000001', '17327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17335', '9000000001', '2023-03-29 13:45:49.213000', '9000000001', '17327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17336', '9000000001', '2023-03-29 13:45:49.256000', '9000000001', '17327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17337', '4', '2023-03-29 13:45:49.405000', '4', '17327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "죄송해요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17338', '4', '2023-03-29 13:46:04.303000', '4', '17327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "가야합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('17342', '9000000001', '2023-03-29 13:46:51.173000', '9000000001', '17339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17343', '4', '2023-03-29 13:47:40.365000', '4', '17327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "뭔일이래"
    } ]
  } ]
}', NULL, 'send'), 
  ('17344', '4', '2023-03-29 13:47:42.270000', '16926', '17327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T04:47:42.216Z"
  }
}', NULL, 'receive'), 
  ('17345', '4', '2023-03-29 13:47:50.356000', '4', '17327', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17350', '9000000001', '2023-03-29 13:48:07.730000', '9000000001', '17347', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17351', '4', '2023-03-29 13:48:13.197000', '16926', '17347', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T04:48:13.137Z"
  }
}', NULL, 'receive'), 
  ('17352', '9000000001', '2023-03-29 13:48:13.220000', '9000000001', '17347', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17353', '9000000001', '2023-03-29 13:48:19.088000', '9000000001', '17339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17354', '9000000001', '2023-03-29 13:48:19.098000', '9000000001', '17339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17355', '3', '2023-03-29 13:48:19.141000', '3', '17339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "장시간"
    } ]
  } ]
}', NULL, 'send'), 
  ('17356', '9000000001', '2023-03-29 13:48:20.583000', '9000000001', '17347', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17357', '9000000001', '2023-03-29 13:48:20.593000', '9000000001', '17347', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17358', '4', '2023-03-29 13:48:20.619000', '4', '17347', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17359', '3', '2023-03-29 13:48:27.320000', '16091', '17339', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "죄송합니다."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T04:48:27.264Z"
  }
}', NULL, 'receive'), 
  ('17361', '9000000001', '2023-03-29 13:50:32.903000', '9000000001', '17347', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17363', '9000000001', '2023-03-29 13:51:42.873000', '9000000001', '17347', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('17369', '9000000001', '2023-03-29 13:52:03.630000', '9000000001', '17367', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17373', '9000000001', '2023-03-29 13:53:50.348000', '9000000001', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17374', '9000000001', '2023-03-29 13:54:04.549000', '9000000001', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17375', '9000000001', '2023-03-29 13:54:04.559000', '9000000001', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17376', '3', '2023-03-29 13:54:04.588000', '3', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('17380', '9000000001', '2023-03-29 13:54:37.174000', '9000000001', '17378', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17381', '9000000001', '2023-03-29 13:56:33.249000', '9000000001', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17383', '3', '2023-03-29 13:57:20.526000', '16091', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "없어요."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T04:57:20.453Z"
  }
}', NULL, 'receive'), 
  ('17388', '9000000001', '2023-03-29 14:01:49.024000', '16926', '17386', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T05:01:48.955Z"
  }
}', NULL, 'receive'), 
  ('17389', '9000000001', '2023-03-29 14:01:49.046000', '9000000001', '17386', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17393', '9000000001', '2023-03-29 14:02:12.284000', '9000000001', '17391', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17399', '9000000001', '2023-03-29 14:02:30.683000', '9000000001', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17403', '9000000001', '2023-03-29 14:02:35.684000', '16926', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T05:02:35.643Z"
  }
}', NULL, 'receive'), 
  ('17404', '9000000001', '2023-03-29 14:02:35.711000', '9000000001', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17405', '4', '2023-03-29 14:02:44.432000', '17394', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "방가방가"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T05:02:44.387Z"
  }
}', NULL, 'receive'), 
  ('17406', '9000000001', '2023-03-29 14:02:44.464000', '9000000001', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17407', '9000000001', '2023-03-29 14:02:49.728000', '9000000001', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17408', '9000000001', '2023-03-29 14:02:49.738000', '9000000001', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17410', '4', '2023-03-29 14:02:49.766000', '4', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17411', '9000000001', '2023-03-29 14:02:50.314000', '9000000001', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17412', '4', '2023-03-29 14:02:54.987000', '17394', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "빅터 안녕하십니까"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T05:02:54.86Z"
  }
}', NULL, 'receive'), 
  ('17413', '4', '2023-03-29 14:02:59.182000', '4', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17414', '4', '2023-03-29 14:03:05.062000', '4', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어드민 청년"
    } ]
  } ]
}', NULL, 'send'), 
  ('17415', '4', '2023-03-29 14:03:32.759000', '4', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    }, {
      "type" : "action"
    } ]
  } ]
}', NULL, 'send'), 
  ('17419', '9000000001', '2023-03-29 14:04:24.944000', '9000000001', '17416', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17420', '9000000001', '2023-03-29 14:05:33.678000', '9000000001', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17422', '9000000001', '2023-03-29 14:10:03.774000', '9000000001', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17425', '9000000001', '2023-03-29 14:10:33.978000', '9000000001', '17396', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17426', '9000000001', '2023-03-29 14:11:33.947000', '9000000001', '17416', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17428', '3', '2023-03-29 14:19:59.486000', '15659', '17416', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T05:19:59.389Z"
  }
}', NULL, 'receive'), 
  ('17429', '9000000001', '2023-03-29 14:19:59.507000', '9000000001', '17416', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17430', '9000000001', '2023-03-29 14:21:12.100000', '9000000001', '17416', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17431', '9000000001', '2023-03-29 14:48:31.243000', '9000000001', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17432', '9000000001', '2023-03-29 14:48:31.253000', '9000000001', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17433', '4', '2023-03-29 14:48:31.283000', '4', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "바나나"
    } ]
  } ]
}', NULL, 'send'), 
  ('17434', '4', '2023-03-29 14:49:36.831000', '4', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "나쁜뇨속"
    } ]
  } ]
}', NULL, 'send'), 
  ('17435', '4', '2023-03-29 14:49:49.918000', '4', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어 테스트 중입니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('17436', '9000000001', '2023-03-29 14:52:05.408000', '9000000001', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17438', '4', '2023-03-29 14:52:06.992000', '4', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "바나나"
    } ]
  } ]
}', NULL, 'send'), 
  ('17439', '4', '2023-03-29 14:52:51.589000', '4', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어 잘 되네요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17441', '9000000001', '2023-03-29 14:57:05.693000', '9000000001', '17401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17442', '3', '2023-03-29 15:31:31.083000', '3', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1번"
    } ]
  } ]
}', NULL, 'send'), 
  ('17444', '3', '2023-03-29 15:31:35.268000', '3', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "2번"
    } ]
  } ]
}', NULL, 'send'), 
  ('17447', '9000000001', '2023-03-29 15:31:36.717000', '9000000001', '17370', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17462', '9000000001', '2023-03-29 15:45:18.646000', '9000000001', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17463', '9000000001', '2023-03-29 15:45:43.083000', '9000000001', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17464', '9000000001', '2023-03-29 15:45:43.091000', '9000000001', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17465', '5', '2023-03-29 15:45:43.120000', '5', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허어"
    } ]
  } ]
}', NULL, 'send'), 
  ('17466', '5', '2023-03-29 15:45:58.116000', '5', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "헬로우 헬로우"
    } ]
  } ]
}', NULL, 'send'), 
  ('17467', '5', '2023-03-29 15:46:58.353000', '5', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17468', '5', '2023-03-29 15:47:38.684000', '5', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZP7fNelVJ/zY3K0mLP8SweIuTYKd2Y30/i_qsh7av.jpg",
      "display" : "image/jpeg",
      "name" : "KakaoTalk_20230308_111305561.jpg",
      "size" : 429136
    } ]
  } ]
}', NULL, 'send'), 
  ('17469', '9000000001', '2023-03-29 15:50:07.401000', '9000000001', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17471', '5', '2023-03-29 15:50:16.895000', '15659', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎㅎㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T06:50:16.819Z"
  }
}', NULL, 'receive'), 
  ('17472', '9000000001', '2023-03-29 15:50:49.920000', '9000000001', '17459', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17476', '9000000001', '2023-03-29 16:00:01.376000', '9000000001', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17477', '9000000001', '2023-03-29 16:00:23.963000', '9000000001', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17478', '9000000001', '2023-03-29 16:00:23.972000', '9000000001', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17479', '5', '2023-03-29 16:00:23.999000', '5', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17480', '5', '2023-03-29 16:00:49.087000', '15659', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZP4Ac031H/2McdqC1eTdlgrAfjlJOrd0/i_518073114cbc.jpg",
      "display" : "image"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T07:00:49.019Z"
  }
}', NULL, 'receive'), 
  ('17485', '9000000001', '2023-03-29 16:20:33.597000', '9000000001', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17486', '16862', '2023-03-29 16:22:42.232000', '15606', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "코디"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T07:22:42.156Z"
  }
}', NULL, 'receive'), 
  ('17489', '16862', '2023-03-29 16:22:58.273000', '15606', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "코디입니다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T07:22:58.21Z"
  }
}', NULL, 'receive'), 
  ('17491', '16862', '2023-03-29 16:23:46.607000', '15606', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T07:23:46.547Z"
  }
}', NULL, 'receive'), 
  ('17495', '16862', '2023-03-29 16:25:18.949000', '15606', '16882', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오잉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T07:25:18.874Z"
  }
}', NULL, 'receive'), 
  ('17499', '9000000001', '2023-03-29 16:25:27.140000', '9000000001', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17500', '9000000001', '2023-03-29 16:25:27.149000', '15606', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T07:25:27.101Z"
  }
}', NULL, 'receive'), 
  ('17501', '9000000001', '2023-03-29 16:25:27.182000', '9000000001', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17502', '3', '2023-03-29 16:26:01.868000', '15606', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-29T07:26:01.823Z"
  }
}', NULL, 'receive'), 
  ('17503', '9000000001', '2023-03-29 16:26:01.889000', '9000000001', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17505', '9000000001', '2023-03-29 16:27:39.092000', '9000000001', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17507', '9000000001', '2023-03-29 16:33:09.464000', '9000000001', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17540', '9000000001', '2023-03-30 09:38:52.226000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17541', '5', '2023-03-30 09:38:52.549000', '16835', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T00:38:52.466Z"
  }
}', NULL, 'receive'), 
  ('17542', '9000000001', '2023-03-30 09:38:52.566000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17543', '5', '2023-03-30 09:38:56.774000', '16835', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T00:38:56.74Z"
  }
}', NULL, 'receive'), 
  ('17544', '9000000001', '2023-03-30 09:38:56.790000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17545', '5', '2023-03-30 09:39:20.639000', '16835', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T00:39:20.605Z"
  }
}', NULL, 'receive'), 
  ('17546', '9000000001', '2023-03-30 09:39:20.655000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17547', '9000000001', '2023-03-30 09:39:37.489000', '9000000001', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17548', '9000000001', '2023-03-30 09:39:37.496000', '9000000001', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17549', '4', '2023-03-30 09:39:37.519000', '4', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17550', '4', '2023-03-30 09:39:39.408000', '4', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17551', '4', '2023-03-30 09:39:42.019000', '4', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17552', '9000000001', '2023-03-30 09:42:04.724000', '9000000001', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17554', '9000000001', '2023-03-30 09:46:33.188000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17557', '9000000001', '2023-03-30 09:47:04.956000', '9000000001', '17482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17561', '9000000001', '2023-03-30 10:01:26.347000', '9000000001', '17558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17562', '9000000001', '2023-03-30 10:03:49.622000', '9000000001', '17558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17563', '9000000001', '2023-03-30 10:03:49.630000', '9000000001', '17558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17564', '4', '2023-03-30 10:03:49.654000', '4', '17558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잘 되네요."
    } ]
  } ]
}', NULL, 'send'), 
  ('17565', '9000000001', '2023-03-30 10:04:44.351000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17566', '9000000001', '2023-03-30 10:04:44.360000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/fee91c70-2316-4daf-a381-e4e807c68b9f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17567', '5', '2023-03-30 10:04:44.386000', '5', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "누구세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17568', '5', '2023-03-30 10:04:49.243000', '5', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "누구세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17569', '5', '2023-03-30 10:04:58.394000', '5', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "누구세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17570', '5', '2023-03-30 10:05:10.361000', '5', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "누구세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17571', '5', '2023-03-30 10:05:16.531000', '5', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "누구세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17572', '5', '2023-03-30 10:05:44.009000', '5', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어 누구세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17573', '9000000001', '2023-03-30 10:06:05.488000', '9000000001', '17558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17575', '9000000001', '2023-03-30 10:07:35.615000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17577', '9000000001', '2023-03-30 10:08:05.680000', '9000000001', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17581', '5', '2023-03-30 10:10:55.669000', '15659', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T01:10:54.576Z"
  }
}', NULL, 'receive'), 
  ('17583', '9000000001', '2023-03-30 10:11:05.901000', '9000000001', '17558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17586', '9000000001', '2023-03-30 10:12:36.004000', '9000000001', '17537', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17590', '9000000001', '2023-03-30 10:21:34.046000', '9000000001', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17591', '5', '2023-03-30 10:21:34.864000', '16835', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "겨울이예요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T01:21:34.834Z"
  }
}', NULL, 'receive'), 
  ('17592', '9000000001', '2023-03-30 10:21:34.883000', '9000000001', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17593', '5', '2023-03-30 10:21:59.427000', '16835', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담원1 로그인했을때 요 대화내용이 안보여요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T01:21:59.383Z"
  }
}', NULL, 'receive'), 
  ('17594', '9000000001', '2023-03-30 10:21:59.444000', '9000000001', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17595', '9000000001', '2023-03-30 10:29:05.286000', '9000000001', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17597', '9000000001', '2023-03-30 10:29:11.231000', '9000000001', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까? 상담사 S입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/6bc55b6c-588a-4610-9284-df3cf464d242.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17598', '9000000001', '2023-03-30 10:29:11.240000', '9000000001', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까? 상담사 S입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/6bc55b6c-588a-4610-9284-df3cf464d242.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17599', '5', '2023-03-30 10:29:11.263000', '5', '17587', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17603', '9000000001', '2023-03-30 10:29:59.096000', '9000000001', '17601', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17607', '9000000001', '2023-03-30 10:30:15.447000', '9000000001', '17605', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17612', '9000000001', '2023-03-30 10:30:23.861000', '9000000001', '17609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17617', '9000000001', '2023-03-30 10:31:00.927000', '9000000001', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17618', '4', '2023-03-30 10:31:08.107000', '16835', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "oihjlkjk;lj"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T01:31:08.061Z"
  }
}', NULL, 'receive'), 
  ('17619', '9000000001', '2023-03-30 10:31:08.123000', '9000000001', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17620', '9000000001', '2023-03-30 10:31:16.930000', '9000000001', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17621', '9000000001', '2023-03-30 10:31:16.938000', '9000000001', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17622', '4', '2023-03-30 10:31:16.959000', '4', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "lihjj"
    } ]
  } ]
}', NULL, 'send'), 
  ('17623', '4', '2023-03-30 10:32:36.754000', '16835', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이잉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T01:32:36.716Z"
  }
}', NULL, 'receive'), 
  ('17624', '4', '2023-03-30 10:32:58.146000', '17394', '17609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "딜런입니다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T01:32:58.107Z"
  }
}', NULL, 'receive'), 
  ('17625', '9000000001', '2023-03-30 10:32:58.163000', '9000000001', '17609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17626', '9000000001', '2023-03-30 10:33:29.187000', '9000000001', '17609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17627', '9000000001', '2023-03-30 10:33:29.194000', '9000000001', '17609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17628', '4', '2023-03-30 10:33:29.217000', '4', '17609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이 딜런"
    } ]
  } ]
}', NULL, 'send'), 
  ('17630', '9000000001', '2023-03-30 10:35:36.654000', '9000000001', '17609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17633', '9000000001', '2023-03-30 10:40:36.906000', '9000000001', '17609', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17638', '9000000001', '2023-03-30 10:47:43.811000', '17634', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅇㅎㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T01:47:43.791Z"
  }
}', NULL, 'receive'), 
  ('17639', '9000000001', '2023-03-30 10:47:43.827000', '9000000001', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17641', '9000000001', '2023-03-30 10:48:06.634000', '9000000001', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17642', '9000000001', '2023-03-30 10:48:16.119000', '9000000001', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17643', '9000000001', '2023-03-30 10:48:16.126000', '9000000001', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17644', '4', '2023-03-30 10:48:16.148000', '4', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "gdgd"
    } ]
  } ]
}', NULL, 'send'), 
  ('17645', '4', '2023-03-30 10:48:19.967000', '4', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅇㅎㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17646', '4', '2023-03-30 10:48:28.083000', '4', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZQdBJqNK9/mwKDYemoEWwE2mSLj46eTk/i_ch4n4n.jpg",
      "display" : "image/jpeg",
      "name" : "mkkokokick.jpg",
      "size" : 20464
    } ]
  } ]
}', NULL, 'send'), 
  ('17647', '4', '2023-03-30 10:49:00.761000', '4', '17636', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://mud-kage.kakao.com/dn/dTQ4Xl/o42d2Nwish/3G7EqNvDwiHzxGo715Mrfk/f_ggxek4.zip",
      "display" : "application/x-zip-compressed",
      "name" : "i13880609910.zip",
      "size" : 67773
    } ]
  } ]
}', NULL, 'send'), 
  ('17648', '4', '2023-03-30 10:56:47.446000', '4', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZQdR6zoSs/tWrSoKP7exh5WKKcy9g3zK/i_elz01g.png",
      "display" : "image/png",
      "name" : "images.png",
      "size" : 4755
    } ]
  } ]
}', NULL, 'send'), 
  ('17649', '4', '2023-03-30 10:57:11.433000', '16835', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZQfpPBYtM/g49YKJBzOkXPorfXi3bbl0/i_c2f86a2b54f5.png",
      "display" : "image"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T01:57:11.337Z"
  }
}', NULL, 'receive'), 
  ('17691', '4', '2023-03-30 13:17:07.612000', '4', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "asd"
    } ]
  } ]
}', NULL, 'send'), 
  ('17694', '9000000001', '2023-03-30 13:19:10.948000', '9000000001', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17696', '4', '2023-03-30 13:20:44.781000', '4', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "asd"
    } ]
  } ]
}', NULL, 'send'), 
  ('17701', '9000000001', '2023-03-30 13:24:11.181000', '9000000001', '17614', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17747', '9000000001', '2023-03-30 14:53:50.762000', '16926', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T05:53:50.674Z"
  }
}', NULL, 'receive'), 
  ('17748', '9000000001', '2023-03-30 14:53:50.781000', '9000000001', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17750', '9000000001', '2023-03-30 14:54:09.118000', '9000000001', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17751', '9000000001', '2023-03-30 14:54:24.386000', '9000000001', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17752', '9000000001', '2023-03-30 14:54:24.394000', '9000000001', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17753', '4', '2023-03-30 14:54:24.419000', '4', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17755', '4', '2023-03-30 14:54:54.837000', '16926', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담가능시간 변경 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T05:54:54.791Z"
  }
}', NULL, 'receive'), 
  ('17760', '9000000001', '2023-03-30 14:56:20.917000', '9000000001', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17761', '9000000001', '2023-03-30 14:57:42.775000', '9000000001', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까? 상담사 S입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/6bc55b6c-588a-4610-9284-df3cf464d242.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17762', '9000000001', '2023-03-30 14:57:42.783000', '9000000001', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까? 상담사 S입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/03/6bc55b6c-588a-4610-9284-df3cf464d242.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('17763', '5', '2023-03-30 14:57:42.807000', '5', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어이어"
    } ]
  } ]
}', NULL, 'send'), 
  ('17764', '5', '2023-03-30 14:58:15.240000', '16091', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "gkdk"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T05:58:15.195Z"
  }
}', NULL, 'receive'), 
  ('17765', '5', '2023-03-30 14:58:18.994000', '5', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어이"
    } ]
  } ]
}', NULL, 'send'), 
  ('17766', '5', '2023-03-30 14:58:57.726000', '16091', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T05:58:57.67Z"
  }
}', NULL, 'receive'), 
  ('17767', '5', '2023-03-30 14:59:59.558000', '16091', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T05:59:59.49Z"
  }
}', NULL, 'receive'), 
  ('17769', '5', '2023-03-30 15:00:30.323000', '16091', '17757', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T06:00:30.277Z"
  }
}', NULL, 'receive'), 
  ('17773', '9000000001', '2023-03-30 15:00:50.477000', '9000000001', '17770', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17776', '9000000001', '2023-03-30 15:01:01.470000', '9000000001', '17774', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17779', '9000000001', '2023-03-30 15:01:14.043000', '16091', '17777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-30T06:01:14.019Z"
  }
}', NULL, 'receive'), 
  ('17780', '9000000001', '2023-03-30 15:01:14.060000', '9000000001', '17777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17781', '9000000001', '2023-03-30 15:01:22.274000', '9000000001', '17777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17786', '9000000001', '2023-03-30 15:02:55.320000', '9000000001', '17784', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17789', '9000000001', '2023-03-30 15:03:16.663000', '9000000001', '17787', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17792', '9000000001', '2023-03-30 15:05:06.411000', '9000000001', '17790', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17796', '9000000001', '2023-03-30 15:06:38.294000', '9000000001', '17794', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('17832', '5', '2023-03-31 09:23:55.061000', '5', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하하"
    } ]
  } ]
}', NULL, 'send'), 
  ('17834', '9000000001', '2023-03-31 09:24:11.592000', '9000000001', '17473', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17838', '9000000001', '2023-03-31 09:24:23.163000', '9000000001', '17835', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17839', '4', '2023-03-31 09:24:57.040000', '15659', '17835', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㄴㅁ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T00:24:57.009Z"
  }
}', NULL, 'receive'), 
  ('17840', '9000000001', '2023-03-31 09:24:57.056000', '9000000001', '17835', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17844', '9000000001', '2023-03-31 09:25:17.400000', '9000000001', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17845', '5', '2023-03-31 09:25:37.035000', '15659', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하하하"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T00:25:37.005Z"
  }
}', NULL, 'receive'), 
  ('17846', '9000000001', '2023-03-31 09:25:37.049000', '9000000001', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17847', '5', '2023-03-31 09:25:38.554000', '15659', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "낭ㄻ미;ㄹㄴㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T00:25:38.476Z"
  }
}', NULL, 'receive'), 
  ('17848', '9000000001', '2023-03-31 09:25:38.570000', '9000000001', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17850', '9000000001', '2023-03-31 09:26:34.676000', '9000000001', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17851', '9000000001', '2023-03-31 09:26:57.808000', '9000000001', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17852', '9000000001', '2023-03-31 09:26:57.816000', '9000000001', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17853', '4', '2023-03-31 09:26:57.840000', '4', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17854', '4', '2023-03-31 09:27:02.897000', '4', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "바나나"
    } ]
  } ]
}', NULL, 'send'), 
  ('17855', '4', '2023-03-31 09:27:07.873000', '15659', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "바나나"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T00:27:07.824Z"
  }
}', NULL, 'receive'), 
  ('17856', '4', '2023-03-31 09:27:48.849000', '15659', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "볼펜"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T00:27:48.81Z"
  }
}', NULL, 'receive'), 
  ('17863', '4', '2023-03-31 11:21:06.374000', '4', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "볼펜"
    } ]
  } ]
}', NULL, 'send'), 
  ('17867', '4', '2023-03-31 11:22:35.969000', '4', '17841', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "가나다"
    } ]
  } ]
}', NULL, 'send'), 
  ('17877', '9000000001', '2023-03-31 11:24:40.532000', '9000000001', '17874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17878', '9000000001', '2023-03-31 11:31:59.554000', '9000000001', '17874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17880', '4', '2023-03-31 12:21:56.959000', '4', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아"
    } ]
  } ]
}', NULL, 'send'), 
  ('17881', '4', '2023-03-31 12:22:24.296000', '4', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17882', '4', '2023-03-31 12:22:33.427000', '16926', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T03:22:32.231Z"
  }
}', NULL, 'receive'), 
  ('17888', '9000000001', '2023-03-31 12:23:29.147000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17892', '9000000001', '2023-03-31 12:24:48.537000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17894', '9000000001', '2023-03-31 12:24:55.954000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17901', '4', '2023-03-31 12:30:25.174000', '15659', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T03:30:25.088Z"
  }
}', NULL, 'receive'), 
  ('17902', '9000000001', '2023-03-31 12:30:25.209000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17907', '4', '2023-03-31 12:32:40.901000', '15659', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T03:32:40.731Z"
  }
}', NULL, 'receive'), 
  ('17908', '9000000001', '2023-03-31 12:32:40.943000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17909', '4', '2023-03-31 12:32:57.943000', '4', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "채팅테스트입니다.."
    } ]
  } ]
}', NULL, 'send'), 
  ('17910', '4', '2023-03-31 12:33:37.728000', '15659', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어2"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T03:33:37.648Z"
  }
}', NULL, 'receive'), 
  ('17911', '9000000001', '2023-03-31 12:33:37.771000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17912', '4', '2023-03-31 12:33:43.935000', '15659', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T03:33:43.886Z"
  }
}', NULL, 'receive'), 
  ('17913', '9000000001', '2023-03-31 12:33:43.970000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17914', '4', '2023-03-31 12:33:54.256000', '15659', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1은 금지어입니다."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T03:33:54.212Z"
  }
}', NULL, 'receive'), 
  ('17915', '9000000001', '2023-03-31 12:33:54.299000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('17916', '9000000001', '2023-03-31 12:35:13.850000', '9000000001', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17921', '9000000001', '2023-03-31 12:40:14.313000', '9000000001', '17745', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17922', '9000000001', '2023-03-31 12:41:14.391000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('17924', '9000000001', '2023-03-31 12:47:40.820000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17925', '9000000001', '2023-03-31 12:47:40.861000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17926', '4', '2023-03-31 12:47:40.934000', '4', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('17927', '9000000001', '2023-03-31 12:50:00.648000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17930', '9000000001', '2023-03-31 12:55:01.032000', '9000000001', '17885', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17935', '9000000001', '2023-03-31 17:29:07.527000', '9000000001', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17936', '9000000001', '2023-03-31 17:29:35.949000', '9000000001', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17937', '9000000001', '2023-03-31 17:29:35.960000', '9000000001', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17938', '5', '2023-03-31 17:29:36.019000', '5', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17939', '5', '2023-03-31 17:29:40.910000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:29:40.797Z"
  }
}', NULL, 'receive'), 
  ('17940', '5', '2023-03-31 17:29:43.935000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어2"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:29:43.888Z"
  }
}', NULL, 'receive'), 
  ('17941', '5', '2023-03-31 17:30:34.975000', '5', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어 노노"
    } ]
  } ]
}', NULL, 'send'), 
  ('17942', '5', '2023-03-31 17:30:36.597000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1은 금지어대상입니다."
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:36.575Z"
  }
}', NULL, 'receive'), 
  ('17943', '5', '2023-03-31 17:30:41.692000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네넵 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:41.232Z"
  }
}', NULL, 'receive'), 
  ('17944', '5', '2023-03-31 17:30:45.368000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "볼펜"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:45.335Z"
  }
}', NULL, 'receive'), 
  ('17945', '5', '2023-03-31 17:30:46.868000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사탕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:46.848Z"
  }
}', NULL, 'receive'), 
  ('17946', '5', '2023-03-31 17:30:47.574000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "생각"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:47.545Z"
  }
}', NULL, 'receive'), 
  ('17947', '5', '2023-03-31 17:30:48.144000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안함"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:48.094Z"
  }
}', NULL, 'receive'), 
  ('17948', '5', '2023-03-31 17:30:48.580000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오호"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:48.558Z"
  }
}', NULL, 'receive'), 
  ('17949', '5', '2023-03-31 17:30:49.062000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "저장"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:49.025Z"
  }
}', NULL, 'receive'), 
  ('17950', '5', '2023-03-31 17:30:49.943000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "짜잔"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:49.919Z"
  }
}', NULL, 'receive'), 
  ('17951', '5', '2023-03-31 17:30:51.402000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "짜짜짜"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:51.365Z"
  }
}', NULL, 'receive'), 
  ('17952', '5', '2023-03-31 17:30:53.092000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "코대원"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:53.062Z"
  }
}', NULL, 'receive'), 
  ('17953', '5', '2023-03-31 17:30:53.616000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "크림"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:30:53.592Z"
  }
}', NULL, 'receive'), 
  ('17954', '5', '2023-03-31 17:33:51.176000', '15659', '17932', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-03-31T08:33:51.118Z"
  }
}', NULL, 'receive'), 
  ('17961', '9000000001', '2023-04-03 09:51:12.421000', '9000000001', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17962', '9000000001', '2023-04-03 09:52:41.884000', '9000000001', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 매니저1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17963', '9000000001', '2023-04-03 09:52:41.895000', '9000000001', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 매니저1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17964', '3', '2023-04-03 09:52:41.943000', '3', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "바나나"
    } ]
  } ]
}', NULL, 'send'), 
  ('17965', '9000000001', '2023-04-03 09:52:44.796000', '9000000001', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17966', '9000000001', '2023-04-03 09:52:44.805000', '9000000001', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17967', '5', '2023-04-03 09:52:44.829000', '5', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17968', '5', '2023-04-03 09:53:32.685000', '15659', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T00:53:32.645Z"
  }
}', NULL, 'receive'), 
  ('17969', '9000000001', '2023-04-03 09:54:49.527000', '9000000001', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17972', '9000000001', '2023-04-03 09:59:49.827000', '9000000001', '17496', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17979', '9000000001', '2023-04-03 14:07:06.336000', '9000000001', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17980', '9000000001', '2023-04-03 14:07:38.991000', '9000000001', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17981', '9000000001', '2023-04-03 14:07:39.001000', '9000000001', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17982', '4', '2023-04-03 14:07:39.054000', '4', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('17983', '4', '2023-04-03 14:08:00.858000', '16926', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T05:08:00.782Z"
  }
}', NULL, 'receive'), 
  ('17984', '4', '2023-04-03 14:09:17.748000', '4', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17985', '9000000001', '2023-04-03 14:11:39.475000', '9000000001', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('17987', '4', '2023-04-03 14:13:11.894000', '16926', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㄹㅁㄴㄹ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T05:13:11.821Z"
  }
}', NULL, 'receive'), 
  ('17989', '4', '2023-04-03 14:36:41.474000', '4', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅠㅡㅜㅠㅡㅜ"
    } ]
  } ]
}', NULL, 'send'), 
  ('17991', '9000000001', '2023-04-03 14:37:10.430000', '9000000001', '17976', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('17996', '9000000001', '2023-04-03 14:37:58.928000', '9000000001', '17993', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('17997', '9000000001', '2023-04-03 14:38:25.378000', '9000000001', '17993', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17998', '9000000001', '2023-04-03 14:38:25.386000', '9000000001', '17993', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('17999', '4', '2023-04-03 14:38:25.410000', '4', '17993', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18004', '9000000001', '2023-04-03 14:42:36.762000', '9000000001', '18001', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18005', '5', '2023-04-03 14:42:45.601000', '16091', '18001', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T05:42:45.556Z"
  }
}', NULL, 'receive'), 
  ('18006', '9000000001', '2023-04-03 14:42:45.619000', '9000000001', '18001', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18010', '9000000001', '2023-04-03 14:43:16.009000', '9000000001', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18011', '4', '2023-04-03 14:43:18.786000', '16926', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T05:43:18.772Z"
  }
}', NULL, 'receive'), 
  ('18012', '9000000001', '2023-04-03 14:43:18.804000', '9000000001', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18017', '9000000001', '2023-04-03 14:44:39.684000', '9000000001', '18014', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18018', '4', '2023-04-03 14:44:57.327000', '16091', '18014', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T05:44:57.289Z"
  }
}', NULL, 'receive'), 
  ('18019', '9000000001', '2023-04-03 14:44:57.349000', '9000000001', '18014', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18024', '9000000001', '2023-04-03 14:45:35.267000', '9000000001', '18021', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18025', '4', '2023-04-03 14:45:39.810000', '16091', '18021', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T05:45:39.781Z"
  }
}', NULL, 'receive'), 
  ('18026', '9000000001', '2023-04-03 14:45:39.836000', '9000000001', '18021', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18027', '4', '2023-04-03 14:45:59.751000', '16091', '18021', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕사헤요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T05:45:59.713Z"
  }
}', NULL, 'receive'), 
  ('18028', '9000000001', '2023-04-03 14:45:59.772000', '9000000001', '18021', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18032', '9000000001', '2023-04-03 14:46:51.151000', '9000000001', '18029', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18036', '9000000001', '2023-04-03 14:47:17.889000', '9000000001', '18033', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18040', '9000000001', '2023-04-03 14:50:00.372000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18041', '5', '2023-04-03 14:50:04.728000', '16091', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "흐흐흐"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T05:50:04.691Z"
  }
}', NULL, 'receive'), 
  ('18042', '9000000001', '2023-04-03 14:50:04.745000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18043', '9000000001', '2023-04-03 14:50:40.852000', '9000000001', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('18045', '9000000001', '2023-04-03 14:57:11.357000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('18048', '9000000001', '2023-04-03 15:48:28.419000', '9000000001', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18049', '9000000001', '2023-04-03 15:48:28.429000', '9000000001', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18050', '4', '2023-04-03 15:48:28.457000', '4', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "박민순인가요? ㅎ"
    } ]
  } ]
}', NULL, 'send'), 
  ('18051', '9000000001', '2023-04-03 15:53:43.033000', '9000000001', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18053', '4', '2023-04-03 15:55:15.801000', '16926', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오늘부터 박민순입니다 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T06:55:14.662Z"
  }
}', NULL, 'receive'), 
  ('18056', '4', '2023-04-03 16:19:37.754000', '4', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어허"
    } ]
  } ]
}', NULL, 'send'), 
  ('18058', '9000000001', '2023-04-03 16:19:43.768000', '9000000001', '18007', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18072', '9000000001', '2023-04-03 18:39:03.153000', '9000000001', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18073', '9000000001', '2023-04-03 18:39:21.956000', '9000000001', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18074', '9000000001', '2023-04-03 18:39:21.964000', '9000000001', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18075', '4', '2023-04-03 18:39:21.988000', '4', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18076', '4', '2023-04-03 18:39:45.411000', '16926', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:39:45.36Z"
  }
}', NULL, 'receive'), 
  ('18077', '4', '2023-04-03 18:39:50.344000', '16926', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대출받고 싶어요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:39:50.289Z"
  }
}', NULL, 'receive'), 
  ('18078', '4', '2023-04-03 18:40:17.239000', '4', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "신용대출을 위해서는 고객님에 대한 정보 확인이 필요합니다. [본인확인 바로가기]를 선택하시고 로그인해주세요. "
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "본인확인 바로가기 ",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18080', '4', '2023-04-03 18:40:31.328000', '4', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "본인확인이 완료되었습니다. [신청서]버튼을 선택하시고 대출 신청서를 작성해주세요. "
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "신청서",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18082', '4', '2023-04-03 18:40:33.936000', '4', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "신청정보를 입력합니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18084', '4', '2023-04-03 18:40:37.034000', '4', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "약관에 동의해주세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18086', '5', '2023-04-03 18:42:53.525000', '16091', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "신경"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:42:53.451Z"
  }
}', NULL, 'receive'), 
  ('18087', '9000000001', '2023-04-03 18:42:53.589000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18088', '5', '2023-04-03 18:42:58.935000', '16091', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "신경"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:42:58.861Z"
  }
}', NULL, 'receive'), 
  ('18089', '9000000001', '2023-04-03 18:42:58.969000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18090', '5', '2023-04-03 18:43:08.911000', '16091', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:43:08.871Z"
  }
}', NULL, 'receive'), 
  ('18091', '9000000001', '2023-04-03 18:43:08.941000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18092', '5', '2023-04-03 18:43:29.914000', '16091', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:43:29.857Z"
  }
}', NULL, 'receive'), 
  ('18093', '9000000001', '2023-04-03 18:43:29.951000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18094', '4', '2023-04-03 18:45:13.850000', '4', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어"
    } ]
  } ]
}', NULL, 'send'), 
  ('18095', '5', '2023-04-03 18:45:26.399000', '16091', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:45:26.335Z"
  }
}', NULL, 'receive'), 
  ('18096', '9000000001', '2023-04-03 18:45:26.431000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18097', '5', '2023-04-03 18:45:29.697000', '16091', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:45:29.634Z"
  }
}', NULL, 'receive'), 
  ('18098', '9000000001', '2023-04-03 18:45:29.726000', '9000000001', '18037', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18103', '9000000001', '2023-04-03 18:46:00.522000', '9000000001', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18104', '9000000001', '2023-04-03 18:46:23.774000', '9000000001', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18105', '9000000001', '2023-04-03 18:46:23.785000', '9000000001', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18106', '4', '2023-04-03 18:46:23.838000', '4', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아야기"
    } ]
  } ]
}', NULL, 'send'), 
  ('18107', '4', '2023-04-03 18:46:30.889000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "난"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:46:30.839Z"
  }
}', NULL, 'receive'), 
  ('18108', '4', '2023-04-03 18:46:32.832000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넌"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:46:32.796Z"
  }
}', NULL, 'receive'), 
  ('18109', '4', '2023-04-03 18:46:44.512000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "장"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:46:44.475Z"
  }
}', NULL, 'receive'), 
  ('18110', '9000000001', '2023-04-03 18:46:44.544000', '9000000001', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18111', '4', '2023-04-03 18:46:56.153000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "다시"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:46:56.098Z"
  }
}', NULL, 'receive'), 
  ('18112', '4', '2023-04-03 18:47:46.057000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "워이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:47:46.012Z"
  }
}', NULL, 'receive'), 
  ('18113', '9000000001', '2023-04-03 18:47:46.096000', '9000000001', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18114', '4', '2023-04-03 18:48:15.199000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "스도"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:48:15.138Z"
  }
}', NULL, 'receive'), 
  ('18116', '4', '2023-04-03 18:49:47.112000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어엉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:49:47.062Z"
  }
}', NULL, 'receive'), 
  ('18117', '9000000001', '2023-04-03 18:49:47.150000', '9000000001', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18118', '4', '2023-04-03 18:50:18.018000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어어"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:50:17.968Z"
  }
}', NULL, 'receive'), 
  ('18119', '9000000001', '2023-04-03 18:50:18.054000', '9000000001', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18120', '9000000001', '2023-04-03 18:50:21.590000', '9000000001', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18122', '4', '2023-04-03 18:50:30.043000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "자"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:50:29.985Z"
  }
}', NULL, 'receive'), 
  ('18123', '4', '2023-04-03 18:50:40.840000', '16091', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허어"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T09:50:40.813Z"
  }
}', NULL, 'receive'), 
  ('18124', '9000000001', '2023-04-03 18:50:40.872000', '9000000001', '18100', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18126', '9000000001', '2023-04-03 18:55:22.107000', '9000000001', '18069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18235', '9000000001', '2023-04-03 20:50:31.738000', '9000000001', '18233', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('18240', '9000000001', '2023-04-03 20:50:53.077000', '9000000001', '18237', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18241', '4', '2023-04-03 20:51:45.163000', '16926', '18237', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T11:51:45.116Z"
  }
}', NULL, 'receive'), 
  ('18242', '9000000001', '2023-04-03 20:51:45.181000', '9000000001', '18237', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18243', '9000000001', '2023-04-03 20:52:06.251000', '9000000001', '18237', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18244', '9000000001', '2023-04-03 20:52:06.259000', '9000000001', '18237', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원1 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18245', '4', '2023-04-03 20:52:06.284000', '4', '18237', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18256', '9000000001', '2023-04-03 20:56:47.052000', '9000000001', '18248', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('18267', '9000000001', '2023-04-03 20:57:49.100000', '9000000001', '18258', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18268', '9000000001', '2023-04-03 20:58:02.748000', '9000000001', '18258', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18269', '9000000001', '2023-04-03 20:58:02.763000', '9000000001', '18258', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18270', '5', '2023-04-03 20:58:02.792000', '5', '18258', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18271', '9000000001', '2023-04-03 21:03:26.034000', '9000000001', '18258', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18273', '5', '2023-04-03 21:03:38.624000', '5', '18258', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18278', '9000000001', '2023-04-03 21:03:58.993000', '9000000001', '18275', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18279', '5', '2023-04-03 21:04:04.070000', '16926', '18275', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-03T12:04:04.02Z"
  }
}', NULL, 'receive'), 
  ('18280', '9000000001', '2023-04-03 21:04:04.088000', '9000000001', '18275', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18281', '9000000001', '2023-04-03 21:04:18.096000', '9000000001', '18275', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 카카오를 대표하는 라이언입니다. 사랑스런 춘식이도 찾아주세요~ "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/00ac8fbc-2210-44e8-ac68-03c8d81d0ced.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18282', '9000000001', '2023-04-03 21:04:18.106000', '9000000001', '18275', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 카카오를 대표하는 라이언입니다. 사랑스런 춘식이도 찾아주세요~ "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/00ac8fbc-2210-44e8-ac68-03c8d81d0ced.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18283', '5', '2023-04-03 21:04:18.134000', '5', '18275', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18284', '9000000001', '2023-04-03 21:09:26.322000', '9000000001', '18275', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18287', '9000000001', '2023-04-03 21:14:26.576000', '9000000001', '18275', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18315', '9000000001', '2023-04-04 10:29:12.405000', '9000000001', '18312', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18316', '4', '2023-04-04 10:31:10.778000', '4', '18312', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허이"
    } ]
  } ]
}', NULL, 'send'), 
  ('18320', '9000000001', '2023-04-04 10:32:34.688000', '9000000001', '18317', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18324', '9000000001', '2023-04-04 10:33:17.105000', '9000000001', '18321', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18325', '4', '2023-04-04 10:33:48.811000', '16091', '18321', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T01:33:48.763Z"
  }
}', NULL, 'receive'), 
  ('18326', '9000000001', '2023-04-04 10:33:48.827000', '9000000001', '18321', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18327', '4', '2023-04-04 10:34:00.614000', '4', '18321', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어허라"
    } ]
  } ]
}', NULL, 'send'), 
  ('18328', '4', '2023-04-04 10:38:00.868000', '4', '18321', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅋㅋ"
    } ]
  } ]
}', NULL, 'send'), 
  ('18329', '4', '2023-04-04 10:40:00.066000', '4', '18321', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "흠"
    } ]
  } ]
}', NULL, 'send'), 
  ('18330', '4', '2023-04-04 10:40:03.883000', '16091', '18321', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "영"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T01:40:03.816Z"
  }
}', NULL, 'receive'), 
  ('18335', '9000000001', '2023-04-04 10:44:26.874000', '9000000001', '18332', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18336', '9000000001', '2023-04-04 10:44:41.553000', '9000000001', '18332', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잘 주무시죠?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18337', '9000000001', '2023-04-04 10:44:41.562000', '9000000001', '18332', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잘 주무시죠?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18338', '4', '2023-04-04 10:44:41.585000', '4', '18332', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18339', '9000000001', '2023-04-04 10:49:48.576000', '9000000001', '18332', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18345', '9000000001', '2023-04-04 10:50:29.844000', '9000000001', '18342', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18346', '4', '2023-04-04 10:50:42.792000', '4', '18342', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잠간만"
    } ]
  } ]
}', NULL, 'send'), 
  ('18355', '9000000001', '2023-04-04 10:53:06.079000', '9000000001', '18348', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18356', '4', '2023-04-04 10:53:07.952000', '16091', '18348', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T01:53:04.767Z"
  }
}', NULL, 'receive'), 
  ('18357', '9000000001', '2023-04-04 10:53:07.967000', '9000000001', '18348', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18358', '4', '2023-04-04 10:53:12.052000', '4', '18348', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니야"
    } ]
  } ]
}', NULL, 'send'), 
  ('18363', '9000000001', '2023-04-04 10:54:08.120000', '9000000001', '18360', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18364', '9000000001', '2023-04-04 10:54:19.409000', '9000000001', '18360', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "---"
    } ]
  } ]
}', NULL, 'send'), 
  ('18365', '9000000001', '2023-04-04 10:54:19.416000', '9000000001', '18360', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "---"
    } ]
  } ]
}', NULL, 'send'), 
  ('18366', '4', '2023-04-04 10:54:19.440000', '4', '18360', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ]
}', NULL, 'send'), 
  ('18371', '9000000001', '2023-04-04 10:55:39.284000', '9000000001', '18368', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18372', '4', '2023-04-04 10:55:53.068000', '4', '18368', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "랑"
    } ]
  } ]
}', NULL, 'send'), 
  ('18377', '9000000001', '2023-04-04 10:58:30.710000', '9000000001', '18374', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18378', '5', '2023-04-04 10:58:51.964000', '16926', '18374', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T01:58:51.927Z"
  }
}', NULL, 'receive'), 
  ('18379', '9000000001', '2023-04-04 10:58:51.985000', '9000000001', '18374', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18380', '5', '2023-04-04 10:59:03.722000', '5', '18374', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18392', '9000000001', '2023-04-04 11:00:50.293000', '9000000001', '18383', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18393', '9000000001', '2023-04-04 11:00:59.643000', '9000000001', '18383', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "첫 인사말 테스트 상담직원 2번입니다. "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/a63d9d18-db56-4639-93d5-dc936f7ac7cb.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18394', '9000000001', '2023-04-04 11:00:59.651000', '9000000001', '18383', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "첫 인사말 테스트 상담직원 2번입니다. "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/a63d9d18-db56-4639-93d5-dc936f7ac7cb.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18395', '5', '2023-04-04 11:00:59.689000', '5', '18383', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18396', '9000000001', '2023-04-04 11:01:19.039000', '9000000001', '18368', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18400', '9000000001', '2023-04-04 11:06:19.435000', '9000000001', '18383', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18401', '9000000001', '2023-04-04 11:06:19.436000', '9000000001', '18368', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18404', '9000000001', '2023-04-04 11:11:19.719000', '9000000001', '18383', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18408', '9000000001', '2023-04-04 11:49:25.575000', '9000000001', '18405', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18409', '4', '2023-04-04 11:49:35.520000', '16822', '18405', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ys"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T02:49:35.295Z"
  }
}', NULL, 'receive'), 
  ('18410', '9000000001', '2023-04-04 11:49:35.536000', '9000000001', '18405', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18414', '9000000001', '2023-04-04 11:56:50.793000', '9000000001', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18415', '4', '2023-04-04 11:57:00.777000', '16822', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ys"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T02:57:00.754Z"
  }
}', NULL, 'receive'), 
  ('18416', '9000000001', '2023-04-04 11:57:00.793000', '9000000001', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18417', '4', '2023-04-04 11:57:19.514000', '16822', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T02:57:19.474Z"
  }
}', NULL, 'receive'), 
  ('18418', '9000000001', '2023-04-04 11:57:19.528000', '9000000001', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18419', '9000000001', '2023-04-04 11:57:25.798000', '9000000001', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('18420', '9000000001', '2023-04-04 11:57:25.806000', '9000000001', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('18421', '4', '2023-04-04 11:57:25.829000', '4', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1"
    } ]
  } ]
}', NULL, 'send'), 
  ('18423', '9000000001', '2023-04-04 12:02:51.092000', '9000000001', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18426', '9000000001', '2023-04-04 12:07:51.550000', '9000000001', '18411', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18430', '9000000001', '2023-04-04 12:44:50.998000', '9000000001', '18427', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18431', '5', '2023-04-04 12:45:00.291000', '16822', '18427', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ys"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T03:45:00.243Z"
  }
}', NULL, 'receive'), 
  ('18432', '9000000001', '2023-04-04 12:45:00.306000', '9000000001', '18427', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18433', '5', '2023-04-04 12:45:45.827000', '16822', '18427', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "111"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T03:45:45.786Z"
  }
}', NULL, 'receive'), 
  ('18434', '9000000001', '2023-04-04 12:45:45.843000', '9000000001', '18427', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18438', '9000000001', '2023-04-04 12:46:01.796000', '9000000001', '18435', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18439', '5', '2023-04-04 12:46:16.649000', '16822', '18435', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ys"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T03:46:16.609Z"
  }
}', NULL, 'receive'), 
  ('18440', '9000000001', '2023-04-04 12:46:16.666000', '9000000001', '18435', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18441', '9000000001', '2023-04-04 12:46:20.098000', '9000000001', '18435', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "첫 인사말 테스트 상담직원 2번입니다. "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/a63d9d18-db56-4639-93d5-dc936f7ac7cb.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18442', '9000000001', '2023-04-04 12:46:20.107000', '9000000001', '18435', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "첫 인사말 테스트 상담직원 2번입니다. "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/a63d9d18-db56-4639-93d5-dc936f7ac7cb.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18443', '5', '2023-04-04 12:46:20.132000', '5', '18435', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1"
    } ]
  } ]
}', NULL, 'send'), 
  ('18454', '9000000001', '2023-04-04 16:06:16.996000', '9000000001', '18451', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18455', '4', '2023-04-04 16:06:20.712000', '16926', '18451', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:06:20.658Z"
  }
}', NULL, 'receive'), 
  ('18456', '9000000001', '2023-04-04 16:06:20.732000', '9000000001', '18451', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18457', '9000000001', '2023-04-04 16:06:30.542000', '9000000001', '18451', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18458', '9000000001', '2023-04-04 16:06:30.551000', '9000000001', '18451', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18459', '4', '2023-04-04 16:06:30.576000', '4', '18451', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18464', '9000000001', '2023-04-04 16:11:15.247000', '9000000001', '18461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18465', '9000000001', '2023-04-04 16:11:59.810000', '9000000001', '18461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18466', '9000000001', '2023-04-04 16:11:59.818000', '9000000001', '18461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18467', '4', '2023-04-04 16:11:59.848000', '4', '18461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18468', '9000000001', '2023-04-04 16:17:27.883000', '9000000001', '18461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18474', '9000000001', '2023-04-04 16:17:33.708000', '9000000001', '18471', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18475', '9000000001', '2023-04-04 16:17:49.088000', '9000000001', '18471', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18476', '9000000001', '2023-04-04 16:17:49.096000', '9000000001', '18471', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18477', '4', '2023-04-04 16:17:49.119000', '4', '18471', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안냐세[요."
    } ]
  } ]
}', NULL, 'send'), 
  ('18478', '4', '2023-04-04 16:18:22.551000', '16091', '18471', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허아류"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:18:22.491Z"
  }
}', NULL, 'receive'), 
  ('18479', '4', '2023-04-04 16:18:28.695000', '4', '18471', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "헉"
    } ]
  } ]
}', NULL, 'send'), 
  ('18480', '4', '2023-04-04 16:18:31.805000', '16091', '18471', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "학"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:18:31.731Z"
  }
}', NULL, 'receive'), 
  ('18485', '9000000001', '2023-04-04 16:19:01.103000', '9000000001', '18482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18486', '9000000001', '2023-04-04 16:19:07.970000', '9000000001', '18482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18487', '9000000001', '2023-04-04 16:19:07.978000', '9000000001', '18482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18488', '4', '2023-04-04 16:19:08', '4', '18482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예"
    } ]
  } ]
}', NULL, 'send'), 
  ('18489', '4', '2023-04-04 16:19:15.783000', '16091', '18482', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "헉"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:19:15.76Z"
  }
}', NULL, 'receive'), 
  ('18494', '9000000001', '2023-04-04 16:21:09.156000', '9000000001', '18491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18495', '9000000001', '2023-04-04 16:21:15.378000', '9000000001', '18491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18496', '9000000001', '2023-04-04 16:21:15.386000', '9000000001', '18491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18497', '4', '2023-04-04 16:21:15.410000', '4', '18491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예"
    } ]
  } ]
}', NULL, 'send'), 
  ('18498', '4', '2023-04-04 16:21:18.739000', '16091', '18491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "제"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:21:18.71Z"
  }
}', NULL, 'receive'), 
  ('18499', '4', '2023-04-04 16:21:21.457000', '4', '18491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아"
    } ]
  } ]
}', NULL, 'send'), 
  ('18500', '4', '2023-04-04 16:21:24.230000', '16091', '18491', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:21:24.187Z"
  }
}', NULL, 'receive'), 
  ('18502', '9000000001', '2023-04-04 16:22:28.131000', '9000000001', '18461', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18507', '9000000001', '2023-04-04 16:25:18.364000', '9000000001', '18504', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18508', '9000000001', '2023-04-04 16:25:41.900000', '9000000001', '18504', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18509', '9000000001', '2023-04-04 16:25:41.908000', '9000000001', '18504', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18510', '4', '2023-04-04 16:25:41.932000', '4', '18504', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오호"
    } ]
  } ]
}', NULL, 'send'), 
  ('18515', '9000000001', '2023-04-04 16:27:38.059000', '9000000001', '18512', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18516', '9000000001', '2023-04-04 16:28:00.965000', '9000000001', '18512', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18517', '9000000001', '2023-04-04 16:28:00.972000', '9000000001', '18512', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18518', '4', '2023-04-04 16:28:00.994000', '4', '18512', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "양"
    } ]
  } ]
}', NULL, 'send'), 
  ('18519', '9000000001', '2023-04-04 16:33:28.530000', '9000000001', '18512', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18522', '9000000001', '2023-04-04 16:38:28.771000', '9000000001', '18512', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18527', '9000000001', '2023-04-04 16:43:59.874000', '9000000001', '18524', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18528', '9000000001', '2023-04-04 16:44:13.011000', '9000000001', '18524', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18529', '4', '2023-04-04 16:44:13.161000', '4', '18524', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어히"
    } ]
  } ]
}', NULL, 'send'), 
  ('18530', '4', '2023-04-04 16:44:24.295000', '16091', '18524', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어아"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:44:24.217Z"
  }
}', NULL, 'receive'), 
  ('18535', '9000000001', '2023-04-04 16:44:59.954000', '9000000001', '18532', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18536', '9000000001', '2023-04-04 16:45:08.748000', '9000000001', '18532', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18537', '4', '2023-04-04 16:45:08.772000', '4', '18532', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예"
    } ]
  } ]
}', NULL, 'send'), 
  ('18538', '4', '2023-04-04 16:45:12.858000', '16091', '18532', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:45:12.793Z"
  }
}', NULL, 'receive'), 
  ('18543', '9000000001', '2023-04-04 16:45:43.631000', '9000000001', '18540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18544', '4', '2023-04-04 16:45:51.833000', '16091', '18540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "말"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:45:51.776Z"
  }
}', NULL, 'receive'), 
  ('18545', '9000000001', '2023-04-04 16:45:51.857000', '9000000001', '18540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18546', '9000000001', '2023-04-04 16:45:56.968000', '9000000001', '18540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18547', '4', '2023-04-04 16:45:56.988000', '4', '18540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어이"
    } ]
  } ]
}', NULL, 'send'), 
  ('18548', '4', '2023-04-04 16:46:17.062000', '4', '18540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "희안 하네"
    } ]
  } ]
}', NULL, 'send'), 
  ('18549', '4', '2023-04-04 16:46:20.287000', '16091', '18540', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그ㅜ러게"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:46:20.233Z"
  }
}', NULL, 'receive'), 
  ('18554', '9000000001', '2023-04-04 16:46:35.791000', '9000000001', '18551', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18555', '9000000001', '2023-04-04 16:46:38.993000', '9000000001', '18551', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18556', '4', '2023-04-04 16:46:39.016000', '4', '18551', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예"
    } ]
  } ]
}', NULL, 'send'), 
  ('18557', '4', '2023-04-04 16:46:49.241000', '16091', '18551', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "반갑습니다."
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:46:49.193Z"
  }
}', NULL, 'receive'), 
  ('18558', '4', '2023-04-04 16:46:52.080000', '4', '18551', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "저도요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18569', '9000000001', '2023-04-04 16:52:11.514000', '9000000001', '18551', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18571', '5', '2023-04-04 16:52:11.706000', '15659', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하하"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:52:11.674Z"
  }
}', NULL, 'receive'), 
  ('18572', '5', '2023-04-04 16:52:43.214000', '15659', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:52:43.135Z"
  }
}', NULL, 'receive'), 
  ('18573', '5', '2023-04-04 16:53:03.251000', '5', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('18574', '5', '2023-04-04 16:53:08.610000', '15659', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:53:08.554Z"
  }
}', NULL, 'receive'), 
  ('18575', '5', '2023-04-04 16:53:12.285000', '15659', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어2"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:53:12.266Z"
  }
}', NULL, 'receive'), 
  ('18576', '5', '2023-04-04 16:53:13.209000', '15659', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아몬드"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:53:13.161Z"
  }
}', NULL, 'receive'), 
  ('18577', '5', '2023-04-04 16:53:19.550000', '15659', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1은 금지어2와 같이 금지어입니다."
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T07:53:19.522Z"
  }
}', NULL, 'receive'), 
  ('18579', '9000000001', '2023-04-04 16:57:11.862000', '9000000001', '18551', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18586', '9000000001', '2023-04-04 17:14:00.406000', '9000000001', '18583', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18587', '9000000001', '2023-04-04 17:14:08.056000', '9000000001', '18583', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "첫 인사말 테스트 상담직원 2번입니다. "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/a63d9d18-db56-4639-93d5-dc936f7ac7cb.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18588', '5', '2023-04-04 17:14:08.084000', '5', '18583', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('18589', '5', '2023-04-04 17:14:12.020000', '18581', '18583', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T08:14:11.986Z"
  }
}', NULL, 'receive'), 
  ('18590', '5', '2023-04-04 17:14:14.993000', '18581', '18583', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZRlmRZJi1/iXDmRAyD0vqkcsQA57s4vk/i_0682f19d182c.jpg",
      "display" : "image"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T08:14:14.912Z"
  }
}', NULL, 'receive'), 
  ('18591', '5', '2023-04-04 17:14:21.084000', '5', '18583', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "굳굳"
    } ]
  } ]
}', NULL, 'send'), 
  ('18610', '9000000001', '2023-04-04 17:24:20.401000', '9000000001', '17958', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18616', '9000000001', '2023-04-04 17:25:16.304000', '9000000001', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18617', '9000000001', '2023-04-04 17:25:24.212000', '9000000001', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "첫 인사말 테스트 상담직원 2번입니다. "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/a63d9d18-db56-4639-93d5-dc936f7ac7cb.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18618', '5', '2023-04-04 17:25:24.234000', '5', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18619', '5', '2023-04-04 17:25:36.083000', '15659', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T08:25:35.997Z"
  }
}', NULL, 'receive'), 
  ('18620', '5', '2023-04-04 17:25:37.911000', '15659', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어2"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-04T08:25:37.884Z"
  }
}', NULL, 'receive'), 
  ('18629', '9000000001', '2023-04-05 10:24:04.098000', '9000000001', '18626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18630', '9000000001', '2023-04-05 10:27:50.961000', '9000000001', '18626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18631', '4', '2023-04-05 10:27:50.988000', '4', '18626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18632', '9000000001', '2023-04-05 10:33:09.361000', '9000000001', '18626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18635', '9000000001', '2023-04-05 10:38:09.627000', '9000000001', '18626', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18638', '5', '2023-04-05 14:33:37.616000', '5', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "누구세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18639', '5', '2023-04-05 14:33:54.525000', '15659', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "asher 입니다.."
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T05:33:53.375Z"
  }
}', NULL, 'receive'), 
  ('18644', '9000000001', '2023-04-05 14:34:28.691000', '9000000001', '18641', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18645', '5', '2023-04-05 14:34:42.623000', '5', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대화창에는 아무런 응답이 안나오네요 ㅎ ㅎ"
    } ]
  } ]
}', NULL, 'send'), 
  ('18646', '9000000001', '2023-04-05 14:34:44.313000', '9000000001', '18641', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18647', '4', '2023-04-05 14:34:44.332000', '4', '18641', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어"
    } ]
  } ]
}', NULL, 'send'), 
  ('18648', '5', '2023-04-05 14:35:06.242000', '15659', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹 ?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T05:35:05.775Z"
  }
}', NULL, 'receive'), 
  ('18649', '5', '2023-04-05 14:35:11.413000', '5', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이제 나오네요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18650', '5', '2023-04-05 14:35:11.438000', '15659', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "무슨뜻이죵 ?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T05:35:11.404Z"
  }
}', NULL, 'receive'), 
  ('18651', '5', '2023-04-05 14:35:18.424000', '15659', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아 네넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T05:35:18.379Z"
  }
}', NULL, 'receive'), 
  ('18652', '5', '2023-04-05 14:35:20.116000', '5', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어 제가 보내는 메시지는 대화창에 안나와요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18653', '5', '2023-04-05 14:35:25.291000', '5', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아 이제 나오네요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18654', '5', '2023-04-05 14:35:30.496000', '15659', '18613', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "음;"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T05:35:30.476Z"
  }
}', NULL, 'receive'), 
  ('18659', '9000000001', '2023-04-05 14:37:31.556000', '9000000001', '18656', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18660', '9000000001', '2023-04-05 14:37:59.508000', '9000000001', '18656', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18661', '4', '2023-04-05 14:37:59.526000', '4', '18656', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예"
    } ]
  } ]
}', NULL, 'send'), 
  ('18666', '9000000001', '2023-04-05 14:38:36.995000', '9000000001', '18663', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18668', '9000000001', '2023-04-05 14:39:24.117000', '9000000001', '18663', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18669', '4', '2023-04-05 14:39:24.137000', '4', '18663', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "엉"
    } ]
  } ]
}', NULL, 'send'), 
  ('18674', '9000000001', '2023-04-05 14:42:45.588000', '9000000001', '18671', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18675', '9000000001', '2023-04-05 14:42:53.177000', '9000000001', '18671', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트<BR/>"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/2c8fa43e-eb08-41ba-ad13-6f63f260badb.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18676', '4', '2023-04-05 14:42:53.195000', '4', '18671', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이고"
    } ]
  } ]
}', NULL, 'send'), 
  ('18677', '4', '2023-04-05 14:44:37.167000', '4', '18671', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "헝"
    } ]
  } ]
}', NULL, 'send'), 
  ('18682', '9000000001', '2023-04-05 14:45:36.405000', '9000000001', '18679', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18684', '9000000001', '2023-04-05 14:45:49.086000', '9000000001', '18679', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트<BR/>"
    } ]
  } ]
}', NULL, 'send'), 
  ('18685', '4', '2023-04-05 14:45:49.112000', '4', '18679', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "엉"
    } ]
  } ]
}', NULL, 'send'), 
  ('18686', '9000000001', '2023-04-05 14:51:15.930000', '9000000001', '18679', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18689', '9000000001', '2023-04-05 14:56:16.198000', '9000000001', '18679', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18694', '9000000001', '2023-04-05 14:59:01.149000', '9000000001', '18691', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18695', '5', '2023-04-05 14:59:44.690000', '16926', '18691', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T05:59:44.642Z"
  }
}', NULL, 'receive'), 
  ('18696', '9000000001', '2023-04-05 14:59:44.710000', '9000000001', '18691', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18701', '9000000001', '2023-04-05 15:00:14.021000', '9000000001', '18698', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18702', '5', '2023-04-05 15:00:22.937000', '16926', '18698', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T06:00:22.907Z"
  }
}', NULL, 'receive'), 
  ('18703', '9000000001', '2023-04-05 15:00:22.951000', '9000000001', '18698', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18704', '9000000001', '2023-04-05 15:01:53.549000', '9000000001', '18698', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "첫 인사말 테스트 상담직원 2번입니다. "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/a63d9d18-db56-4639-93d5-dc936f7ac7cb.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18705', '5', '2023-04-05 15:01:53.568000', '5', '18698', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18710', '9000000001', '2023-04-05 15:03:41.098000', '9000000001', '18707', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18711', '5', '2023-04-05 15:03:50.662000', '16926', '18707', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T06:03:50.624Z"
  }
}', NULL, 'receive'), 
  ('18712', '9000000001', '2023-04-05 15:03:50.678000', '9000000001', '18707', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18713', '5', '2023-04-05 15:03:58.860000', '5', '18707', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18714', '5', '2023-04-05 15:04:36.971000', '16926', '18707', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종신보험 관련해서 문의 드릴게 있어서요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T06:04:36.927Z"
  }
}', NULL, 'receive'), 
  ('18715', '5', '2023-04-05 15:04:56.554000', '16926', '18707', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "보험상품 문의드릴게 있는데요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T06:04:56.528Z"
  }
}', NULL, 'receive'), 
  ('18720', '9000000001', '2023-04-05 15:15:47.681000', '9000000001', '18717', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18721', '5', '2023-04-05 15:16:00.500000', '16926', '18717', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T06:16:00.449Z"
  }
}', NULL, 'receive'), 
  ('18722', '9000000001', '2023-04-05 15:16:00.519000', '9000000001', '18717', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18727', '9000000001', '2023-04-05 15:20:19.119000', '9000000001', '18724', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18728', '5', '2023-04-05 15:20:29.498000', '16926', '18724', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T06:20:29.454Z"
  }
}', NULL, 'receive'), 
  ('18729', '9000000001', '2023-04-05 15:20:29.516000', '9000000001', '18724', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18730', '5', '2023-04-05 15:20:39.195000', '5', '18724', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18731', '5', '2023-04-05 15:20:45.310000', '16926', '18724', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종신보험 관심있어요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T06:20:45.273Z"
  }
}', NULL, 'receive'), 
  ('18736', '9000000001', '2023-04-05 15:29:25.786000', '9000000001', '18733', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18737', '9000000001', '2023-04-05 15:29:36.749000', '9000000001', '18733', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('18738', '5', '2023-04-05 15:29:36.769000', '5', '18733', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18743', '9000000001', '2023-04-05 15:32:09.659000', '9000000001', '18740', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18750', '9000000001', '2023-04-05 15:36:39.971000', '9000000001', '18747', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18755', '9000000001', '2023-04-05 15:44:34.190000', '9000000001', '18752', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18756', '9000000001', '2023-04-05 15:51:47.571000', '9000000001', '18752', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('18763', '9000000001', '2023-04-05 15:56:35.750000', '9000000001', '18760', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18768', '9000000001', '2023-04-05 16:03:29.080000', '16926', '18766', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T07:03:29.033Z"
  }
}', NULL, 'receive'), 
  ('18769', '9000000001', '2023-04-05 16:03:29.098000', '9000000001', '18766', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18771', '9000000001', '2023-04-05 16:03:32.670000', '9000000001', '18766', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18772', '9000000001', '2023-04-05 16:03:37.733000', '9000000001', '18766', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 김상담입니다. "
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/3facf187-ee48-4180-a0d1-bab0ff6c7e59.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18773', '5', '2023-04-05 16:03:37.751000', '5', '18766', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18774', '9000000001', '2023-04-05 16:03:48.130000', '9000000001', '18760', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('18779', '9000000001', '2023-04-05 16:05:11.570000', '16926', '18777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T07:05:11.538Z"
  }
}', NULL, 'receive'), 
  ('18780', '9000000001', '2023-04-05 16:05:11.588000', '9000000001', '18777', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18784', '9000000001', '2023-04-05 16:05:51.660000', '16926', '18782', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T07:05:51.623Z"
  }
}', NULL, 'receive'), 
  ('18785', '9000000001', '2023-04-05 16:05:51.682000', '9000000001', '18782', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18791', '9000000001', '2023-04-05 16:05:54.748000', '9000000001', '18782', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18792', '9000000001', '2023-04-05 16:05:54.748000', '9000000001', '18787', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18793', '9000000001', '2023-04-05 16:06:00.951000', '9000000001', '18782', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험 전문 상담사입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/3facf187-ee48-4180-a0d1-bab0ff6c7e59.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18794', '5', '2023-04-05 16:06:00.976000', '5', '18782', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18799', '9000000001', '2023-04-05 16:08:00.875000', '9000000001', '18796', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18800', '9000000001', '2023-04-05 16:11:18.287000', '9000000001', '18782', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18802', '9000000001', '2023-04-05 16:15:18.631000', '9000000001', '18796', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('18805', '9000000001', '2023-04-05 16:16:18.535000', '9000000001', '18782', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18806', '4', '2023-04-05 16:19:50.214000', '16091', '18796', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T07:19:50.161Z"
  }
}', NULL, 'receive'), 
  ('18807', '9000000001', '2023-04-05 16:19:50.234000', '9000000001', '18796', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('18808', '9000000001', '2023-04-05 16:20:08', '9000000001', '18796', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트<BR/>"
    } ]
  } ]
}', NULL, 'send'), 
  ('18809', '4', '2023-04-05 16:20:08.024000', '4', '18796', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예"
    } ]
  } ]
}', NULL, 'send'), 
  ('18814', '9000000001', '2023-04-05 16:22:45.946000', '9000000001', '18811', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18815', '9000000001', '2023-04-05 16:29:49.237000', '9000000001', '18811', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('18821', '9000000001', '2023-04-05 17:01:13.977000', '9000000001', '18818', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('18828', '9000000001', '2023-04-05 18:07:39.064000', '9000000001', '18825', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18829', '9000000001', '2023-04-05 18:09:03.830000', '9000000001', '18825', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험 전문 상담사입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/3facf187-ee48-4180-a0d1-bab0ff6c7e59.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18830', '5', '2023-04-05 18:09:03.897000', '5', '18825', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "난"
    } ]
  } ]
}', NULL, 'send'), 
  ('18831', '5', '2023-04-05 18:09:10.605000', '16091', '18825', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넌"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T09:09:10.533Z"
  }
}', NULL, 'receive'), 
  ('18832', '9000000001', '2023-04-05 18:09:10.634000', '9000000001', '18825', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18833', '5', '2023-04-05 18:09:55.955000', '5', '18825', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/3facf187-ee48-4180-a0d1-bab0ff6c7e59.png"
    } ]
  } ]
}', NULL, 'send'), 
  ('18834', '9000000001', '2023-04-05 18:15:00.688000', '9000000001', '18825', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18837', '9000000001', '2023-04-05 18:20:01.016000', '9000000001', '18825', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18842', '9000000001', '2023-04-05 18:49:09.607000', '9000000001', '18839', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18843', '9000000001', '2023-04-05 18:56:31.883000', '9000000001', '18839', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('18849', '9000000001', '2023-04-05 20:42:03.715000', '9000000001', '18846', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18850', '5', '2023-04-05 20:42:11.660000', '16926', '18846', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T11:42:11.622Z"
  }
}', NULL, 'receive'), 
  ('18851', '9000000001', '2023-04-05 20:42:11.895000', '9000000001', '18846', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18852', '9000000001', '2023-04-05 20:42:20.404000', '9000000001', '18846', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험 전문 상담사입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/3facf187-ee48-4180-a0d1-bab0ff6c7e59.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18853', '5', '2023-04-05 20:42:20.426000', '5', '18846', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18854', '5', '2023-04-05 20:44:13.542000', '16926', '18846', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T11:44:12.923Z"
  }
}', NULL, 'receive'), 
  ('18860', '9000000001', '2023-04-05 20:55:01.725000', '9000000001', '18857', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18861', '9000000001', '2023-04-05 20:55:10.666000', '9000000001', '18857', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/20aab4b7-3b46-4fcf-98b3-7470dfab2885.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18862', '5', '2023-04-05 20:55:10.687000', '5', '18857', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18869', '9000000001', '2023-04-05 20:58:07.247000', '9000000001', '18866', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18870', '9000000001', '2023-04-05 20:58:26.138000', '9000000001', '18866', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사랑하는 고객님 반갑습니다. 상담직원 상담원2 입니다. 무엇을 도와드릴까요?"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/39f7f7a3-4f84-425f-a027-a38ebd6c9114.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18871', '5', '2023-04-05 20:58:26.157000', '5', '18866', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18877', '9000000001', '2023-04-05 20:59:16.112000', '9000000001', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18878', '9000000001', '2023-04-05 20:59:23.350000', '9000000001', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/774b65d3-b5f3-4dea-857b-8c3253fc9c64.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18879', '5', '2023-04-05 20:59:23.370000', '5', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18880', '5', '2023-04-05 21:01:03.308000', '16926', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종신보험 상담하고싶어요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T12:01:03.26Z"
  }
}', NULL, 'receive'), 
  ('18881', '5', '2023-04-05 21:01:15.485000', '5', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 고객님 잠시만 기다려주세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18882', '5', '2023-04-05 21:02:08.946000', '16926', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종신보험 상담하고싶어요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T12:02:08.885Z"
  }
}', NULL, 'receive'), 
  ('18883', '9000000001', '2023-04-05 21:02:08.966000', '9000000001', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18884', '5', '2023-04-05 21:03:23.630000', '5', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 고객님 잠시만기다려주세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('18892', '9000000001', '2023-04-05 21:08:35.979000', '9000000001', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18894', '5', '2023-04-05 21:08:39.567000', '5', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "신용점수가 높다는 것은 차주가 빚을 제때 갚은 실적이 좋다는 의미"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "신용점수",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18896', '5', '2023-04-05 21:08:43.018000', '5', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "소득이 안정적이고 총부채상환비율이 낮은 대출자는 대출 승인을 받을 가능성이 높음"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "소득",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18898', '5', '2023-04-05 21:08:47.482000', '5', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18900', '5', '2023-04-05 21:10:07.078000', '16926', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "뭐라고 하는지 잘 모르겠어요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T12:10:06.993Z"
  }
}', NULL, 'receive'), 
  ('18901', '9000000001', '2023-04-05 21:10:07.102000', '9000000001', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('18907', '9000000001', '2023-04-05 21:10:49.224000', '9000000001', '18874', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18914', '9000000001', '2023-04-05 21:13:05.887000', '9000000001', '18912', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('18918', '9000000001', '2023-04-05 21:13:44.270000', '9000000001', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18919', '4', '2023-04-05 21:13:45.776000', '16926', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T12:13:45.742Z"
  }
}', NULL, 'receive'), 
  ('18920', '9000000001', '2023-04-05 21:13:45.794000', '9000000001', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18921', '4', '2023-04-05 21:13:55.850000', '16926', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T12:13:55.825Z"
  }
}', NULL, 'receive'), 
  ('18922', '9000000001', '2023-04-05 21:13:55.867000', '9000000001', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18923', '4', '2023-04-05 21:14:23.256000', '16926', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T12:14:23.236Z"
  }
}', NULL, 'receive'), 
  ('18924', '9000000001', '2023-04-05 21:14:23.274000', '9000000001', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18925', '9000000001', '2023-04-05 21:21:38.203000', '9000000001', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('18927', '9000000001', '2023-04-05 21:59:34.041000', '9000000001', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('18928', '4', '2023-04-05 21:59:34.063000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "신용점수가 높다는 것은 차주가 빚을 제때 갚은 실적이 좋다는 의미"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "신용점수",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18930', '4', '2023-04-05 21:59:49.722000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "소득이 안정적이고 총부채상환비율이 낮은 대출자는 대출 승인을 받을 가능성이 높음"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "소득",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18932', '4', '2023-04-05 22:00:07.482000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18934', '4', '2023-04-05 22:00:11.133000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "사업자 대출의 경우 금융기관은 대출자의 재무건전성과 상환능력을 평가하기 위해 손익계산서, 대차대조표, 현금흐름계산서 등의 재무제표 검토"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "재무제표",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18936', '4', '2023-04-05 22:01:52.070000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18938', '4', '2023-04-05 22:02:01.398000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안정적인 미래 현금흐름을 창출할 가능성이 높은 사업이 대출받기에 유리함"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "대출 목적",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18940', '4', '2023-04-05 22:02:05.415000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('18941', '4', '2023-04-05 22:02:20.836000', '16926', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "텍스트 입력은 사라지는군요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-05T13:02:20.75Z"
  }
}', NULL, 'receive'), 
  ('18942', '4', '2023-04-05 22:02:29.325000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허너"
    } ]
  } ]
}', NULL, 'send'), 
  ('18944', '4', '2023-04-05 22:02:33.897000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18946', '4', '2023-04-05 22:02:37.748000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18948', '4', '2023-04-05 22:02:41.443000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18950', '4', '2023-04-05 22:02:46.050000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18952', '4', '2023-04-05 22:03:05.032000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18954', '4', '2023-04-05 22:03:18.533000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18956', '4', '2023-04-05 22:03:35.815000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18958', '4', '2023-04-05 22:03:49.943000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안정적인 미래 현금흐름을 창출할 가능성이 높은 사업이 대출받기에 유리함"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "대출 목적",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18960', '4', '2023-04-05 22:03:59.395000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "담보"
    } ]
  } ]
}', NULL, 'send'), 
  ('18962', '4', '2023-04-05 22:04:06.454000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "차주가 대출금을 상환할 수 없는 경우, 금융기관이 자산을 처분할 수 있는 권리를 부여 \n금융 기관은 토지, 주택, 주식 등 다양한 자산에 대해 담보 요구 가능"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "담보",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18964', '4', '2023-04-05 22:04:31.557000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "차주가 대출금을 상환할 수 없는 경우, 금융기관이 자산을 처분할 수 있는 권리를 부여 \n금융 기관은 토지, 주택, 주식 등 다양한 자산에 대해 담보 요구 가능"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "담보",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18966', '4', '2023-04-05 22:04:39.318000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "차주가 대출금을 상환할 수 없는 경우, 금융기관이 자산을 처분할 수 있는 권리를 부여 \n금융 기관은 토지, 주택, 주식 등 다양한 자산에 대해 담보 요구 가능"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "담보",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18968', '4', '2023-04-05 22:04:49.384000', '4', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "미상환 대출과 신용카드 잔고를 포함한 대출자의 기존 부채를 고려\n기존 부채가 많을수록 대출받기가 어려움"
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "기존 부채",
        "data" : "https://kakaoenterprise.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('18970', '9000000001', '2023-04-05 22:10:07.873000', '9000000001', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18973', '9000000001', '2023-04-05 22:15:08.143000', '9000000001', '18915', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18978', '9000000001', '2023-04-06 09:31:13.754000', '9000000001', '18975', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18979', '9000000001', '2023-04-06 09:31:19.544000', '9000000001', '18975', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/774b65d3-b5f3-4dea-857b-8c3253fc9c64.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('18980', '5', '2023-04-06 09:31:19.561000', '5', '18975', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dP"
    } ]
  } ]
}', NULL, 'send'), 
  ('18981', '9000000001', '2023-04-06 09:36:26.014000', '9000000001', '18975', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('18984', '9000000001', '2023-04-06 09:41:26.254000', '9000000001', '18975', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('18995', '9000000001', '2023-04-06 10:54:12.931000', '9000000001', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18996', '4', '2023-04-06 10:54:18.137000', '15606', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T01:54:18.05Z"
  }
}', NULL, 'receive'), 
  ('18997', '9000000001', '2023-04-06 10:54:18.155000', '9000000001', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('18998', '9000000001', '2023-04-06 10:54:30.149000', '9000000001', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('18999', '4', '2023-04-06 10:54:30.168000', '4', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19000', '4', '2023-04-06 10:54:44.933000', '15606', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 안녕하세요, 신용 대출 상담"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T01:54:44.905Z"
  }
}', NULL, 'receive'), 
  ('19001', '4', '2023-04-06 10:54:47.336000', '15606', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "해주세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T01:54:47.317Z"
  }
}', NULL, 'receive'), 
  ('19014', '9000000001', '2023-04-06 14:23:06.034000', '9000000001', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19016', '9000000001', '2023-04-06 14:25:48.060000', '9000000001', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/9c40a815-89b9-4024-8b2b-e14c60641f6d.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19017', '4', '2023-04-06 14:25:48.080000', '4', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허허"
    } ]
  } ]
}', NULL, 'send'), 
  ('19018', '9000000001', '2023-04-06 14:31:03.898000', '9000000001', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19020', '4', '2023-04-06 14:32:13.059000', '16091', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니요."
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T05:32:12.966Z"
  }
}', NULL, 'receive'), 
  ('19021', '4', '2023-04-06 14:32:16.113000', '16091', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "있어요."
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T05:32:16.076Z"
  }
}', NULL, 'receive'), 
  ('19027', '9000000001', '2023-04-06 15:41:28.347000', '9000000001', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19028', '4', '2023-04-06 15:41:35.744000', '16926', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 ~. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T06:41:35.699Z"
  }
}', NULL, 'receive'), 
  ('19029', '9000000001', '2023-04-06 15:41:35.764000', '9000000001', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19030', '9000000001', '2023-04-06 15:48:37.978000', '9000000001', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19032', '9000000001', '2023-04-06 15:55:12.465000', '9000000001', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/9c40a815-89b9-4024-8b2b-e14c60641f6d.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19033', '4', '2023-04-06 15:55:12.488000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "나가지마!!!!!!!!!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19034', '4', '2023-04-06 15:55:56.475000', '16926', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "첫인사말 이미지는?????"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T06:55:56.376Z"
  }
}', NULL, 'receive'), 
  ('19035', '4', '2023-04-06 16:05:51.325000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZRAwNKySa/FbnE0RrwCELVVtWoSCGVkk/i_fho29h.png",
      "display" : "image/png",
      "name" : "Bootstrap_logo.svg.png",
      "size" : 9743
    } ]
  } ]
}', NULL, 'send'), 
  ('19036', '4', '2023-04-06 16:07:19.757000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예"
    } ]
  } ]
}', NULL, 'send'), 
  ('19037', '4', '2023-04-06 16:07:26.094000', '16091', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어할"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T07:07:26.034Z"
  }
}', NULL, 'receive'), 
  ('19038', '4', '2023-04-06 16:07:31.225000', '4', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://dn-m.talk.kakao.com/talkm/oZRA2lwpBR/J2Kky05UpyfqvaPouku6tk/i_knsl6x.png",
      "display" : "image/png",
      "name" : "Bootstrap_logo.svg.png",
      "size" : 9743
    } ]
  } ]
}', NULL, 'send'), 
  ('19039', '9000000001', '2023-04-06 16:12:36.654000', '9000000001', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19041', '9000000001', '2023-04-06 16:12:36.709000', '9000000001', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19043', '4', '2023-04-06 16:12:57.048000', '16926', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오 이미지 나오네요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T07:12:56.992Z"
  }
}', NULL, 'receive'), 
  ('19045', '9000000001', '2023-04-06 16:17:36.928000', '9000000001', '19011', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19052', '4', '2023-04-06 17:31:02.163000', '4', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('19053', '9000000001', '2023-04-06 17:36:09.059000', '9000000001', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19056', '9000000001', '2023-04-06 17:41:09.310000', '9000000001', '18992', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19058', '4', '2023-04-06 17:53:24.388000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아"
    } ]
  } ]
}', NULL, 'send'), 
  ('19059', '4', '2023-04-06 17:53:48.722000', '16926', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "무슨일 있으신가요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T08:53:47.598Z"
  }
}', NULL, 'receive'), 
  ('19060', '4', '2023-04-06 17:54:19.475000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "업어"
    } ]
  } ]
}', NULL, 'send'), 
  ('19061', '4', '2023-04-06 17:54:27.345000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "없어"
    } ]
  } ]
}', NULL, 'send'), 
  ('19062', '4', '2023-04-06 17:57:46.237000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대시보드 지연 테스트 해보았습니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('19063', '4', '2023-04-06 18:02:16.946000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "저녁 드시나요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('19064', '4', '2023-04-06 18:02:32.623000', '16926', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "야근 하시나요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T09:02:32.477Z"
  }
}', NULL, 'receive'), 
  ('19065', '4', '2023-04-06 18:04:50.094000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ]
}', NULL, 'send'), 
  ('19066', '4', '2023-04-06 18:05:17.644000', '16926', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오늘 목요일인데 야근해요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T09:05:17.59Z"
  }
}', NULL, 'receive'), 
  ('19067', '4', '2023-04-06 18:05:25.489000', '16926', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "라커 야근하신데요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T09:05:25.445Z"
  }
}', NULL, 'receive'), 
  ('19073', '9000000001', '2023-04-06 18:45:06.758000', '9000000001', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19074', '9000000001', '2023-04-06 18:45:21.017000', '9000000001', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/9c40a815-89b9-4024-8b2b-e14c60641f6d.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19075', '4', '2023-04-06 18:45:21.078000', '4', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "wjrldy"
    } ]
  } ]
}', NULL, 'send'), 
  ('19076', '4', '2023-04-06 18:45:24.913000', '4', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "저기요."
    } ]
  } ]
}', NULL, 'send'), 
  ('19077', '4', '2023-04-06 18:45:33.710000', '16091', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "왜요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T09:45:33.643Z"
  }
}', NULL, 'receive'), 
  ('19078', '4', '2023-04-06 18:45:37.977000', '4', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니거든요."
    } ]
  } ]
}', NULL, 'send'), 
  ('19079', '4', '2023-04-06 18:45:42.292000', '16091', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "맞거든요."
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T09:45:42.255Z"
  }
}', NULL, 'receive'), 
  ('19084', '9000000001', '2023-04-06 18:46:22.300000', '9000000001', '19081', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19085', '5', '2023-04-06 18:47:42.377000', '15585', '19081', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T09:47:42.312Z"
  }
}', NULL, 'receive'), 
  ('19086', '9000000001', '2023-04-06 18:47:42.404000', '9000000001', '19081', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19087', '9000000001', '2023-04-06 18:49:29.735000', '9000000001', '19081', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/774b65d3-b5f3-4dea-857b-8c3253fc9c64.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19088', '5', '2023-04-06 18:49:29.769000', '5', '19081', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ]
}', NULL, 'send'), 
  ('19093', '9000000001', '2023-04-06 18:52:11.630000', '9000000001', '19090', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19094', '9000000001', '2023-04-06 18:52:25.707000', '9000000001', '19090', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/774b65d3-b5f3-4dea-857b-8c3253fc9c64.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19095', '5', '2023-04-06 18:52:25.728000', '5', '19090', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "인사말"
    } ]
  } ]
}', NULL, 'send'), 
  ('19097', '9000000001', '2023-04-06 18:57:26.201000', '9000000001', '19090', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19104', '9000000001', '2023-04-06 19:01:14.266000', '9000000001', '19101', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19105', '5', '2023-04-06 19:01:21.143000', '15585', '19101', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎㅎ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T10:01:21.098Z"
  }
}', NULL, 'receive'), 
  ('19106', '9000000001', '2023-04-06 19:01:21.162000', '9000000001', '19101', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19107', '9000000001', '2023-04-06 19:01:25.663000', '9000000001', '19101', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19108', '5', '2023-04-06 19:01:25.693000', '5', '19101', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이"
    } ]
  } ]
}', NULL, 'send'), 
  ('19109', '9000000001', '2023-04-06 19:06:26.792000', '9000000001', '19101', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19112', '9000000001', '2023-04-06 19:11:27.291000', '9000000001', '19101', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19120', '9000000001', '2023-04-06 19:28:55.622000', '9000000001', '19117', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19121', '9000000001', '2023-04-06 19:35:57.333000', '9000000001', '19117', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19128', '9000000001', '2023-04-06 19:39:46.983000', '9000000001', '19125', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19129', '4', '2023-04-06 19:40:06.758000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "라커 퇴근하고 집에서 일하신데요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19130', '4', '2023-04-06 19:41:14.628000', '4', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "맞아요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('19131', '4', '2023-04-06 19:41:21.045000', '4', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "앗 여긴 하나만 나가네요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19132', '4', '2023-04-06 19:41:29.381000', '4', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오잉 갑자기 하나"
    } ]
  } ]
}', NULL, 'send'), 
  ('19137', '9000000001', '2023-04-06 19:45:23.317000', '9000000001', '19134', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19141', '9000000001', '2023-04-06 19:46:28.332000', '9000000001', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19144', '9000000001', '2023-04-06 19:46:29.162000', '9000000001', '19139', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19145', '9000000001', '2023-04-06 19:46:58.012000', '9000000001', '19125', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19147', '9000000001', '2023-04-06 19:46:58.389000', '9000000001', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19155', '9000000001', '2023-04-06 19:51:29.012000', '9000000001', '19070', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19157', '9000000001', '2023-04-06 19:51:59.078000', '9000000001', '19024', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19159', '9000000001', '2023-04-06 19:53:58.430000', '9000000001', '19139', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19179', '9000000001', '2023-04-06 21:36:04.449000', '9000000001', '19170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19180', '9000000001', '2023-04-06 21:36:10.450000', '9000000001', '19170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/98cc4406-7865-4559-9ef1-651b225ddfcb.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19181', '4', '2023-04-06 21:36:10.469000', '4', '19170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19187', '9000000001', '2023-04-06 21:36:54.144000', '9000000001', '19184', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담대기)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19188', '4', '2023-04-06 21:36:55.664000', '16926', '19184', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-06T12:36:55.616Z"
  }
}', NULL, 'receive'), 
  ('19189', '9000000001', '2023-04-06 21:36:55.682000', '9000000001', '19184', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "시스템 자동 메시지 (상담연결)"
    } ]
  } ]
}', NULL, 'send'), 
  ('19190', '9000000001', '2023-04-06 21:37:03.271000', '9000000001', '19184', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/7fb9eefa-2219-47bf-8f84-786d681b1a8a.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19191', '4', '2023-04-06 21:37:03.289000', '4', '19184', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19192', '9000000001', '2023-04-06 21:42:31.728000', '9000000001', '19184', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19195', '9000000001', '2023-04-06 21:47:32.599000', '9000000001', '19184', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19201', '9000000001', '2023-04-06 22:02:25.764000', '9000000001', '19198', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19202', '9000000001', '2023-04-06 22:02:36.375000', '9000000001', '19198', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19203', '4', '2023-04-06 22:02:36.423000', '4', '19198', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "hh"
    } ]
  } ]
}', NULL, 'send'), 
  ('19204', '9000000001', '2023-04-06 22:08:05.416000', '9000000001', '19198', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19207', '9000000001', '2023-04-06 22:13:05.813000', '9000000001', '19198', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19216', '9000000001', '2023-04-07 12:41:19.738000', '9000000001', '19213', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19217', '9000000001', '2023-04-07 12:48:24.314000', '9000000001', '19213', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19222', '9000000001', '2023-04-07 12:57:39.791000', '9000000001', '19219', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19223', '9000000001', '2023-04-07 13:04:55.483000', '9000000001', '19219', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19265', '9000000001', '2023-04-07 16:27:05.036000', '9000000001', '19219', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19266', '4', '2023-04-07 16:27:05.107000', '4', '19219', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "2010년 KEP 신용대출 프로세스"
    } ]
  } ]
}', NULL, 'send'), 
  ('19268', '9000000001', '2023-04-07 16:32:26.788000', '9000000001', '19219', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19271', '9000000001', '2023-04-07 16:37:27.088000', '9000000001', '19219', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19328', '9000000001', '2023-04-07 18:53:41.309000', '9000000001', '19324', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19329', '9000000001', '2023-04-07 18:54:09.305000', '9000000001', '19324', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19330', '4', '2023-04-07 18:54:09.383000', '4', '19324', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어허라"
    } ]
  } ]
}', NULL, 'send'), 
  ('19332', '4', '2023-04-07 18:54:23.102000', '16091', '19324', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "앓"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-07T09:54:22.996Z"
  }
}', NULL, 'receive'), 
  ('19345', '9000000001', '2023-04-09 15:54:03.696000', '9000000001', '19337', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('19348', '9000000001', '2023-04-09 15:54:26.790000', '9000000001', '19346', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('19351', '9000000001', '2023-04-09 15:54:32.307000', '9000000001', '19349', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('19358', '9000000001', '2023-04-09 15:55:04.711000', '9000000001', '19352', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_answer"
    } ]
  } ]
}', NULL, 'send'), 
  ('19385', '9000000001', '2023-04-10 09:22:15.100000', '9000000001', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19386', '4', '2023-04-10 09:22:23.772000', '15606', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "시작"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T00:22:23.718Z"
  }
}', NULL, 'receive'), 
  ('19387', '9000000001', '2023-04-10 09:22:23.790000', '9000000001', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19388', '9000000001', '2023-04-10 09:22:23.822000', '9000000001', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('19389', '9000000001', '2023-04-10 09:29:52.623000', '9000000001', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19400', '9000000001', '2023-04-10 10:07:15.466000', '9000000001', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19401', '4', '2023-04-10 10:07:15.547000', '4', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹"
    } ]
  } ]
}', NULL, 'send'), 
  ('19402', '4', '2023-04-10 10:07:21.277000', '4', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19403', '4', '2023-04-10 10:07:29.949000', '15606', '19382', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종료할게요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:07:29.771Z"
  }
}', NULL, 'receive'), 
  ('19408', '9000000001', '2023-04-10 10:20:02.402000', '9000000001', '19405', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19409', '4', '2023-04-10 10:20:09.085000', '15606', '19405', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오잇"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:20:09.002Z"
  }
}', NULL, 'receive'), 
  ('19410', '9000000001', '2023-04-10 10:20:09.106000', '9000000001', '19405', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19423', '9000000001', '2023-04-10 10:36:51.052000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19424', '5', '2023-04-10 10:37:15.352000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "냐"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:37:15.269Z"
  }
}', NULL, 'receive'), 
  ('19425', '9000000001', '2023-04-10 10:37:15.370000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19426', '5', '2023-04-10 10:37:44.427000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아몬드"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:37:44.3Z"
  }
}', NULL, 'receive'), 
  ('19427', '9000000001', '2023-04-10 10:37:44.446000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19428', '5', '2023-04-10 10:37:55.107000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "바나나"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:37:55.048Z"
  }
}', NULL, 'receive'), 
  ('19429', '9000000001', '2023-04-10 10:37:55.127000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19430', '5', '2023-04-10 10:37:57.241000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "딸기"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:37:57.204Z"
  }
}', NULL, 'receive'), 
  ('19431', '9000000001', '2023-04-10 10:37:57.263000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19432', '5', '2023-04-10 10:39:58.125000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:39:58.076Z"
  }
}', NULL, 'receive'), 
  ('19433', '9000000001', '2023-04-10 10:39:58.146000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19434', '9000000001', '2023-04-10 10:40:59.577000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19435', '5', '2023-04-10 10:40:59.625000', '5', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담사"
    } ]
  } ]
}', NULL, 'send'), 
  ('19436', '9000000001', '2023-04-10 10:46:06.730000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19438', '5', '2023-04-10 10:46:16.415000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잠시만요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:46:16.346Z"
  }
}', NULL, 'receive'), 
  ('19439', '5', '2023-04-10 10:52:39.100000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T01:52:39.025Z"
  }
}', NULL, 'receive'), 
  ('19442', '5', '2023-04-10 10:56:52.885000', '5', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대답"
    } ]
  } ]
}', NULL, 'send'), 
  ('19443', '9000000001', '2023-04-10 11:02:19.041000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19446', '5', '2023-04-10 11:04:07.287000', '5', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "옥수수"
    } ]
  } ]
}', NULL, 'send'), 
  ('19447', '5', '2023-04-10 11:04:19.833000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "옥수수"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T02:04:19.742Z"
  }
}', NULL, 'receive'), 
  ('19456', '9000000001', '2023-04-10 11:11:26.321000', '9000000001', '19453', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19457', '4', '2023-04-10 11:11:31.450000', '15606', '19453', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T02:11:31.392Z"
  }
}', NULL, 'receive'), 
  ('19458', '9000000001', '2023-04-10 11:11:31.469000', '9000000001', '19453', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19459', '4', '2023-04-10 11:11:33.550000', '15606', '19453', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아아ㅏㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T02:11:33.452Z"
  }
}', NULL, 'receive'), 
  ('19460', '9000000001', '2023-04-10 11:11:33.575000', '9000000001', '19453', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19466', '9000000001', '2023-04-10 11:17:23.477000', '9000000001', '19463', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19467', '5', '2023-04-10 11:18:08.176000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "옥수수"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T02:18:06.082Z"
  }
}', NULL, 'receive'), 
  ('19468', '5', '2023-04-10 11:18:36.793000', '15859', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "옥수수"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T02:18:36.727Z"
  }
}', NULL, 'receive'), 
  ('19470', '9000000001', '2023-04-10 11:24:50.414000', '9000000001', '19463', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19505', '9000000001', '2023-04-10 13:56:02.520000', '9000000001', '19484', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19506', '4', '2023-04-10 13:56:07.174000', '15606', '19484', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T04:56:07.133Z"
  }
}', NULL, 'receive'), 
  ('19507', '9000000001', '2023-04-10 13:56:07.195000', '9000000001', '19484', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19508', '9000000001', '2023-04-10 13:56:12.753000', '9000000001', '19484', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19509', '4', '2023-04-10 13:56:12.779000', '4', '19484', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19515', '9000000001', '2023-04-10 13:56:25.060000', '9000000001', '19510', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19516', '5', '2023-04-10 13:56:25.983000', '15606', '19510', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "다시요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T04:56:22.774Z"
  }
}', NULL, 'receive'), 
  ('19517', '9000000001', '2023-04-10 13:56:26.003000', '9000000001', '19510', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19518', '5', '2023-04-10 13:57:15.459000', '15606', '19510', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네''"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T04:57:15.418Z"
  }
}', NULL, 'receive'), 
  ('19519', '9000000001', '2023-04-10 13:57:15.475000', '9000000001', '19510', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19524', '9000000001', '2023-04-10 13:57:35.559000', '9000000001', '19521', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19525', '9000000001', '2023-04-10 14:01:38.886000', '9000000001', '19521', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19526', '4', '2023-04-10 14:01:38.912000', '4', '19521', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19527', '4', '2023-04-10 14:01:50.218000', '4', '19521', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담을 시작하겠습니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('19528', '9000000001', '2023-04-10 14:06:54.577000', '9000000001', '19521', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19531', '9000000001', '2023-04-10 14:12:22.781000', '9000000001', '19521', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19533', '9000000001', '2023-04-10 14:53:55.718000', '9000000001', '19463', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19534', '4', '2023-04-10 14:53:55.767000', '4', '19463', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "얩"
    } ]
  } ]
}', NULL, 'send'), 
  ('19539', '9000000001', '2023-04-10 14:54:11.716000', '9000000001', '19536', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19540', '4', '2023-04-10 14:54:19.812000', '15606', '19536', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "예?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T05:54:19.684Z"
  }
}', NULL, 'receive'), 
  ('19541', '9000000001', '2023-04-10 14:54:19.831000', '9000000001', '19536', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19542', '9000000001', '2023-04-10 14:54:29.493000', '9000000001', '19536', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19543', '4', '2023-04-10 14:54:29.513000', '4', '19536', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19544', '4', '2023-04-10 14:54:38.772000', '15606', '19536', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T05:54:38.736Z"
  }
}', NULL, 'receive'), 
  ('19545', '9000000001', '2023-04-10 14:58:58.385000', '9000000001', '19463', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19548', '9000000001', '2023-04-10 15:04:00.262000', '9000000001', '19463', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19557', '4', '2023-04-10 16:24:50.140000', '4', '19536', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종료하시죠"
    } ]
  } ]
}', NULL, 'send'), 
  ('19563', '9000000001', '2023-04-10 16:26:02.647000', '9000000001', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19564', '5', '2023-04-10 16:26:03.054000', '15606', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T07:26:01.888Z"
  }
}', NULL, 'receive'), 
  ('19565', '9000000001', '2023-04-10 16:26:03.073000', '9000000001', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19566', '5', '2023-04-10 16:26:28.299000', '15606', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T07:26:28.249Z"
  }
}', NULL, 'receive'), 
  ('19567', '9000000001', '2023-04-10 16:26:28.318000', '9000000001', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19568', '9000000001', '2023-04-10 16:26:53.834000', '9000000001', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19569', '5', '2023-04-10 16:26:53.857000', '5', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ]
}', NULL, 'send'), 
  ('19571', '9000000001', '2023-04-10 16:32:08.951000', '9000000001', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19583', '9000000001', '2023-04-10 16:37:09.232000', '9000000001', '19558', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19651', '9000000001', '2023-04-10 18:59:47.283000', '9000000001', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19652', '9000000001', '2023-04-10 19:00:06.979000', '9000000001', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19653', '4', '2023-04-10 19:00:07.012000', '4', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19654', '4', '2023-04-10 19:00:22.412000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "갗은"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:00:21.887Z"
  }
}', NULL, 'receive'), 
  ('19655', '4', '2023-04-10 19:00:25.110000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "같은"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:00:25.046Z"
  }
}', NULL, 'receive'), 
  ('19656', '4', '2023-04-10 19:01:08.923000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대녀"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:01:08.876Z"
  }
}', NULL, 'receive'), 
  ('19657', '4', '2023-04-10 19:01:22.063000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대녀"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:01:22.011Z"
  }
}', NULL, 'receive'), 
  ('19658', '4', '2023-04-10 19:01:30.345000', '4', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대녀"
    } ]
  } ]
}', NULL, 'send'), 
  ('19659', '4', '2023-04-10 19:01:40.268000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대녀"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:01:40.187Z"
  }
}', NULL, 'receive'), 
  ('19660', '4', '2023-04-10 19:02:14.187000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "닥전"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:02:12.817Z"
  }
}', NULL, 'receive'), 
  ('19661', '4', '2023-04-10 19:02:35.247000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "닥전"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:02:35.186Z"
  }
}', NULL, 'receive'), 
  ('19662', '4', '2023-04-10 19:03:08.528000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "닥전"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:03:08.461Z"
  }
}', NULL, 'receive'), 
  ('19663', '4', '2023-04-10 19:05:32.880000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "닥전"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:05:32.795Z"
  }
}', NULL, 'receive'), 
  ('19664', '4', '2023-04-10 19:05:43.919000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "옥수수"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:05:43.862Z"
  }
}', NULL, 'receive'), 
  ('19665', '4', '2023-04-10 19:05:59.031000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "닥젅"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:05:58.964Z"
  }
}', NULL, 'receive'), 
  ('19666', '4', '2023-04-10 19:06:00.496000', '15659', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "닥전"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:06:00.457Z"
  }
}', NULL, 'receive'), 
  ('19667', '9000000001', '2023-04-10 19:06:33.766000', '9000000001', '19648', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('19673', '9000000001', '2023-04-10 19:07:16.536000', '9000000001', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19674', '4', '2023-04-10 19:07:35.560000', '15659', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "닥전"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:07:35.498Z"
  }
}', NULL, 'receive'), 
  ('19675', '9000000001', '2023-04-10 19:07:35.592000', '9000000001', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19676', '9000000001', '2023-04-10 19:14:48.338000', '9000000001', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19678', '9000000001', '2023-04-10 19:19:33.765000', '9000000001', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19679', '4', '2023-04-10 19:19:33.799000', '4', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dkdk"
    } ]
  } ]
}', NULL, 'send'), 
  ('19680', '4', '2023-04-10 19:19:40.217000', '15659', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "닥전"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:19:40.124Z"
  }
}', NULL, 'receive'), 
  ('19681', '4', '2023-04-10 19:21:27.380000', '15659', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "및친"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:21:27.302Z"
  }
}', NULL, 'receive'), 
  ('19689', '4', '2023-04-10 19:53:04.157000', '15659', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:53:03.991Z"
  }
}', NULL, 'receive'), 
  ('19690', '4', '2023-04-10 19:53:12.387000', '15659', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1은 무언가 잘못되었다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T10:53:12.315Z"
  }
}', NULL, 'receive'), 
  ('19806', '4', '2023-04-10 20:56:06.533000', '4', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오계정의 내 정보에서 입력하거나 수정한 생년월일과 성별 정보는 헤이카카오 앱의 [사이드 메뉴 > 내 정보]에 표시됩니다.\n\n※ 헤이카카오 앱에서 내 정보를 수정하려면 최신 버전으로 앱을 업데이트해주세요. 헤이카카오 앱 버전이 1.6.0 이상일 때에만 프로필을 직접 수정할 수 있습니다.\n※ 카카오계정의 내 정보는 [카카오톡 > 내 프로필 > 카카오계정 > 내 정보 관리]에서도 확인하고 수정하실 수 있습니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19808', '9000000001', '2023-04-10 21:01:22.325000', '9000000001', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19810', '4', '2023-04-10 21:01:32.673000', '15659', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅎ 호호"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-10T12:01:31.064Z"
  }
}', NULL, 'receive'), 
  ('19819', '9000000001', '2023-04-11 09:04:33.555000', '9000000001', '19816', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19824', '9000000001', '2023-04-11 09:05:54.688000', '9000000001', '19821', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19825', '4', '2023-04-11 09:06:00.456000', '15606', '19821', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T00:06:00.398Z"
  }
}', NULL, 'receive'), 
  ('19826', '9000000001', '2023-04-11 09:06:00.477000', '9000000001', '19821', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19827', '9000000001', '2023-04-11 09:06:00.509000', '9000000001', '19821', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('19828', '9000000001', '2023-04-11 09:06:10.830000', '9000000001', '19821', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19829', '4', '2023-04-11 09:06:10.875000', '4', '19821', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네네"
    } ]
  } ]
}', NULL, 'send'), 
  ('19830', '4', '2023-04-11 09:06:29.701000', '15606', '19821', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종료합니다!"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T00:06:29.659Z"
  }
}', NULL, 'receive'), 
  ('19831', '9000000001', '2023-04-11 09:06:29.729000', '9000000001', '19821', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('19837', '9000000001', '2023-04-11 09:23:25.081000', '9000000001', '19834', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19838', '4', '2023-04-11 09:23:40.306000', '15606', '19834', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T00:23:40.204Z"
  }
}', NULL, 'receive'), 
  ('19839', '9000000001', '2023-04-11 09:23:40.326000', '9000000001', '19834', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19840', '9000000001', '2023-04-11 09:23:40.356000', '9000000001', '19834', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('19841', '9000000001', '2023-04-11 09:23:46.833000', '9000000001', '19834', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19842', '4', '2023-04-11 09:23:46.862000', '4', '19834', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('19843', '9000000001', '2023-04-11 09:28:54.476000', '9000000001', '19834', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19846', '9000000001', '2023-04-11 09:33:56.390000', '9000000001', '19834', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19851', '9000000001', '2023-04-11 10:10:33.423000', '9000000001', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19852', '4', '2023-04-11 10:11:01.687000', '16926', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:11:01.581Z"
  }
}', NULL, 'receive'), 
  ('19853', '9000000001', '2023-04-11 10:11:01.713000', '9000000001', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19855', '9000000001', '2023-04-11 10:12:45.294000', '9000000001', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19856', '4', '2023-04-11 10:12:45.345000', '4', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅓㅣㅓ"
    } ]
  } ]
}', NULL, 'send'), 
  ('19857', '9000000001', '2023-04-11 10:17:56.780000', '9000000001', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19859', '4', '2023-04-11 10:18:03.452000', '16926', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:18:03.371Z"
  }
}', NULL, 'receive'), 
  ('19860', '4', '2023-04-11 10:18:04.685000', '16926', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니야 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:18:04.623Z"
  }
}', NULL, 'receive'), 
  ('19861', '4', '2023-04-11 10:18:06.588000', '16926', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아직 아니야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:18:06.101Z"
  }
}', NULL, 'receive'), 
  ('19862', '4', '2023-04-11 10:18:09.393000', '16926', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "좀 더 있어봐"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:18:09.332Z"
  }
}', NULL, 'receive'), 
  ('19863', '9000000001', '2023-04-11 10:18:28.803000', '9000000001', '19848', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('19882', '9000000001', '2023-04-11 10:19:14.994000', '9000000001', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19883', '4', '2023-04-11 10:19:24.274000', '16926', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:19:24.211Z"
  }
}', NULL, 'receive'), 
  ('19884', '9000000001', '2023-04-11 10:19:24.298000', '9000000001', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19885', '9000000001', '2023-04-11 10:19:36.609000', '9000000001', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요. 테스트"
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e597361-d522-480e-bf40-7581b8f6fc37.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19886', '4', '2023-04-11 10:19:36.645000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 고객님 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('19887', '4', '2023-04-11 10:20:03.562000', '16926', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "회사에서 커넥트올웨이즈 도입을 고려하고 있습니다. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:20:03.511Z"
  }
}', NULL, 'receive'), 
  ('19888', '4', '2023-04-11 10:20:16.695000', '16926', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈에 대해 자세히 알려 주실 수 있으실까요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:20:16.637Z"
  }
}', NULL, 'receive'), 
  ('19889', '4', '2023-04-11 10:20:59.264000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈에 관심이 있으시군요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19890', '4', '2023-04-11 10:24:18.544000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 고객이 점차 정보와 신뢰를 얻어서 최종 거래까지 이동할 수 있도록 적절한 커뮤니케이션 채널로 자연스럽게 전이를 유도합니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('19891', '4', '2023-04-11 10:24:56.806000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 대면 대고객 접점에서 일어나는 모든 서비스를 비대면으로 지원하는 비대면 채널 상담 서비스를 지향합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19892', '4', '2023-04-11 10:26:07.160000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 App/Web 채널 구축, 텔레마케팅 대체/보완, 영업/외근직 지원, 1:1 고객 응대등 고객과의 접점에 다양하게 적용 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19893', '4', '2023-04-11 10:27:16.165000', '16926', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어떤 기능들이 있나요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:27:16.06Z"
  }
}', NULL, 'receive'), 
  ('19894', '4', '2023-04-11 10:27:46.842000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "개인 카카오톡을 대체하여 회사 공식 채널을 통해 1:1 상담이 가능하도록 영업 직원 개인별 링크를 제공합니다. <br/>상담 기록이 회사에 저장이 되어 보다 체계적인 고객 관리가 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19895', '4', '2023-04-11 10:28:35.877000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "국내 최고 수준의 자연어 챗봇을 지원합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19896', '4', '2023-04-11 10:28:42.403000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오엔터프라이즈 자연어 챗봇은 Advanced ML기능을 제공하여 사용자의 의도를 정확히 파악해 적절한 답변을 제공합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19897', '4', '2023-04-11 10:29:05.257000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "Kakao i GPT로 기업이 제공하는 문서 내에서 답변하기 때문에 책임있는 서비스 구축이 가능하며, 보안 문서는 내부망에서만 답변을 하도록 분리 운영이 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19898', '4', '2023-04-11 10:29:24.674000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상품 조회/선택은 카카오채널에서 지원하고 거래는 고객사 site를 in-app브라우저로 노출함으로써, <br/>고객은 카카오톡에서 완결된 거래까지 수행한다고 느끼게 됩니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19899', '4', '2023-04-11 10:29:43.927000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "무인상담(챗봇) 중에 언제든지 직원 상담 요청이 가능합니다. <br/>지속적인 1:1 전담 상담직원 매칭으로 대면상담에 준하는 신뢰관계 지원이 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('19900', '4', '2023-04-11 10:29:58.238000', '16926', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "다양하고 파워풀한 기능을 제공하는군요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:29:58.185Z"
  }
}', NULL, 'receive'), 
  ('19901', '4', '2023-04-11 10:30:05.970000', '16926', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "내부적으로 검토해보고 다시 연락드려도 될까요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:30:05.876Z"
  }
}', NULL, 'receive'), 
  ('19902', '4', '2023-04-11 10:30:40.731000', '4', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 언제든 편하신 시간에 연락주세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('19903', '9000000001', '2023-04-11 10:35:57.627000', '9000000001', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19909', '9000000001', '2023-04-11 10:40:57.939000', '9000000001', '19867', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19916', '9000000001', '2023-04-11 10:48:38.096000', '9000000001', '19913', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19917', '4', '2023-04-11 10:49:03.422000', '16926', '19913', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T01:49:03.374Z"
  }
}', NULL, 'receive'), 
  ('19918', '9000000001', '2023-04-11 10:49:03.440000', '9000000001', '19913', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19919', '9000000001', '2023-04-11 10:49:11.699000', '9000000001', '19913', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈 전문 상담원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/037a8045-2322-47a0-a76e-efa4668f898f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19920', '4', '2023-04-11 10:49:11.718000', '4', '19913', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 고객님"
    } ]
  } ]
}', NULL, 'send'), 
  ('19922', '9000000001', '2023-04-11 10:51:25.035000', '9000000001', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19923', '9000000001', '2023-04-11 10:54:28.377000', '9000000001', '19913', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('19928', '9000000001', '2023-04-11 10:59:28.720000', '9000000001', '19913', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('19938', '9000000001', '2023-04-11 11:00:44.837000', '9000000001', '19935', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19945', '9000000001', '2023-04-11 11:08:01.024000', '9000000001', '19935', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('19963', '9000000001', '2023-04-11 15:43:48.308000', '9000000001', '19960', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('19964', '4', '2023-04-11 15:43:54.388000', '15606', '19960', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트입니다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T06:43:54.331Z"
  }
}', NULL, 'receive'), 
  ('19965', '9000000001', '2023-04-11 15:43:54.406000', '9000000001', '19960', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('19966', '9000000001', '2023-04-11 15:44:09.169000', '9000000001', '19960', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈 전문 상담원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/037a8045-2322-47a0-a76e-efa4668f898f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('19967', '4', '2023-04-11 15:44:09.191000', '4', '19960', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20012', '9000000001', '2023-04-11 17:05:43.597000', '9000000001', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20021', '4', '2023-04-11 17:07:17.270000', '16926', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T08:07:17.212Z"
  }
}', NULL, 'receive'), 
  ('20022', '9000000001', '2023-04-11 17:07:17.292000', '9000000001', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20027', '9000000001', '2023-04-11 17:07:51.941000', '9000000001', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈 전문 상담원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/037a8045-2322-47a0-a76e-efa4668f898f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20028', '4', '2023-04-11 17:07:51.959000', '4', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20029', '4', '2023-04-11 17:08:08.797000', '16926', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈 도입 관련하여 상담하고 싶어요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T08:08:08.735Z"
  }
}', NULL, 'receive'), 
  ('20030', '4', '2023-04-11 17:08:25.493000', '4', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "감사합니다 고객님 도입을 고려하고 계신군요 어떤 내용이 궁굼하실까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20031', '4', '2023-04-11 17:08:32.260000', '16926', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "전반적인 설명 부탁드려요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-11T08:08:32.199Z"
  }
}', NULL, 'receive'), 
  ('20054', '9000000001', '2023-04-12 09:01:30.154000', '9000000001', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20055', '9000000001', '2023-04-12 09:01:38.742000', '9000000001', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20056', '3', '2023-04-12 09:01:38.789000', '3', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕ㅘ세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20057', '3', '2023-04-12 09:01:41.439000', '15606', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T00:01:41.385Z"
  }
}', NULL, 'receive'), 
  ('20058', '9000000001', '2023-04-12 09:01:41.464000', '9000000001', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20059', '3', '2023-04-12 09:01:47.187000', '3', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트 종료합니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('20060', '3', '2023-04-12 09:01:49.997000', '15606', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "!테스트"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T00:01:49.94Z"
  }
}', NULL, 'receive'), 
  ('20061', '9000000001', '2023-04-12 09:01:50.029000', '9000000001', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20062', '3', '2023-04-12 09:02:10.788000', '15606', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅌ[ㅅ,ㅌ,"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T00:02:10.735Z"
  }
}', NULL, 'receive'), 
  ('20063', '9000000001', '2023-04-12 09:02:10.820000', '9000000001', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20064', '3', '2023-04-12 09:02:15.392000', '15606', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T00:02:15.356Z"
  }
}', NULL, 'receive'), 
  ('20065', '9000000001', '2023-04-12 09:02:15.415000', '9000000001', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20066', '3', '2023-04-12 09:02:18.806000', '15606', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "종료"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T00:02:18.756Z"
  }
}', NULL, 'receive'), 
  ('20067', '9000000001', '2023-04-12 09:02:18.831000', '9000000001', '20039', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20072', '9000000001', '2023-04-12 09:02:30.614000', '9000000001', '20069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20073', '3', '2023-04-12 09:02:33.630000', '15606', '20069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕ㅘ세료"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T00:02:33.568Z"
  }
}', NULL, 'receive'), 
  ('20074', '9000000001', '2023-04-12 09:02:33.649000', '9000000001', '20069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20075', '9000000001', '2023-04-12 09:02:33.679000', '9000000001', '20069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20076', '9000000001', '2023-04-12 09:02:38.239000', '9000000001', '20069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20077', '3', '2023-04-12 09:02:38.263000', '3', '20069', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('20080', '4', '2023-04-12 09:42:24.706000', '4', '20009', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "spsp"
    } ]
  } ]
}', NULL, 'send'), 
  ('20084', '9000000001', '2023-04-12 11:08:57.162000', '9000000001', '19670', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20090', '9000000001', '2023-04-12 13:28:51.707000', '9000000001', '20087', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20091', '4', '2023-04-12 13:28:56.650000', '15606', '20087', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담중"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T04:28:56.598Z"
  }
}', NULL, 'receive'), 
  ('20092', '9000000001', '2023-04-12 13:28:56.668000', '9000000001', '20087', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20097', '9000000001', '2023-04-12 13:29:20.829000', '9000000001', '20094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20098', '4', '2023-04-12 13:29:23.978000', '15606', '20094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T04:29:23.913Z"
  }
}', NULL, 'receive'), 
  ('20099', '9000000001', '2023-04-12 13:29:23.995000', '9000000001', '20094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20100', '9000000001', '2023-04-12 13:36:27.686000', '9000000001', '20094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('20102', '9000000001', '2023-04-12 13:36:51.941000', '9000000001', '20094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈 전문 상담원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/037a8045-2322-47a0-a76e-efa4668f898f.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20103', '4', '2023-04-12 13:36:51.965000', '4', '20094', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네ㅔㅂ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20115', '9000000001', '2023-04-12 15:29:04.298000', '9000000001', '19125', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/476eabf6-c3a7-4ca6-8672-fa2ec7ddcd1f.jpeg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20116', '4', '2023-04-12 15:29:04.320000', '4', '19125', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "누구세요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20117', '9000000001', '2023-04-12 15:29:10.654000', '9000000001', '19125', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('20120', '4', '2023-04-12 15:29:21.851000', '4', '19125', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄴㅇㄹㅁㅇㄹㄹㅁㄴ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20121', '9000000001', '2023-04-12 15:34:21.095000', '9000000001', '19125', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20126', '9000000001', '2023-04-12 15:42:24.556000', '9000000001', '20123', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20132', '9000000001', '2023-04-12 15:43:52.906000', '9000000001', '20129', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20133', '4', '2023-04-12 15:44:03.085000', '16926', '20129', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T06:44:03.03Z"
  }
}', NULL, 'receive'), 
  ('20134', '9000000001', '2023-04-12 15:44:03.108000', '9000000001', '20129', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20135', '9000000001', '2023-04-12 15:44:10.584000', '9000000001', '20129', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/4e038f36-c8e7-4549-8961-9645a2fe749c.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20136', '4', '2023-04-12 15:44:10.605000', '4', '20129', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20143', '9000000001', '2023-04-12 15:47:22.118000', '9000000001', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20144', '4', '2023-04-12 15:47:27.206000', '16926', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T06:47:27.138Z"
  }
}', NULL, 'receive'), 
  ('20145', '9000000001', '2023-04-12 15:47:27.227000', '9000000001', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20146', '9000000001', '2023-04-12 15:47:34.883000', '9000000001', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20147', '4', '2023-04-12 15:47:34.902000', '4', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20148', '9000000001', '2023-04-12 15:52:56.427000', '9000000001', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20150', '4', '2023-04-12 15:54:38.831000', '16926', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈에 대해 자세히 설명해 주세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T06:54:38.749Z"
  }
}', NULL, 'receive'), 
  ('20151', '4', '2023-04-12 15:54:58.087000', '16926', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "회사에서 도입을 고려하고 있습니다. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T06:54:58.045Z"
  }
}', NULL, 'receive'), 
  ('20152', '4', '2023-04-12 15:55:14.904000', '16926', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "각 기능에 대해서 상세하게 설명해 주시면 많은 도움이 될것 같아요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T06:55:14.867Z"
  }
}', NULL, 'receive'), 
  ('20153', '4', '2023-04-12 15:58:45.730000', '4', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 고객이 점차 정보와 신뢰를 얻어서 최종 거래까지 이동할 수 있도록 적절한 커뮤니케이션 채널로 자연스럽게 전이를 유도합니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('20154', '4', '2023-04-12 15:58:47.630000', '4', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 대면 대고객 접점에서 일어나는 모든 서비스를 비대면으로 지원하는 비대면 채널 상담 서비스를 지향합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20155', '4', '2023-04-12 15:58:49.732000', '4', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 App/Web 채널 구축, 텔레마케팅 대체/보완, 영업/외근직 지원, 1:1 고객 응대등 고객과의 접점에 다양하게 적용 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20156', '4', '2023-04-12 15:59:10.882000', '16926', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "감사합니다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T06:59:10.801Z"
  }
}', NULL, 'receive'), 
  ('20157', '4', '2023-04-12 15:59:16.990000', '16926', '20140', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "내부 검토이후 다시 연락드리겠습니다. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T06:59:16.942Z"
  }
}', NULL, 'receive'), 
  ('20162', '9000000001', '2023-04-12 16:02:27.255000', '9000000001', '20159', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20163', '4', '2023-04-12 16:02:37.889000', '16926', '20159', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:02:37.822Z"
  }
}', NULL, 'receive'), 
  ('20164', '9000000001', '2023-04-12 16:02:37.913000', '9000000001', '20159', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20165', '9000000001', '2023-04-12 16:02:42.564000', '9000000001', '20159', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20166', '4', '2023-04-12 16:02:42.586000', '4', '20159', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 고객님"
    } ]
  } ]
}', NULL, 'send'), 
  ('20167', '4', '2023-04-12 16:02:48.197000', '16926', '20159', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 지난번 커넥트올웨이즈 도입 관련하여 상담을 하였는데요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:02:48.142Z"
  }
}', NULL, 'receive'), 
  ('20168', '4', '2023-04-12 16:02:54.063000', '16926', '20159', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어떤 기능들이 있는지 상세하게 설명 부탁드려요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:02:54.019Z"
  }
}', NULL, 'receive'), 
  ('20173', '9000000001', '2023-04-12 16:09:51.381000', '9000000001', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20174', '4', '2023-04-12 16:10:00.539000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:10:00.476Z"
  }
}', NULL, 'receive'), 
  ('20175', '9000000001', '2023-04-12 16:10:00.557000', '9000000001', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20176', '9000000001', '2023-04-12 16:10:40.394000', '9000000001', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20177', '4', '2023-04-12 16:10:40.422000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 고객님 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20191', '4', '2023-04-12 16:13:12.539000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "회사에서 커넥트올웨이즈 도입을 고려하고 있습니다. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:13:12.482Z"
  }
}', NULL, 'receive'), 
  ('20192', '4', '2023-04-12 16:13:18.035000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈에 대해 자세히 알려주시겠어요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:13:17.967Z"
  }
}', NULL, 'receive'), 
  ('20193', '4', '2023-04-12 16:13:36.687000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈에 대해 관심이 있으시군요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20194', '4', '2023-04-12 16:13:50.020000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 고객이 점차 정보와 신뢰를 얻어서 최종 거래까지 이동할 수 있도록 적절한 커뮤니케이션 채널로 자연스럽게 전이를 유도합니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('20195', '4', '2023-04-12 16:13:52.733000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 대면 대고객 접점에서 일어나는 모든 서비스를 비대면으로 지원하는 비대면 채널 상담 서비스를 지향합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20196', '4', '2023-04-12 16:13:55.110000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 App/Web 채널 구축, 텔레마케팅 대체/보완, 영업/외근직 지원, 1:1 고객 응대등 고객과의 접점에 다양하게 적용 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20197', '4', '2023-04-12 16:14:07.978000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어떤 기능들이 있나요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:14:07.938Z"
  }
}', NULL, 'receive'), 
  ('20198', '4', '2023-04-12 16:14:25.591000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오엔터프라이즈 자연어 챗봇은 Advanced ML기능을 제공하여 사용자의 의도를 정확히 파악해 적절한 답변을 제공합니다."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "Kakao i GPT",
        "data" : "https://kakaoent.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('20200', '4', '2023-04-12 16:14:29.013000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오엔터프라이즈 자연어 챗봇은 Advanced ML기능을 제공하여 사용자의 의도를 정확히 파악해 적절한 답변을 제공합니다."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "쳇봇",
        "data" : "https://kakaoent.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('20202', '4', '2023-04-12 16:14:31.154000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상품 조회/선택은 카카오채널에서 지원하고 거래는 고객사 site를 in-app브라우저로 노출함으로써, 고객은 카카오톡에서 완결된 거래까지 수행한다고 느끼게 됩니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20204', '4', '2023-04-12 16:14:34.664000', '4', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "무인상담(챗봇) 중에 언제든지 직원 상담 요청이 가능합니다. 지속적인 1:1 전담 상담직원 매칭으로 대면상담에 준하는 신뢰관계 지원이 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20206', '4', '2023-04-12 16:14:42.379000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상세한 설명 감사합니다. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:14:42.338Z"
  }
}', NULL, 'receive'), 
  ('20207', '4', '2023-04-12 16:19:58.120000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하나만 더 질문해도 될까요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:19:58.046Z"
  }
}', NULL, 'receive'), 
  ('20208', '9000000001', '2023-04-12 16:19:58.144000', '9000000001', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20209', '4', '2023-04-12 16:22:12.560000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "다시"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:22:12.495Z"
  }
}', NULL, 'receive'), 
  ('20210', '4', '2023-04-12 16:22:13.900000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "다시"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:22:13.86Z"
  }
}', NULL, 'receive'), 
  ('20211', '4', '2023-04-12 16:22:16.857000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "나쁜"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:22:16.785Z"
  }
}', NULL, 'receive'), 
  ('20212', '4', '2023-04-12 16:22:19.118000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:22:19.063Z"
  }
}', NULL, 'receive'), 
  ('20213', '4', '2023-04-12 16:22:22.780000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오늘 다시"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:22:22.722Z"
  }
}', NULL, 'receive'), 
  ('20214', '4', '2023-04-12 16:22:37.508000', '16926', '20170', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "야이 *** *** **** 야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:22:37.463Z"
  }
}', NULL, 'receive'), 
  ('20219', '9000000001', '2023-04-12 16:25:24.851000', '9000000001', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20220', '4', '2023-04-12 16:25:29.834000', '16926', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "야이 ***야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:25:29.77Z"
  }
}', NULL, 'receive'), 
  ('20221', '9000000001', '2023-04-12 16:25:29.852000', '9000000001', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20222', '9000000001', '2023-04-12 16:28:06.619000', '9000000001', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20223', '4', '2023-04-12 16:28:06.641000', '4', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20224', '4', '2023-04-12 16:28:19.727000', '16926', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "*** **** ldkjfalkfdjlafj **** "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:28:19.669Z"
  }
}', NULL, 'receive'), 
  ('20225', '4', '2023-04-12 16:28:26.081000', '16926', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "*** *** **** ㅣ아ㅓㅣ리아ㅓㄹ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:28:26.014Z"
  }
}', NULL, 'receive'), 
  ('20231', '9000000001', '2023-04-12 16:29:07.384000', '9000000001', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20232', '9000000001', '2023-04-12 16:29:18.418000', '9000000001', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20233', '3', '2023-04-12 16:29:18.436000', '3', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 고객님"
    } ]
  } ]
}', NULL, 'send'), 
  ('20234', '3', '2023-04-12 16:29:25.506000', '16926', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넌또 뭐야 ***** "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T07:29:25.444Z"
  }
}', NULL, 'receive'), 
  ('20235', '3', '2023-04-12 16:29:47.876000', '3', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 폭언과 욕설이 반복될 경우 상담을 강제종료 할 수 있습니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20236', '9000000001', '2023-04-12 16:34:52.016000', '9000000001', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20239', '9000000001', '2023-04-12 16:39:52.317000', '9000000001', '20216', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20244', '9000000001', '2023-04-12 18:08:18.698000', '9000000001', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20245', '9000000001', '2023-04-12 18:08:26.626000', '9000000001', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20246', '4', '2023-04-12 18:08:26.681000', '4', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "허이 어"
    } ]
  } ]
}', NULL, 'send'), 
  ('20247', '4', '2023-04-12 18:08:45.192000', '16091', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아이야"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T09:08:44.673Z"
  }
}', NULL, 'receive'), 
  ('20248', '4', '2023-04-12 18:08:51.056000', '4', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그럼 안녕?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20249', '4', '2023-04-12 18:09:22.918000', '4', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이상한게 나오네"
    } ]
  } ]
}', NULL, 'send'), 
  ('20250', '4', '2023-04-12 18:09:30.265000', '16091', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그러게"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T09:09:30.099Z"
  }
}', NULL, 'receive'), 
  ('20251', '4', '2023-04-12 18:10:54.850000', '4', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어이구"
    } ]
  } ]
}', NULL, 'send'), 
  ('20252', '4', '2023-04-12 18:11:00.024000', '16091', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "저이구"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T09:10:59.95Z"
  }
}', NULL, 'receive'), 
  ('20253', '4', '2023-04-12 18:11:04.145000', '4', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아닌데"
    } ]
  } ]
}', NULL, 'send'), 
  ('20254', '4', '2023-04-12 18:11:13.087000', '16091', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "맞는데"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-12T09:11:13.027Z"
  }
}', NULL, 'receive'), 
  ('20256', '9000000001', '2023-04-13 10:31:56.312000', '9000000001', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('20263', '9000000001', '2023-04-13 10:36:15.793000', '9000000001', '19420', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20268', '4', '2023-04-13 10:37:13.331000', '4', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "말씀이 없으셔서 강제종료합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20270', '9000000001', '2023-04-13 10:37:18.132000', '9000000001', '20241', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20276', '9000000001', '2023-04-13 10:38:21.856000', '9000000001', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20281', '4', '2023-04-13 10:40:25.238000', '16926', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T01:40:25.14Z"
  }
}', NULL, 'receive'), 
  ('20282', '9000000001', '2023-04-13 10:40:25.261000', '9000000001', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20283', '9000000001', '2023-04-13 10:40:39.507000', '9000000001', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20284', '4', '2023-04-13 10:40:39.532000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 고객님 무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20289', '4', '2023-04-13 10:41:55.680000', '16926', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "회사에서 커넥트올웨이즈 도입을 고려하고 있습니다. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T01:41:55.62Z"
  }
}', NULL, 'receive'), 
  ('20290', '4', '2023-04-13 10:42:01.338000', '16926', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈에 대해 자세히 알려주시겠어요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T01:42:01.278Z"
  }
}', NULL, 'receive'), 
  ('20291', '4', '2023-04-13 10:42:12.583000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈에 대해 관심이 있으시군요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20292', '4', '2023-04-13 10:42:27.719000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 고객이 점차 정보와 신뢰를 얻어서 최종 거래까지 이동할 수 있도록 적절한 커뮤니케이션 채널로 자연스럽게 전이를 유도합니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('20293', '4', '2023-04-13 10:42:30.224000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 대면 대고객 접점에서 일어나는 모든 서비스를 비대면으로 지원하는 비대면 채널 상담 서비스를 지향합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20294', '4', '2023-04-13 10:42:32.633000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트 올웨이즈는 App/Web 채널 구축, 텔레마케팅 대체/보완, 영업/외근직 지원, 1:1 고객 응대등 고객과의 접점에 다양하게 적용 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20295', '4', '2023-04-13 10:42:40.880000', '16926', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "어떤 기능들이 있나요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T01:42:40.822Z"
  }
}', NULL, 'receive'), 
  ('20296', '4', '2023-04-13 10:42:59.522000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상품 조회/선택은 카카오채널에서 지원하고 거래는 고객사 site를 in-app브라우저로 노출함으로써, 고객은 카카오톡에서 완결된 거래까지 수행한다고 느끼게 됩니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20298', '4', '2023-04-13 10:43:02.386000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오엔터프라이즈 자연어 챗봇은 Advanced ML기능을 제공하여 사용자의 의도를 정확히 파악해 적절한 답변을 제공합니다."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "Kakao i GPT",
        "data" : "https://kakaoent.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('20300', '4', '2023-04-13 10:43:05.400000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오엔터프라이즈 자연어 챗봇은 Advanced ML기능을 제공하여 사용자의 의도를 정확히 파악해 적절한 답변을 제공합니다."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "link",
        "name" : "쳇봇",
        "data" : "https://kakaoent.com/",
        "device_type" : "all"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('20302', '4', '2023-04-13 10:43:08.264000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "무인상담(챗봇) 중에 언제든지 직원 상담 요청이 가능합니다. 지속적인 1:1 전담 상담직원 매칭으로 대면상담에 준하는 신뢰관계 지원이 가능합니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20304', '4', '2023-04-13 10:43:17.511000', '16926', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "다양하고 파워풀한 기능을 제공하는군요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T01:43:17.443Z"
  }
}', NULL, 'receive'), 
  ('20305', '4', '2023-04-13 10:43:22.080000', '16926', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "내부적으로 검토해보고 다시 연락드려도 될까요?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T01:43:22.023Z"
  }
}', NULL, 'receive'), 
  ('20306', '4', '2023-04-13 10:43:29.343000', '4', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 언제든 편하신 시간에 연락주세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20308', '9000000001', '2023-04-13 10:48:43.084000', '9000000001', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20311', '9000000001', '2023-04-13 10:53:43.360000', '9000000001', '20273', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20322', '9000000001', '2023-04-13 11:06:39.145000', '9000000001', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20323', '4', '2023-04-13 11:07:07.428000', '16926', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T02:07:07.349Z"
  }
}', NULL, 'receive'), 
  ('20324', '9000000001', '2023-04-13 11:07:07.451000', '9000000001', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20325', '9000000001', '2023-04-13 11:07:17.843000', '9000000001', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20326', '4', '2023-04-13 11:07:17.875000', '4', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 고객님"
    } ]
  } ]
}', NULL, 'send'), 
  ('20339', '4', '2023-04-13 11:11:11.936000', '4', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "무엇을 도와드릴까요?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20340', '4', '2023-04-13 11:11:19.191000', '16926', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "회사에서 커넥트올웨이즈 도입을 고려하고 있습니다. "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T02:11:19.124Z"
  }
}', NULL, 'receive'), 
  ('20341', '4', '2023-04-13 11:11:24.122000', '16926', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈에 대해 자세히 알려주시겠어요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T02:11:24.051Z"
  }
}', NULL, 'receive'), 
  ('20342', '4', '2023-04-13 11:11:33.440000', '4', '20313', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "커넥트올웨이즈에 관심이 있으시군요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20359', '9000000001', '2023-04-13 14:30:59.084000', '9000000001', '20356', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20360', '4', '2023-04-13 14:31:09.044000', '15606', '20356', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아아"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T05:31:08.998Z"
  }
}', NULL, 'receive'), 
  ('20361', '9000000001', '2023-04-13 14:31:09.065000', '9000000001', '20356', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20362', '9000000001', '2023-04-13 14:31:34.244000', '9000000001', '20356', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20363', '4', '2023-04-13 14:31:34.284000', '4', '20356', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하십니까"
    } ]
  } ]
}', NULL, 'send'), 
  ('20370', '9000000001', '2023-04-13 16:43:58.030000', '9000000001', '20367', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20371', '4', '2023-04-13 16:44:04.506000', '16926', '20367', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T07:44:04.385Z"
  }
}', NULL, 'receive'), 
  ('20372', '9000000001', '2023-04-13 16:44:04.525000', '9000000001', '20367', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20373', '9000000001', '2023-04-13 16:47:39.905000', '9000000001', '20367', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20374', '4', '2023-04-13 16:47:39.931000', '4', '20367', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이루"
    } ]
  } ]
}', NULL, 'send'), 
  ('20375', '4', '2023-04-13 16:48:03.878000', '16926', '20367', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하이루 방가방가"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-13T07:48:03.809Z"
  }
}', NULL, 'receive'), 
  ('20393', '9000000001', '2023-04-14 13:44:06.540000', '9000000001', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20394', '9000000001', '2023-04-14 13:44:19.754000', '9000000001', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20395', '3', '2023-04-14 13:44:19.778000', '3', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20396', '3', '2023-04-14 13:44:22.563000', '3', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20397', '3', '2023-04-14 13:44:24.365000', '3', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "왜두개씩"
    } ]
  } ]
}', NULL, 'send'), 
  ('20398', '3', '2023-04-14 13:44:24.912000', '3', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "나감"
    } ]
  } ]
}', NULL, 'send'), 
  ('20399', '3', '2023-04-14 13:44:38.813000', '15659', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T04:44:38.639Z"
  }
}', NULL, 'receive'), 
  ('20404', '9000000001', '2023-04-14 13:46:43.522000', '9000000001', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20405', '5', '2023-04-14 13:46:46.740000', '20400', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "유채화테스트입니다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T04:46:46.678Z"
  }
}', NULL, 'receive'), 
  ('20406', '9000000001', '2023-04-14 13:46:46.763000', '9000000001', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20408', '9000000001', '2023-04-14 13:47:50.799000', '9000000001', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20409', '3', '2023-04-14 13:47:57.011000', '20400', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T04:47:56.955Z"
  }
}', NULL, 'receive'), 
  ('20410', '9000000001', '2023-04-14 13:47:57.035000', '9000000001', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20411', '9000000001', '2023-04-14 13:47:57.548000', '9000000001', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20412', '3', '2023-04-14 13:47:57.568000', '3', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "fffg"
    } ]
  } ]
}', NULL, 'send'), 
  ('20413', '3', '2023-04-14 13:48:04.580000', '3', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "jhgjhk''"
    } ]
  } ]
}', NULL, 'send'), 
  ('20414', '3', '2023-04-14 13:48:22.826000', '3', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "하하"
    } ]
  } ]
}', NULL, 'send'), 
  ('20415', '3', '2023-04-14 13:48:24.731000', '3', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "호호"
    } ]
  } ]
}', NULL, 'send'), 
  ('20416', '3', '2023-04-14 13:48:34.103000', '3', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "연결 테스트 중"
    } ]
  } ]
}', NULL, 'send'), 
  ('20417', '9000000001', '2023-04-14 13:53:46.362000', '9000000001', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20421', '9000000001', '2023-04-14 13:58:46.644000', '9000000001', '20401', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20425', '9000000001', '2023-04-14 14:24:01.617000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20426', '4', '2023-04-14 14:24:02.604000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "핫핫"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:24:02.56Z"
  }
}', NULL, 'receive'), 
  ('20427', '9000000001', '2023-04-14 14:24:02.626000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20428', '4', '2023-04-14 14:24:06.350000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "핫핫핫"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:24:06.302Z"
  }
}', NULL, 'receive'), 
  ('20429', '9000000001', '2023-04-14 14:24:06.368000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20431', '9000000001', '2023-04-14 14:24:28.163000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20432', '16293', '2023-04-14 14:24:32.966000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "헛핫핫"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:24:32.891Z"
  }
}', NULL, 'receive'), 
  ('20433', '9000000001', '2023-04-14 14:24:32.989000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20434', '16293', '2023-04-14 14:24:35.983000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "히히"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:24:35.936Z"
  }
}', NULL, 'receive'), 
  ('20435', '9000000001', '2023-04-14 14:24:36.002000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20437', '9000000001', '2023-04-14 14:24:42.986000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20438', '16293', '2023-04-14 14:24:52.858000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "히히"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:24:52.816Z"
  }
}', NULL, 'receive'), 
  ('20439', '9000000001', '2023-04-14 14:24:52.882000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20440', '16293', '2023-04-14 14:25:26.167000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅇㅊ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:25:26.132Z"
  }
}', NULL, 'receive'), 
  ('20441', '9000000001', '2023-04-14 14:25:26.185000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20442', '16293', '2023-04-14 14:25:30.710000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇㅊ허"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:25:30.641Z"
  }
}', NULL, 'receive'), 
  ('20443', '9000000001', '2023-04-14 14:25:30.728000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20444', '16293', '2023-04-14 14:25:32.006000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅍ아ㅜㅇ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:25:31.969Z"
  }
}', NULL, 'receive'), 
  ('20445', '9000000001', '2023-04-14 14:25:32.025000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20446', '16293', '2023-04-14 14:25:33.658000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅓ쿵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:25:33.606Z"
  }
}', NULL, 'receive'), 
  ('20447', '9000000001', '2023-04-14 14:25:33.678000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20448', '16293', '2023-04-14 14:25:52.704000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄹ ㄴ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:25:52.638Z"
  }
}', NULL, 'receive'), 
  ('20449', '9000000001', '2023-04-14 14:25:52.722000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20450', '16293', '2023-04-14 14:25:54.826000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㅊㄹ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:25:54.789Z"
  }
}', NULL, 'receive'), 
  ('20451', '9000000001', '2023-04-14 14:25:54.848000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20452', '16293', '2023-04-14 14:26:02.164000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : " ㅎ로ㅓ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:26:02.117Z"
  }
}', NULL, 'receive'), 
  ('20453', '9000000001', '2023-04-14 14:26:02.185000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20454', '16293', '2023-04-14 14:26:04.156000', '20400', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅎㄹ파ㅑ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:26:04.118Z"
  }
}', NULL, 'receive'), 
  ('20455', '9000000001', '2023-04-14 14:26:04.174000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20456', '9000000001', '2023-04-14 14:33:19.165000', '9000000001', '20422', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('20463', '9000000001', '2023-04-14 14:42:46.338000', '9000000001', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20464', '4', '2023-04-14 14:43:05.470000', '16926', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕 방가방가"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:43:05.393Z"
  }
}', NULL, 'receive'), 
  ('20465', '9000000001', '2023-04-14 14:43:05.490000', '9000000001', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20466', '9000000001', '2023-04-14 14:43:15.622000', '9000000001', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20467', '4', '2023-04-14 14:43:15.641000', '4', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "그래 고객 안녕"
    } ]
  } ]
}', NULL, 'send'), 
  ('20468', '4', '2023-04-14 14:43:19.194000', '4', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "뭐 알고싶어"
    } ]
  } ]
}', NULL, 'send'), 
  ('20473', '9000000001', '2023-04-14 14:43:38.188000', '9000000001', '20470', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20474', '4', '2023-04-14 14:43:48.521000', '15606', '20470', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "놓침 테스트"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:43:48.459Z"
  }
}', NULL, 'receive'), 
  ('20475', '9000000001', '2023-04-14 14:43:48.537000', '9000000001', '20470', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20476', '4', '2023-04-14 14:43:54.910000', '15606', '20470', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "놓침 테스트 중입니다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:43:54.851Z"
  }
}', NULL, 'receive'), 
  ('20477', '9000000001', '2023-04-14 14:43:54.927000', '9000000001', '20470', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20482', '9000000001', '2023-04-14 14:44:32.804000', '9000000001', '20479', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20483', '9000000001', '2023-04-14 14:44:40.919000', '9000000001', '20479', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20484', '4', '2023-04-14 14:44:40.939000', '4', '20479', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20485', '4', '2023-04-14 14:44:43.097000', '15606', '20479', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "놓침 테스트 중입니다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:44:43.055Z"
  }
}', NULL, 'receive'), 
  ('20486', '4', '2023-04-14 14:44:48.805000', '4', '20479', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 수고하세요 ~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20487', '4', '2023-04-14 14:44:54.690000', '4', '20479', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "놓침테스트 중입니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('20488', '4', '2023-04-14 14:45:07.264000', '15606', '20479', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:45:07.205Z"
  }
}', NULL, 'receive'), 
  ('20489', '4', '2023-04-14 14:45:20.994000', '16926', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : " 나 말했다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:45:20.958Z"
  }
}', NULL, 'receive'), 
  ('20493', '4', '2023-04-14 14:46:39.124000', '4', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "응 알았더"
    } ]
  } ]
}', NULL, 'send'), 
  ('20501', '9000000001', '2023-04-14 14:49:17.177000', '9000000001', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20502', '3', '2023-04-14 14:50:18.317000', '3', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄲ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20503', '3', '2023-04-14 14:50:33.307000', '15659', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T05:50:32.137Z"
  }
}', NULL, 'receive'), 
  ('20504', '9000000001', '2023-04-14 14:56:20.588000', '9000000001', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 죄송합니다. 현재는 상담직원 응대가 어렵습니다. 상담이 가능할 때 즉시 상담 도와드릴 예정이니 종료하지말고 기다려주세요!"
    } ]
  } ]
}', NULL, 'send'), 
  ('20510', '9000000001', '2023-04-14 16:58:25.924000', '9000000001', '19139', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20511', '5', '2023-04-14 16:58:25.942000', '5', '19139', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20512', '5', '2023-04-14 16:58:33.294000', '5', '19139', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄹㄹㄹㄹ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20513', '5', '2023-04-14 16:59:32.260000', '5', '19139', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄹㄹ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20517', '9000000001', '2023-04-14 17:00:10.534000', '9000000001', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20518', '4', '2023-04-14 17:00:10.884000', '15859', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "상담이"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T08:00:10.832Z"
  }
}', NULL, 'receive'), 
  ('20519', '9000000001', '2023-04-14 17:00:10.903000', '9000000001', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20520', '9000000001', '2023-04-14 17:00:32.936000', '9000000001', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20521', '4', '2023-04-14 17:00:32.953000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('20523', '9000000001', '2023-04-14 17:05:51.511000', '9000000001', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20525', '4', '2023-04-14 17:06:29.105000', '15859', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "있어영"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-14T08:06:29.005Z"
  }
}', NULL, 'receive'), 
  ('20526', '4', '2023-04-14 17:13:32.220000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오오"
    } ]
  } ]
}', NULL, 'send'), 
  ('20527', '4', '2023-04-14 17:13:39.894000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('20528', '4', '2023-04-14 17:16:24.496000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "캬캬"
    } ]
  } ]
}', NULL, 'send'), 
  ('20529', '4', '2023-04-14 17:16:47.051000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅁㄴㅇㅇ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20530', '4', '2023-04-14 17:17:04.232000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅣ;"
    } ]
  } ]
}', NULL, 'send'), 
  ('20531', '4', '2023-04-14 17:17:04.538000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅣ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20532', '4', '2023-04-14 17:17:09.392000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅣ;ㅣ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20533', '9000000001', '2023-04-14 17:22:22.105000', '9000000001', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20535', '4', '2023-04-14 17:22:50.190000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트중입니다"
    } ]
  } ]
}', NULL, 'send'), 
  ('20536', '4', '2023-04-14 17:22:56.561000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오"
    } ]
  } ]
}', NULL, 'send'), 
  ('20537', '4', '2023-04-14 17:23:14.154000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "왜에두번씩??"
    } ]
  } ]
}', NULL, 'send'), 
  ('20538', '4', '2023-04-14 17:23:16.666000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "잉?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20539', '4', '2023-04-14 17:24:53.736000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "오"
    } ]
  } ]
}', NULL, 'send'), 
  ('20540', '4', '2023-04-14 17:25:03.593000', '4', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아아아아아아"
    } ]
  } ]
}', NULL, 'send'), 
  ('20542', '9000000001', '2023-04-14 17:27:22.365000', '9000000001', '20514', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20556', '9000000001', '2023-04-17 09:27:44.476000', '9000000001', '20547', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20557', '9000000001', '2023-04-17 09:27:50.096000', '9000000001', '20547', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20558', '3', '2023-04-17 09:27:50.117000', '3', '20547', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20563', '9000000001', '2023-04-17 09:30:27.674000', '9000000001', '20560', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20564', '9000000001', '2023-04-17 09:30:35.415000', '9000000001', '20560', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20565', '3', '2023-04-17 09:30:35.436000', '3', '20560', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('20570', '9000000001', '2023-04-17 09:36:21.366000', '9000000001', '20567', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20571', '9000000001', '2023-04-17 09:38:06.044000', '9000000001', '20567', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20572', '3', '2023-04-17 09:38:06.067000', '3', '20567', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('20580', '9000000001', '2023-04-17 09:45:30.058000', '9000000001', '20577', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20581', '3', '2023-04-17 09:45:43.498000', '15606', '20577', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "놓침테스트"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-17T00:45:43.428Z"
  }
}', NULL, 'receive'), 
  ('20582', '9000000001', '2023-04-17 09:45:43.518000', '9000000001', '20577', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20583', '9000000001', '2023-04-17 09:45:43.546000', '9000000001', '20577', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20584', '3', '2023-04-17 09:48:31.192000', '3', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ]
}', NULL, 'send'), 
  ('20585', '3', '2023-04-17 09:48:36.796000', '3', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕"
    } ]
  } ]
}', NULL, 'send'), 
  ('20586', '3', '2023-04-17 09:48:48.422000', '15659', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "?"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-17T00:48:47.299Z"
  }
}', NULL, 'receive'), 
  ('20587', '9000000001', '2023-04-17 09:48:48.455000', '9000000001', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20588', '3', '2023-04-17 09:49:15.704000', '3', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지연"
    } ]
  } ]
}', NULL, 'send'), 
  ('20589', '3', '2023-04-17 09:49:27.496000', '15659', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "금지어1"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-17T00:49:27.451Z"
  }
}', NULL, 'receive'), 
  ('20590', '9000000001', '2023-04-17 09:49:27.519000', '9000000001', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20596', '9000000001', '2023-04-17 09:52:14.241000', '9000000001', '20593', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20597', '9000000001', '2023-04-17 09:52:17.752000', '9000000001', '20593', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20598', '3', '2023-04-17 09:52:17.770000', '3', '20593', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "시잣"
    } ]
  } ]
}', NULL, 'send'), 
  ('20599', '3', '2023-04-17 09:52:22.318000', '3', '20593', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "시작"
    } ]
  } ]
}', NULL, 'send'), 
  ('20600', '3', '2023-04-17 09:52:33.400000', '3', '20593', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "대기짧고 상담시간 길게"
    } ]
  } ]
}', NULL, 'send'), 
  ('20601', '3', '2023-04-17 09:52:38.280000', '3', '20593', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트 중"
    } ]
  } ]
}', NULL, 'send'), 
  ('20603', '9000000001', '2023-04-17 10:18:16.708000', '9000000001', '20390', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('20615', '9000000001', '2023-04-17 16:34:17.962000', '9000000001', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20616', '4', '2023-04-17 16:34:28.525000', '15606', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-17T07:34:28.48Z"
  }
}', NULL, 'receive'), 
  ('20617', '9000000001', '2023-04-17 16:34:28.546000', '9000000001', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20618', '9000000001', '2023-04-17 16:34:36.260000', '9000000001', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send');
INSERT INTO 
  issue_log (id, assigner, created, creator, issue_id, payload, relative_id, status) 
VALUES 
  ('20619', '4', '2023-04-17 16:34:36.277000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "네 안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20620', '4', '2023-04-17 16:34:43.912000', '15606', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지연 테스트 중입니다"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-17T07:34:43.876Z"
  }
}', NULL, 'receive'), 
  ('20624', '9000000001', '2023-04-17 16:40:43.879000', '9000000001', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20625', '5', '2023-04-17 16:40:45.996000', '15859', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이얍"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-17T07:40:45.899Z"
  }
}', NULL, 'receive'), 
  ('20626', '9000000001', '2023-04-17 16:40:46.023000', '9000000001', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20627', '9000000001', '2023-04-17 16:41:12.675000', '9000000001', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20628', '5', '2023-04-17 16:41:12.695000', '5', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넴"
    } ]
  } ]
}', NULL, 'send'), 
  ('20629', '5', '2023-04-17 16:41:23.730000', '5', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕라세용"
    } ]
  } ]
}', NULL, 'send'), 
  ('20630', '5', '2023-04-17 16:41:39.349000', '15859', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세용"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-17T07:41:39.251Z"
  }
}', NULL, 'receive'), 
  ('20633', '5', '2023-04-18 09:41:32.253000', '15859', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안뇽"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T00:41:31.032Z"
  }
}', NULL, 'receive'), 
  ('20634', '9000000001', '2023-04-18 09:41:32.317000', '9000000001', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20635', '5', '2023-04-18 09:41:49.637000', '5', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "나"
    } ]
  } ]
}', NULL, 'send'), 
  ('20636', '5', '2023-04-18 09:44:55.719000', '5', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스"
    } ]
  } ]
}', NULL, 'send'), 
  ('20637', '5', '2023-04-18 09:46:26.355000', '5', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "가끔"
    } ]
  } ]
}', NULL, 'send'), 
  ('20638', '5', '2023-04-18 09:46:35.338000', '5', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "??"
    } ]
  } ]
}', NULL, 'send'), 
  ('20639', '5', '2023-04-18 09:46:39.562000', '5', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "히히"
    } ]
  } ]
}', NULL, 'send'), 
  ('20640', '5', '2023-04-18 09:46:41.122000', '5', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "헤헤"
    } ]
  } ]
}', NULL, 'send'), 
  ('20641', '9000000001', '2023-04-18 09:51:45.321000', '9000000001', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20644', '9000000001', '2023-04-18 09:56:45.605000', '9000000001', '20621', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20648', '9000000001', '2023-04-18 10:48:22.748000', '9000000001', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20649', '5', '2023-04-18 10:48:25.210000', '15859', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "이야기"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T01:48:25.145Z"
  }
}', NULL, 'receive'), 
  ('20650', '9000000001', '2023-04-18 10:48:25.230000', '9000000001', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20651', '9000000001', '2023-04-18 10:48:45.638000', '9000000001', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20652', '5', '2023-04-18 10:48:45.702000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "반갑습니다.dddd"
    } ]
  } ]
}', NULL, 'send'), 
  ('20653', '5', '2023-04-18 10:48:49.560000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요???"
    } ]
  } ]
}', NULL, 'send'), 
  ('20654', '5', '2023-04-18 10:49:04.600000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕히 가세요!! 자주지만 수정이 가능"
    } ]
  } ]
}', NULL, 'send'), 
  ('20655', '9000000001', '2023-04-18 10:54:17.134000', '9000000001', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20657', '5', '2023-04-18 10:56:01.453000', '15859', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아냐"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T01:56:01.376Z"
  }
}', NULL, 'receive'), 
  ('20658', '5', '2023-04-18 10:56:04.832000', '15859', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "끊기지마ㅜㅜ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T01:56:04.755Z"
  }
}', NULL, 'receive'), 
  ('20660', '5', '2023-04-18 11:12:52.185000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹넹"
    } ]
  } ]
}', NULL, 'send'), 
  ('20661', '5', '2023-04-18 11:12:59.435000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄴㄴㄴㄴ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20662', '5', '2023-04-18 11:13:01.986000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹ㄴ에"
    } ]
  } ]
}', NULL, 'send'), 
  ('20663', '5', '2023-04-18 11:13:04.575000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넹넹"
    } ]
  } ]
}', NULL, 'send'), 
  ('20664', '3', '2023-04-18 11:13:42.265000', '16926', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요 "
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T02:13:42.191Z"
  }
}', NULL, 'receive'), 
  ('20665', '9000000001', '2023-04-18 11:13:42.284000', '9000000001', '20460', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20681', '9000000001', '2023-04-18 11:14:29.001000', '9000000001', '20666', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20682', '9000000001', '2023-04-18 11:15:00.172000', '9000000001', '20666', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20683', '3', '2023-04-18 11:15:00.190000', '3', '20666', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20684', '9000000001', '2023-04-18 11:18:17.902000', '9000000001', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20686', '3', '2023-04-18 11:18:23.119000', '3', '20666', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "2차 인증은 OM/WM 서비스 사용을 위해 로그인 후 담당자의 이메일 혹은 전화번호로 추가 인증을 거쳐야만 로그인할 수 있는 이중 보안 서비스입니다."
    } ]
  } ]
}', NULL, 'send'), 
  ('20687', '5', '2023-04-18 11:18:26.757000', '15859', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아니되오"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T02:18:26.696Z"
  }
}', NULL, 'receive'), 
  ('20688', '9000000001', '2023-04-18 11:23:48.168000', '9000000001', '20666', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20692', '9000000001', '2023-04-18 11:28:48.426000', '9000000001', '20666', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20706', '9000000001', '2023-04-18 14:01:18.940000', '9000000001', '20703', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20707', '9000000001', '2023-04-18 14:02:12.786000', '9000000001', '20703', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20708', '3', '2023-04-18 14:02:12.855000', '3', '20703', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20709', '3', '2023-04-18 14:02:25.876000', '3', '20703', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "뭐징?"
    } ]
  } ]
}', NULL, 'send'), 
  ('20710', '9000000001', '2023-04-18 14:02:36.035000', '9000000001', '20703', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으신가요? 즉시 상담을 종료하시려면 아래 ‘!종료’ 버튼을 눌러주세요."
    }, {
      "type" : "action",
      "actions" : [ {
        "type" : "message",
        "name" : "!종료"
      } ]
    } ]
  } ]
}', NULL, 'send'), 
  ('20713', '3', '2023-04-18 14:03:24.377000', '15659', '20703', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "싫어용"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T05:03:24.292Z"
  }
}', NULL, 'receive'), 
  ('20722', '9000000001', '2023-04-18 14:55:38.779000', '9000000001', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20723', '9000000001', '2023-04-18 14:56:11.019000', '9000000001', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "판교를 대표하는 보험전문가입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/d52efb1f-9721-41fd-84cd-8a30fa53cdac.jpg",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20724', '5', '2023-04-18 14:56:11.046000', '5', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "dd"
    } ]
  } ]
}', NULL, 'send'), 
  ('20725', '5', '2023-04-18 14:56:18.483000', '15659', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄹㄹ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T05:56:18.438Z"
  }
}', NULL, 'receive'), 
  ('20760', '4', '2023-04-18 19:28:49.335000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ]
}', NULL, 'send'), 
  ('20761', '4', '2023-04-18 19:29:31.082000', '15606', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안녕하세요"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T10:29:29.874Z"
  }
}', NULL, 'receive'), 
  ('20766', '4', '2023-04-18 19:32:28.541000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵 안녕하세요 메모테스트중입니당.."
    } ]
  } ]
}', NULL, 'send'), 
  ('20768', '4', '2023-04-18 19:33:33.107000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1"
    } ]
  } ]
}', NULL, 'send'), 
  ('20769', '4', '2023-04-18 19:35:02.392000', '15606', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고생많으십니다 ㅠㅠ"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-18T10:35:02.292Z"
  }
}', NULL, 'receive'), 
  ('20770', '4', '2023-04-18 19:35:21.144000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "아 저 기준이입니당 ㅎㅎ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20771', '4', '2023-04-18 19:36:50.149000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "확인중"
    } ]
  } ]
}', NULL, 'send'), 
  ('20773', '4', '2023-04-18 19:40:58.728000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1"
    } ]
  } ]
}', NULL, 'send'), 
  ('20774', '4', '2023-04-18 19:41:31.508000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1"
    } ]
  } ]
}', NULL, 'send'), 
  ('20776', '4', '2023-04-18 19:44:01.907000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "3"
    } ]
  } ]
}', NULL, 'send'), 
  ('20778', '4', '2023-04-18 19:46:20.565000', '4', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "1"
    } ]
  } ]
}', NULL, 'send'), 
  ('20780', '5', '2023-04-18 19:50:29.262000', '5', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅅㄷㄴㅅ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20781', '5', '2023-04-18 19:50:41.105000', '5', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㅅㄷㄴㅅ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20782', '5', '2023-04-18 19:50:42.980000', '5', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄷㄷㄷㄷㄷㄷ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20783', '9000000001', '2023-04-18 19:51:25.793000', '9000000001', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20785', '5', '2023-04-18 19:54:29.906000', '5', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ㄴㅇㄹㄴㅇㄹㄴㅇㄹㄴㅇㄹ"
    } ]
  } ]
}', NULL, 'send'), 
  ('20787', '9000000001', '2023-04-18 19:56:26.135000', '9000000001', '20612', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20788', '9000000001', '2023-04-18 19:59:56.286000', '9000000001', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20792', '9000000001', '2023-04-18 20:04:56.606000', '9000000001', '20719', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "고객님 말씀이 없으셔서 다음 상담을 위해 상담을 종료합니다. 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며,\n오늘도 행복한 하루되세요^^"
    } ]
  } ]
}', NULL, 'send'), 
  ('20873', '5', '2023-04-19 09:23:06.632000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "ss"
    } ]
  } ]
}', NULL, 'send'), 
  ('20874', '5', '2023-04-19 09:23:16.018000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "sssss"
    } ]
  } ]
}', NULL, 'send'), 
  ('20875', '5', '2023-04-19 09:23:52.017000', '5', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "테스트"
    } ]
  } ]
}', NULL, 'send'), 
  ('20876', '9000000001', '2023-04-19 09:29:17.637000', '9000000001', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "추가 문의가 없으시면 5분 뒤 상담이 자동 종료됩니다. 궁금하신 내용이 있으시면 말씀해주세요~"
    } ]
  } ]
}', NULL, 'send'), 
  ('20878', '5', '2023-04-19 09:31:26.554000', '15859', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "안대"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-19T00:31:25.313Z"
  }
}', NULL, 'receive'), 
  ('20879', '9000000001', '2023-04-19 09:31:26.671000', '9000000001', '20645', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20887', '9000000001', '2023-04-19 09:59:07.203000', '9000000001', '20884', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "wait"
    } ]
  } ]
}', NULL, 'send'), 
  ('20888', '4', '2023-04-19 09:59:14.956000', '15606', '20884', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "옙"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-19T00:59:14.882Z"
  }
}', NULL, 'receive'), 
  ('20889', '9000000001', '2023-04-19 09:59:14.984000', '9000000001', '20884', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "platform_answer",
      "data" : "no_operator"
    } ]
  } ]
}', NULL, 'send'), 
  ('20890', '9000000001', '2023-04-19 09:59:15.019000', '9000000001', '20884', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "지금은 상담 직원의 사정으로 잠시 상담이 중단 되었거나, 정해진 상담 시간이 아닙니다. 상담이 가능한 시간에 응대하도록 하겠습니다. \n- 평일 AM09:00-PM18:00 / 점심시간 12:00~13:00\n- 주말 AM10:00-PM18:00 / 점심시간 12:00~13:00\n*개인 사정으로 업무 시간에 상담을 잠시 중지할 수도 있습니다. 잠시만 기다려주시면 바로 응대하도록 하겠습니다. "
    } ]
  } ]
}', NULL, 'send'), 
  ('20891', '9000000001', '2023-04-19 09:59:30.808000', '9000000001', '20884', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "카카오i커넥트올웨이즈 전문 상담직원입니다."
    }, {
      "type" : "file",
      "data" : "http://connect-branch-web.kep.k9d.in:8000/upload/image/2023/04/65dafc2a-ab20-4313-a547-6019a715e616.png",
      "display" : "image"
    } ]
  } ]
}', NULL, 'send'), 
  ('20892', '4', '2023-04-19 09:59:30.873000', '4', '20884', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "옙"
    } ]
  } ]
}', NULL, 'send'), 
  ('20894', '4', '2023-04-19 10:00:39.600000', '4', '20884', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ]
}', NULL, 'send'), 
  ('20895', '4', '2023-04-19 10:00:45.077000', '15606', '20884', '{
  "version" : "0.1",
  "chapters" : [ {
    "sections" : [ {
      "type" : "text",
      "data" : "넵"
    } ]
  } ],
  "meta" : {
    "created" : "2023-04-19T01:00:45.003Z"
  }
}', NULL, 'receive');
UNLOCK TABLES;

