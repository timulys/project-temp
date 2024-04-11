package com.kep.core.model.dto.platform.kakao;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.platform.BizTalkMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoFriendSendEvent {

    /**
     * 고객사 ClientId
     */
    private String clientId;

    @NotNull
    private BizTalkMessageType messageType;

    @NotEmpty
    private String senderKey;

    @NotNull
    private List<Message> sendMessages;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message{
        private String cid;

        @Length(max = 50)
        private String title;

        @NotEmpty
        @Length(max = 16)
        private String phoneNumber;

        @NotEmpty
        @Length(max = 16)
        private String senderNo;

        @NotEmpty
        @Length(max = 1000)
        private String message;

        private String smsMessage;

        private SMSType smsType;

        private String userKey;

        private String adFlag;

        private String etc1;
        private String etc2;
        private String etc3;
        private String etc4;
        private String etc5;
        private String etc6;
        private String etc7;
        private String etc8;
        private String etc9;
        private String etc10;

        private String taxCd1;

        private String taxCd2;

        private String contentGroupId;

        private List<Button> button;

        private Image image;

        public enum SMSType {
            SM, LM
        }


        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Button{

            @NotEmpty
            private String name;

            @NotEmpty
            private ButtonType type;

            private String schemeAndroid;

            private String schemeIos;

            private String urlMobile;

            private String urlPc;

            /**
             * 버튼 링크 타입
             * <li> AC : 채널추가(TemplateMessageType의 값이 AD, MI일때 자동 생성됨)
             * <li> WL : 웹링크 - name 필수, linkMo 필수, linkPc 선택
             * <li> AL : 앱링크 - name 필수, linkAnd/linkIos/linkMo 중 2가지 이상 필수, linkPc 선택
             * <li> BK : 봇키워드 - name 필수
             * <li> MD : 메시지전달 - name 필수
             * <li> BC : 상담톡전환 - name 필수
             * <li> BF : 비즈니스폼 biz_form_key or biz_form_id 하나 필수 입력 (친구톡 key만 사용가능, 알림톡 id만 사용가능)
             * <li> BT : 봇전환 - name 필수
             * <li> DS : 배송조회 - name 필수
             * <li> P1 : 이미지 보안 전송 플러그인
             * <li> P2 : 개인정보이용 플러그인
             * <li> P3 : 원클릭 결제 플러그인
             */
            public enum ButtonType{
                AC, WL, AL, BK, MD, BC, BF, BT, DS, P1, P2, P3
            }

        }

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Image{

            @NotEmpty
            private String imgUrl;

            @NotEmpty
            private String imgLink;

        }
    }


}
