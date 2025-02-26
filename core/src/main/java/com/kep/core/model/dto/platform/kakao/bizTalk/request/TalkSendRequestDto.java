package com.kep.core.model.dto.platform.kakao.bizTalk.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.platform.BizTalkMessageType;
import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.vo.*;
import lombok.*;
import org.springframework.util.Assert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalkSendRequestDto {
    // 고객 ID
    private String clientId;
    // 사용자 정의 Key Id
    @NotEmpty
    private String cid;
    // 알림톡 메시지 타입
    @NotNull
    private BizTalkMessageType messageType;
    // 발신 프로필 키
    @NotEmpty
    @Size(max = 40)
    private String senderKey;
    // 고객 키
    private String userKey;
    // 사용자 전화번호
    @NotEmpty
    @Size(max = 16)
    private String phoneNumber;
    // 템플릿 코드
    @NotEmpty
    @Size(max = 30)
    private String templateCode;
    // 메시지 본문 - 최대 1000자 (공백포함)
    // TODO : 채널추가 버튼이 있는 경우 960자로 제한
    @NotEmpty
    @Size(max = 1000)
    private String message;
    // 발신자 전화번호
    @NotEmpty
    @Size(max = 16)
    private String senderNo;
    // 톡 가격
    private Integer price;
    // 통화 타입 KRW, USD, EUR 등
    private String currencyType;
    // 강조할 내용
    @Size(max = 60)
    private String title;
    // 상단에 표기될 내용
    private String header;
    // 광고 사용 여부
    private String adFlag;
    // 타임아웃(초)
    @NotNull
    private Integer timeout;
    // 버튼 목록 정보
    private List<Button> button;
    // 바로가기 목록 정보
    private List<QuickReply> quickReply;
    // 대체 전송 사용 여부
    @NotEmpty
    private Boolean fallBackYn;
    // 대체 메시지 타입
    private BizTalkMessageType fallBackMessageType;
    // 대체 메시지 제목
    private String fallBackTitle;
    // 대체 메시지 본문
    @Size(max = 2000)
    private String fallBackMessage;
    // 노출 이미지 정보
    private Image image;
    // 아이템 리스트 및 아이템 정보
    private Item item;
    // 아이템 하이라이트
    private ItemHighlight itemHighlight;
    // 정산 코드 1
    private String taxCd1;
    // 정산 코드 2
    private String taxCd2;

    // FIXME : 비즈니스 파악 이후 field값 추가 구성
    public static TalkSendRequestDto ofAlimTalk(KakaoAlertSendEvent sendEvent, KakaoAlertSendEvent.Message message) {
        if (sendEvent.getSendMessages().size() < 1)
            Assert.notNull(sendEvent.getSendMessages(), "messsage can not be null");

        return TalkSendRequestDto.builder()
                .clientId(sendEvent.getClientId())
                .cid(message.getCid())
                .messageType(sendEvent.getMessageType())
                .senderKey(sendEvent.getSenderKey())
                .phoneNumber(message.getPhoneNumber())
                .templateCode(message.getTemplateCode())
                .message(message.getMessage())
                .senderNo(message.getSenderNo())
                .price(message.getPrice())
                .currencyType(message.getCurrencyType())
                .title(message.getTitle())
                .header(message.getHeader())
                .timeout(message.getTimeout() != null ? message.getTimeout() : 180)
                .button(message.getButton() != null ?
                        message.getButton().stream().map(button -> Button.ofAlimTalk(button)).collect(Collectors.toList()) : null)
                // fallback 관련된 내용은 추후 추가할 것
                .fallBackYn(false)
                .build();
    }

    // FIXME : 비즈니스 파악 이후 field값 추가 구성
    public static TalkSendRequestDto ofFriendTalk(KakaoFriendSendEvent sendEvent, KakaoFriendSendEvent.Message message) {
        if (sendEvent.getSendMessages().size() < 1)
            Assert.notNull(sendEvent.getSendMessages(), "messsage can not be null");

        return TalkSendRequestDto.builder()
                .clientId(sendEvent.getClientId())
                .cid(message.getCid())
                .messageType(sendEvent.getMessageType())
                .senderKey(sendEvent.getSenderKey())
                .phoneNumber(message.getPhoneNumber())
                .message(message.getMessage())
                .senderNo(message.getSenderNo())
                .title(message.getTitle())
                .button(message.getButton() != null ?
                        message.getButton().stream().map(button -> Button.ofFriendTalk(button)).collect(Collectors.toList()) : null)
                .image(message.getImage() != null ?
                        Image.builder()
                        .imgLink(message.getImage().getImgLink())
                        .imgUrl(message.getImage().getImgUrl())
                        .build() : null)
                // fallback 관련된 내용은 추후 추가할 것
                .fallBackYn(false)
                .build();
    }
}
