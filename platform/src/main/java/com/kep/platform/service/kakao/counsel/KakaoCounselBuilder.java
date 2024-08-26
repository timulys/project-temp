package com.kep.platform.service.kakao.counsel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.platform.model.dto.KakaoCounselSendEvent;
import com.kep.platform.util.PlatformUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 링크(LI) 타입 메세지는 아래 형태로만 구성 가능
 * message + links
 * message + image
 * message + image + links
 */
@Component
@Slf4j
public class KakaoCounselBuilder {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private KakaoCounselService kakaoCounselService;

	public List<KakaoCounselSendEvent> build(@NotNull @Valid IssuePayload issuePayload
			, @NotEmpty String serviceKey, @NotEmpty String userKey, @NotEmpty String eventKey) throws Exception {

		List<KakaoCounselSendEvent> eventList = new ArrayList<>();

		for (IssuePayload.Chapter chapter : issuePayload.getChapters()) {
			// 카카오 상담톡 송신 이벤트
			KakaoCounselSendEvent event = KakaoCounselSendEvent.builder()
					.senderKey(serviceKey)
					.userKey(userKey)
					.build();

			List<IssuePayload.Section> sections = chapter.getSections();
			event.setMessageType(getMessageType(sections));
			for (IssuePayload.Section section : sections) {

				switch (section.getType()) {
					case title:
						event.appendTextMessage(PlatformUtils.escapeHtml(section.getData()) + "\n");
						break;

					case text:
						event.appendTextMessage(PlatformUtils.escapeHtml(section.getData()));
						break;

					case file:
						// TODO: 타입 체크
						if (section.getDisplay().startsWith("image")) {
							String uploadUrl;
//							String originalImageUrl = bizParser.getOriginalImageUrl(section.getData());

							// 이미지 타입 메세지
							if (KakaoCounselSendEvent.MESSAGE_TYPE_IMAGE.equals(event.getMessageType())) {
								uploadUrl = kakaoCounselService.uploadImage(UploadPlatformRequestDto.builder()
										.sourceUrl(section.getData())
										.build(), serviceKey);
							}
							// (이미지가 포함된) 링크 타입 메세지
							else {
								uploadUrl = kakaoCounselService.uploadImage(UploadPlatformRequestDto.builder()
										.sourceUrl(section.getData())
										.imageType(KakaoCounselSendEvent.IMAGE_TYPE_LINK)
										.build(), serviceKey);
							}

							if (ObjectUtils.isEmpty(uploadUrl)) {
								continue;
							}
							event.setImageUrl(uploadUrl);
						} else {
//							log.warn("Not Support Type: {}", section.getDisplay());
							String uploadUrl;

							if (KakaoCounselSendEvent.MESSAGE_TYPE_FILE.equals(event.getMessageType())) {
								uploadUrl = kakaoCounselService.uploadFile(UploadPlatformRequestDto.builder()
										.sourceUrl(section.getData())
										.build(), serviceKey);
							} else {
								continue;
							}

							// 파일 URL
							if (!ObjectUtils.isEmpty(uploadUrl)) {
								event.setFileUrl(uploadUrl);
							} else {
								continue;
							}

							// 파일 이름
							if (!ObjectUtils.isEmpty(section.getName())) {
								event.setFileName(section.getName());
							} else {
								event.setFileName("업로드 파일");
							}

							// 파일 크키
							if (!ObjectUtils.isEmpty(section.getName())) {
								event.setFileSize(section.getSize());
							} else {
								event.setFileSize(0L);
							}
						}
						break;

					case action:
						List<KakaoCounselSendEvent.Link> buttonList = event.getLinks();
						if (buttonList == null) {
							buttonList = new ArrayList<>();
							event.setLinks(buttonList);
						}

						for (IssuePayload.Action action : section.getActions()) {

							KakaoCounselSendEvent.Link link = new KakaoCounselSendEvent.Link();
							link.setName(action.getName());

							if (IssuePayload.ActionType.message.equals(action.getType())
									|| IssuePayload.ActionType.hotkey.equals(action.getType())) {
								link.setType(KakaoCounselSendEvent.BUTTON_TYPE_TEXT);
								link.setExtra(action.getExtra());
								buttonList.add(link);
							} else if (IssuePayload.ActionType.link.equals(action.getType())) {
								link.setType(KakaoCounselSendEvent.BUTTON_TYPE_WEB);
//								if (IssuePayload.DeviceType.all.equals(action.getDeviceType())
//										|| IssuePayload.DeviceType.mobile.equals(action.getDeviceType())) {
//								}
								link.setUrlPc(action.getData());
								link.setUrlMobile(action.getData());
								buttonList.add(link);
							} else {
								log.error("Invalid Action Type: {}", action.getType());
							}
						}
						break;

					case platform_answer:
						if (IssuePayload.PlatformAnswer.start.name().equals(section.getData())) {
							event.setAutoAnswer("ST");
						}
						else if (IssuePayload.PlatformAnswer.off.name().equals(section.getData())) {
							event.setAutoAnswer("S1");
						} else if (IssuePayload.PlatformAnswer.no_operator.name().equals(section.getData())) {
							event.setAutoAnswer("S2");
						} else if (IssuePayload.PlatformAnswer.no_answer.name().equals(section.getData())) {
							event.setAutoAnswer("S3");
						} else if (IssuePayload.PlatformAnswer.wait.name().equals(section.getData())) {
							event.setAutoAnswer("S4");
						} else {
							event.setAutoAnswer("S3");
							log.info("INVALID PLATFORM ANSWER: {}, USE 'S3'", section.getData());
						}
						break;

					default:
						break;
				}
			}

			eventList.add(event);
		}

		return normalizeEventList(eventList, eventKey);
	}

