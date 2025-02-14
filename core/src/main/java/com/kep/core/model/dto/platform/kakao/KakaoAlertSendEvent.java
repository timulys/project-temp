package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.platform.BizTalkMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 카카오 알림톡 발송 포맷
 * https://stg-web.bizmsg.kakaoenterprise.com/api-docs/index.html
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAlertSendEvent {
    /**
     * 고객사 ID
     */
    private String clientId;

    /**
     * 알림톡 메시지 타입
     * AT	알림톡
     * AI	이미지 알림톡
     */
    @NotNull
    private BizTalkMessageType messageType;

    /**
     * 메시지 고유 일련번호
     * 일자-일련번호
     */
    private String serialNumber;

    /**
     * 발신 프로필 키
     */
    @NotEmpty
    private String senderKey;

    /**
     * 사용자 전화번호
     */
    @NotEmpty
    @Size(max = 16)
    private String phoneNumber;

    /**
     * 템플릿 코드
     */
    @NotEmpty
    @Size(max = 30)
    private String templateCode;

    /**
     * 메시지 본문
     * 최대 1000자 (공백포함)
     * TODO : 채널추가 버튼이 있는 경우 960자로 제한
     */
    @NotEmpty
    @Size(max = 1000)
    private String message;

    /**
     * 응답 수신 메소드
     * push (권장)
     * polling
     * realtime
     */
    @NotEmpty
    private String responseMethod;

    @NotEmpty
    private List<Message> sendMessages;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        @NotEmpty
        @Size(max = 30)
        private String templateCode;

        /**
         * 사용자 정의 Key Id
         */
        private String cid;

        /**
         * 템플릿 내용 중 강조 표기할 핵심 정보 text(50)
         */
        @Size(max = 50)
        private String kkoTitle;

        @NotEmpty
        @Size(max = 16)
        private String phoneNumber;

        @NotEmpty
        @Size(max = 16)
        private String senderNo;

        @NotEmpty
        @Size(max = 1000)
        private String message;

        private SMSType smsType;

        @Size(max = 2000)
        private String smsMessage;

        @Size(max = 60)
        private String title;

        private Integer timeout;

        private String contentGroupId;

        private String natCd;


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

        private Integer price;

        private String currencyType;

        private List<Button> button;

        private List<QuickReply> quickReply;

        private String header;

        private ItemHighlight itemHighlight;

        private Item item;


        public enum SMSType {
            SM, LM
        }

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Button {

            @NotEmpty
            private String name;

            @NotEmpty
            private ButtonType type;

            private String schemeAndroid;

            private String schemeIos;

            private String urlMobile;

            private String urlPc;

            private String chatExtra;

            private String chatEvent;

            private String pluginId;

            private String relayId;

            private String oneclickId;

            private String productId;

            private String target;

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
            public enum ButtonType {
                AC,
                WL,
                AL,
                DS,
                BK,
                MD,
                BC,
                BT,
                P1,
                P2,
                P3
            }

        }

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class QuickReply {
            @NotEmpty
            private String name;

            @NotEmpty
            private QuickReplyType type;

            private String schemeAndroid;

            private String schemeIos;

            private String urlMobile;

            private String urlPc;

            private String chatExtra;

            private String chatEvent;

            private String target;

            /**
             * <li> WL: 웹링크
             * <li> AL: 앱링크
             * <li> BK: 봇키워드
             * <li> BC: 상담톡전환
             * <li> BT: 봇 전환
             * <li> BF: 비즈니스폼
             */
            public enum QuickReplyType{
                WL, AL, BK, MD, BT, BF, BC
            }
        }

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ItemHighlight {
            @NotEmpty
            private String title;

            private String imageUrl;

            @NotEmpty
            private String description;
        }

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Item {

            @NotEmpty
            private List<ItemList> list;

            @NotEmpty
            private ItemSummary summary;


            @Builder
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ItemList {
                @NotEmpty
                private String title;
                @NotEmpty
                private String description;
            }

            @Builder
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ItemSummary {
                @NotEmpty
                private String title;
                @NotEmpty
                private String description;
            }

        }
    }
}
