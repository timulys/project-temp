package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kep.core.model.dto.platform.BizTalkMessageType;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 카카오 비즈톡 템플릿 포맷
 * <p>
 * https://stg-web.bizmsg.kakaoenterprise.com/api-docs/index.html#
 */
@Schema(description = "카카오 비즈톡 템플릿 포맷")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoBizMessageTemplatePayload {

    /**
     * 템플릿 코드
     */
    @NotEmpty
    @Schema(description = "템플릿 코드")
    private String templateCode;

    /**
     * 템플릿 명
     */
    @Schema(description = "템플릿명")
    private String templateName;

    /**
     * 템플릿 내용
     */
    @Schema(description = "템플릿 내용")
    private String templateContent;

    /**
     * 템플릿 이미지 명(이미지 업로드 메뉴 참조)
     */
    @Schema(description = "템플릿 이미지 명(이미지 업로드 메뉴 참조)")
    private String templateImageName;

    @Schema(description = "템플릿 블록")
    private Boolean templateBlock;
    @Schema(description = "")
    private Boolean templateDormant;

    /**
     * 템플릿 이미지 URL(이미지 업로드 메뉴 참조)
     */
    @URL
    @Schema(description = "템플릿 이미지 URL(이미지 업로드 메뉴 참조)")
    private String templateImageUrl;

    @URL
    @Schema(description = "템플릿 이미지 링크")
    private String templateImageLink;

    @Schema(description = "비즈톡 메시지 타입(알림톡\n" +
            "AT: 알림톡\n" +
            "AI: 알림톡 이미지\n" +
            "SM: SMS\n" +
            "LM: LMS\n" +
            "MM: MMS\n" +
            "SMI: 국제 SMS\n" +
            "친구톡\n" +
            "FT: 친구톡\n" +
            "FI: 친구톡 이미지\n" +
            "FW: 친구톡 와이드 이미지)")
    private BizTalkMessageType bizTalkMessageType;

    @Schema(description = "채널 아이디")
    private String channelId;

    @Schema(description = "셀렉트 채널 아이디")
    private Long selectChannelId;

    @Schema(description = "템플릿 상태")
    private String templateStatus;

    /**
     * 템플릿 카테고리 코드 (참조 : 템플릿 API - 카테고리)
     */
    @Schema(description = "템플릿 카테고리 코드 (참조 : 템플릿 API - 카테고리)")
    private String categoryCode;

    /**
     * 보안 템플릿 여부(Default : false)
     */
    @Pattern(regexp = "true|false")
    @Builder.Default
    @Schema(description = "보안 템플릿 여부(Default : false)")
    private String securityFlag = "false";

    /**
     * 검수 상태
     */
    @Schema(description = "검수 상태")
    private String kepStatus;

    /**
     * 템플릿 메시지 유형(BA: 기본형, EX: 부가 정보형, AD: 광고 추가형, MI: 복합형)
     * EX : templateExtra 필드 필수
     * AD : templateAd 필드 필수, 그룹 템플릿 사용 불가
     * MI : templateExtra, templateAd 필드 필수, 그룹 템플릿 사용 불가
     */
    @NotNull
    @Schema(description = "템플릿 메시지 유형(BA: 기본형, EX: 부가 정보형, AD: 광고 추가형, MI: 복합형)\n" +
            "EX : templateExtra 필드 필수\n" +
            "AD : templateAd 필드 필수, 그룹 템플릿 사용 불가\n" +
            "MI : templateExtra, templateAd 필드 필수, 그룹 템플릿 사용 불가")
    private TemplateMessageType templateMessageType;

    public enum TemplateMessageType {
        BA, EX, AD, MI
    }

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
    public enum LinkType {
        AC, WL, AL, BK, MD, BC, BF, BT, DS, P1, P2, P3
    }

    // ////////////////////////////////////////////////////////////////////////
    // 솔루션 화면용
    // ////////////////////////////////////////////////////////////////////////
    /**
     * 메세지 유형
     */
    @Schema(description = "비즈톡 메시지 템플릿 타입 (TEXT, // 텍스트형\n" +
            "IMAGE, // 이미지형\n" +
            "CAROUSEL, // 캐러셀형\n" +
            "ITEM_LIST, // 아이템 리스트형\n" +
            "NONE,)")
    private KakaoBizMessageTemplateType templateEmphasizeType;

    @Schema(description = "")
    private String templateExtra;
    @Schema(description = "")
    private String templateAd;
    @Schema(description = "")
    private String templateTitle;
    @Schema(description = "")
    private String templateSubtitle;
    @Schema(description = "")
    private List<Button> buttons;
    @Schema(description = "")
    private List<QuickReply> quickReplies;
    @Schema(description = "")
    private String templateHeader;
    @Schema(description = "")
    private TemplateItemHighlight templateItemHighlight;
    @Schema(description = "")
    private TemplateItem templateItem;
    @Schema(description = "")
    private List<TemplateComment> templateComments;


    /**
     * 수정용
     */
    @Schema(description = "")
    private String newTemplateCode;
    @Schema(description = "")
    private String newTemplateName;
    @Schema(description = "")
    private String newTemplateContent;
    @Schema(description = "")
    private String newTemplateImageName;
    @URL
    @Schema(description = "")
    private String newTemplateImageUrl;
    @URL
    @Schema(description = "")
    private String newTemplateImageLink;
    @Schema(description = "")
    private String newSenderKeyType;
    @Schema(description = "")
    private String newCategoryCode;
    @Schema(description = "")
    private KakaoBizMessageTemplateType newTemplateEmphasizeType;
    @Schema(description = "")
    private TemplateMessageType newTemplateMessageType;
    @Schema(description = "")
    private String newTemplateExtra;
    @Schema(description = "")
    private String newTemplateAd;
    @Schema(description = "")
    private String newTemplateTitle;
    @Schema(description = "")
    private String newTemplateSubtitle;
    @Schema(description = "")
    private String newTemplateHeader;
    @Schema(description = "")
    private TemplateItemHighlight newTemplateItemHighlight;
    @Schema(description = "")
    private TemplateItem newTemplateItem;


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Button {
        private Integer ordering;
        private String name;
        private LinkType linkType;
        private String linkTypeName;
        private String linkMo;
        private String linkPc;
        private String linkAnd;
        private String linkIos;
        private String pluginId;

        // 알림톡 변수 정보
        private String chatExtra;
        private String chatEvent;
        private String relayId;
        private String oneclickId;
        private String productId;
        private String target;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuickReply {
        private String name;
        private LinkType linkType;
        private String linkMo;
        private String linkPc;
        private String linkAnd;
        private String linkIos;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateItemHighlight {
        private String title;
        private String description;
        private String imageUrl;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateItem {
        private List<TemplateItemList> list;
        private Summary summary;

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Summary{
            private String title;
            private String description;
        }
        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TemplateItemList {
            private String title;
            private String description;
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateComment {
        private Integer commentSeqno;
        private String commentContent;
        private String commentUserName;
        private String commentCreateAt;
        private String commentStatus;
        private String regBy;
        private String regDate;
        private String updateBy;
        private String updateDate;
    }
}