	// TODO: 챕터별, 섹션별, 타입별로 메소드 분리
	private KakaoCounselSendEvent build(IssuePayload.Section section) {

		return null;
	}

	/**
	 * {@code MAX_TEXT_SIZE} 보다 긴 텍스트일 경우 말풍선 추가, 링크 타입에서는 자름
	 * {@code MAX_BUTTON_SIZE} 보다 버튼 개수에 따라 메세지 나눔
	 *
	 * TODO: {@link IssuePayload}를 가지고 미리 나눠서 모든 채널에서 공통으로 사용할 수 있게, 채널마다 제한이 다른데?
	 */
	private List<KakaoCounselSendEvent> normalizeEventList(
			@NotEmpty @Valid List<KakaoCounselSendEvent> eventList
			, @NotEmpty String eventKey) throws Exception {

		int eventCount = 0;

		List<KakaoCounselSendEvent> partitionEventList = new ArrayList<>();

		for (KakaoCounselSendEvent event : eventList) {

			if (KakaoCounselSendEvent.MESSAGE_TYPE_TEXT.equals(event.getMessageType())) { // 텍스트 타입
				if (event.getMessage() != null && event.getMessage().length() > KakaoCounselSendEvent.MAX_TEXT_SIZE_TEXT_TYPE) { // 텍스트 길이 초과시 말풍선 추가
					for (int i = 0; i < event.getMessage().length(); i += KakaoCounselSendEvent.MAX_TEXT_SIZE_TEXT_TYPE) {
						String partitionMessage = event.getMessage().substring(i, Math.min(i + KakaoCounselSendEvent.MAX_TEXT_SIZE_TEXT_TYPE, event.getMessage().length()));
						// 메세지 객체 복제
						String json = objectMapper.writeValueAsString(event);
						KakaoCounselSendEvent partitionEvent = objectMapper.readValue(json, KakaoCounselSendEvent.class);
						partitionEvent.setMessage(partitionMessage);
						partitionEvent.setSerialNumber(getEventKey(eventKey, ++eventCount));
						partitionEventList.add(partitionEvent);
					}
				} else {
					event.setSerialNumber(getEventKey(eventKey, ++eventCount));
					partitionEventList.add(event);
				}
			} else if (KakaoCounselSendEvent.MESSAGE_TYPE_LINK.equals(event.getMessageType()) && event.getLinks() != null) { // 링크 타입
				if (event.getLinks().size() > KakaoCounselSendEvent.MAX_BUTTON_SIZE) { // 최대 개수 초과시 말풍선 추가

					final AtomicInteger counter = new AtomicInteger();

					final List<List<KakaoCounselSendEvent.Link>> partitionLinkList = new ArrayList<>(event.getLinks().stream()
							.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / KakaoCounselSendEvent.MAX_BUTTON_SIZE))
							.values());

					for (int i = 0; i < partitionLinkList.size(); i++) {
						List<KakaoCounselSendEvent.Link> linkList = partitionLinkList.get(i);

						// event 오브젝트 -> partitionEvent 로 복사
						String json = objectMapper.writeValueAsString(event);
						KakaoCounselSendEvent partitionEvent = objectMapper.readValue(json, KakaoCounselSendEvent.class);

						// 두 번째 말풍선 부터는, 이미지: 미노출, 메세지: '추가 선택 메뉴'
						if (i > 0) {
							partitionEvent.setImageUrl(null); // 이미지
							partitionEvent.setMessage("추가 선택 메뉴"); // 텍스트 메세지
						}
						partitionEvent.setLinks(linkList);
						partitionEvent.setSerialNumber(getEventKey(eventKey, ++eventCount));

						// 텍스트 길이 초과시
						if (!ObjectUtils.isEmpty(partitionEvent.getMessage())
								&& partitionEvent.getMessage().length() > KakaoCounselSendEvent.MAX_TEXT_SIZE_LINK_TYPE) {
//							partitionEvent.setMessage(partitionEvent.getMessage().substring(0, KakaoCounselSendEvent.MAX_TEXT_SIZE_LINKTYPE)); // 자름
							partitionEventList.addAll(normalizeEventList(Collections.singletonList(partitionEvent), partitionEvent.getSerialNumber())); // 말풍선 추가
						} else {
							// 텍스트 메세지 (카카오 상담톡 제한 사항)
							if (ObjectUtils.isEmpty(partitionEvent.getMessage())) {
								throw new UnsupportedOperationException("NO TEXT IN LINK MESSAGE");
//								partitionEvent.setMessage("문의사항을 선택해주세요.");
							}
							partitionEventList.add(partitionEvent);
						}
					}
				} else {
					event.setSerialNumber(getEventKey(eventKey, ++eventCount));
					// 텍스트 메세지 (카카오 상담톡 제한 사항)
					if (ObjectUtils.isEmpty(event.getMessage())) {
						throw new UnsupportedOperationException("NO TEXT IN LINK MESSAGE");
//						event.setMessage("문의사항을 선택해주세요.");
					}
					partitionEventList.add(event);
				}
			} else {
				event.setSerialNumber(getEventKey(eventKey, ++eventCount));
				partitionEventList.add(event);
			}
		}

		// 유효성 검증
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		for (KakaoCounselSendEvent partitionEvent : partitionEventList) {
			Set<ConstraintViolation<KakaoCounselSendEvent>> violations = validator.validate(partitionEvent);
			if (!violations.isEmpty()) {
				log.warn("INVALID EVENT: {}", violations);
			}
		}

		log.debug("partitionEventList: {}", partitionEventList);
		return partitionEventList;
	}

	/**
	 * 카카오 상담톡 메세지 타입 판별
	 */
	@NotEmpty
	private String getMessageType(@NotEmpty List<IssuePayload.Section> sections) {

		boolean hasText = false;
		boolean hasImage = false;
		boolean hasFile = false;
		boolean hasAction = false;

		for (IssuePayload.Section section : sections) {
			switch (section.getType()) {
				case file:
					if (section.getDisplay().startsWith("image")) {
						hasImage = true;
					} else {
						hasFile = true;
					}
					break;
				case action:
				case command:
				case platform_answer:
					hasAction = true;
					break;
				default:
					hasText = true;
					break;
			}
		}

		if (hasAction)
			return KakaoCounselSendEvent.MESSAGE_TYPE_LINK;
		else if (hasImage && hasText)
			return KakaoCounselSendEvent.MESSAGE_TYPE_LINK;
		else if (hasImage)
			return KakaoCounselSendEvent.MESSAGE_TYPE_IMAGE;
		else if (hasFile)
			return KakaoCounselSendEvent.MESSAGE_TYPE_FILE;
		else
			return KakaoCounselSendEvent.MESSAGE_TYPE_TEXT;
	}

	/**
	 * 메세지 고유 식별자 생성
	 */
	private String getEventKey(@NotEmpty String eventKey, int eventCount) {

		// Max length
		if (eventKey.length() > KakaoCounselSendEvent.SERIAL_NUMBER_SIZE - KakaoCounselSendEvent.SERIAL_NUMBER_PADDING_SIZE) {
			eventKey = eventKey.substring(0, KakaoCounselSendEvent.SERIAL_NUMBER_SIZE - KakaoCounselSendEvent.SERIAL_NUMBER_PADDING_SIZE);
		}

		// 메세지 나눠서 보내는 경우
		eventKey += "_" + eventCount;

		return KakaoCounselSendEvent.SERIAL_NUMBER_PREFIX + eventKey + System.currentTimeMillis();
	}

	/**
	 * 메세지 고유 식별자 생성
	 */
	public String generateEventKey() {

		return KakaoCounselSendEvent.SERIAL_NUMBER_PREFIX_ON_EMPTY + System.currentTimeMillis();
	}
}
