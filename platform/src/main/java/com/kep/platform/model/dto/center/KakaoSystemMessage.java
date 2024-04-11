package com.kep.platform.model.dto.center;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoSystemMessage {

    private String code;
    private List<Payload> data;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payload {
        //시스템 메시지 아이디
        private Long id;
        //시스템 메시지 이름
        private String name;
        //메시지 상태(A: 정상, T: 중지, S:차단)
        private String status;
        //등록일
        private String createdAt;
        //수정일
        private String modifiedAt;
        //검수 상태(REG: 등록, REQ: 검수요청, APR: 승인, REJ: 반려)
        private String inspectStatus;
        //검수 요청일
        private String inspectRequestAt;
        //검수일
        private String inspectedAt;
        //시스템 메시지 내용 목록
        private List<Message> messages;
        //검수결과 및 요청사항 목록
        private List<Comment> comments;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Message {
            //시스템 메시지 타입(ST:상담시작, S1:상담불가, S2:상담부재, S3:무응답종료, S4:상담대기, ED:상담종료, ER:응답실패, BL:사용자차단, SB:봇전환)
            private String messageType;
            //내용
            private String content;
            private List<Button> buttons;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Comment {
            //아이디
            private Long id;
            //내용
            private String content;
            //작성자
            private String userName;
            //등록일
            private String createdAt;
            //상태 (APR: 승인, REJ: 반려, INQ:문의)
            private String status;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Button {
            //버튼 노출 순서
            private Long ordering;
            //버튼의 링크타입 (DS: 배송조회, WL: 웹링크, AL: 앱링크, BK: 봇키워드, MD: 메시지전달)
            private String linkType;
            //버튼 이름
            private String linkName;
            //모바일 웹링크주소
            private String linkMobile;
            //PC 웹링크주소
            private String linkPc;
            //IOS 앱링크주소
            private String linkIos;
            //Android 앱링크주소
            private String linkAndroid;
        }

    }
}
