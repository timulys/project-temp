package com.kep.portal.config.property;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 시스템 기본 메시지
 */
@ConfigurationProperties(prefix = "system-message")
@Data
public class SystemMessageProperty {

    private Channel channel;

    private Portal portal;

    // todo inner class 제거 예정으로 알고 있음... ( 확정되면 추후 걷어내야 할 수 있는 코드 )
    private ConsultationTalk consultationTalk;
    private Validation validation;

    @Data
    @NoArgsConstructor
    public static class Channel {

        private impossible impossible;

        @Data
        @NoArgsConstructor
        public static class impossible{
            private String message;
        }

        private assignStandby assignStandby;

        @Data
        @NoArgsConstructor
        public static class assignStandby{
            private Integer number;
            private String message;
        }

        private evaluation evaluation;

        @Data
        @NoArgsConstructor
        public static class evaluation{
            private String message;
        }

        private Start start;
        @Data
        @NoArgsConstructor
        public static class Start {

            /**
             * 세션 시작 메시지
             * ex) 상담톡 무조건 시작
             */
            private CodeMessage st;

            /**
             * 상담시작 공통 인사말
             */
            private String welcom;

            /**
             * 배정대기(상담접수) 안내 [S1 상담불가]
             */
            private CodeMessage unable;

            /**
             * 배정대기/상담대기 안내 [S2 상담부재]
             */
            private CodeMessage absence;

            /**
             * [S3 무응답 종료]
             */
            private CodeMessage noResponseEnd;

            /**
             * #상담대기 안내 [S4 상담대기]
             */
            private CodeMessage waiting;
            private String impossible;
            private String assignStandby;
        }

        @Data
        @NoArgsConstructor
        public static class CodeMessage {
            private String code;
            private String message;
        }

        private End end;

        @Data
        @NoArgsConstructor
        public static class End {
            private String register;
            private String memberDelay;
            private String guestDelay;
            private String guestNoticeDelay;
            private Guide guide;

            @Data
            @NoArgsConstructor
            public static class Guide {
                private String message;
                private String noticeMessage;
            }
        }

    }

    @Data
    @NoArgsConstructor
    public static class Portal {

        private Notification notification;

        private Login login;

        private Password password;

        @Data
        @NoArgsConstructor
        public static class Notification {

            private TitleMessage titleMessage;

            @Data
            @NoArgsConstructor
            public static class TitleMessage {

                private String delayIssueRoom;
                private String doneMemberAssignment;
                private String requestMemberTransform;
                private String doneMemberTransform;
                private String referMemberTransform;
                private String failManualMemberTransform;
                private String failAutoMemberTransform;
                private String requestReviewCounsellingDetails;
                private String doneReviewCounsellingDetails;
                private String referReviewCounsellingDetails;
                private String doneConsultationTransfer;
                private String failConsultationTransfer;
                private String endCounsel;
                private String notice;
                private String talkRequestApprove;
                private String talkApprove;
                private String talkReject;
                private String expiredByDuplication;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Login {

            private LoginFailedMessage loginFailedMessage;

            @Data
            @NoArgsConstructor
            public static class LoginFailedMessage {

                private String badCredentials;

                private String disabled;
                
                //2023.1121/YO/ 동시 접속 세션 초과 에러 문구 저장하는 필드
                private String alreadyAccountLoggedIn;	


            }

        }

        @Data
        @NoArgsConstructor
        public static class Password {
            private ChangeMessage changeMessage;

            @Data
            @NoArgsConstructor
            public static class ChangeMessage {
                private String notMatch;

                private String notPasswordRule;

                private String notEqualConfirm;

                private String notSafety;
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class ConsultationTalk {

        private ConsultationTalk.Button button;

        @Data
        @NoArgsConstructor
        public static class Button {
            private String close;
            private String evaluation;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Validation {

        private Validation.Duplication duplication;
        private Validation.Transfer transfer;

        @Data
        @NoArgsConstructor
        public static class Duplication {
            private String branch;
            private String consultationFunnel;
        }

        @Data
        @NoArgsConstructor
        public static class Transfer {
            private String counselingStaff;
        }
    }
}
