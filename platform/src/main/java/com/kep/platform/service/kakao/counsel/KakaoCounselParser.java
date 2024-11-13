package com.kep.platform.service.kakao.counsel;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.issue.payload.IssuerType;
import com.kep.core.util.TimeUtils;
import com.kep.platform.config.property.PlatformProperty;
import com.kep.platform.model.dto.KakaoCounselReceiveEvent;
import com.kep.platform.model.dto.KakaoCounselReceiveRelayBotEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KakaoCounselParser {

	@Resource
	private PlatformProperty platformProperty;

	/**
	 * 채팅방 오픈 이벤트 파싱
	 */
	public Map<String, Object> parseReference(@NotNull @Valid KakaoCounselReceiveEvent event) {

		// 인입 파라미터 파싱 (reference 필드)
		KakaoCounselReceiveEvent.Reference reference = event.getReference();
		KakaoCounselReceiveEvent.Reference lastReference = event.getLastReference();

		String appUserId = event.getAppUserId();
		Map<String, Object> params = null;
		Map<String, Object> lastParams = null;

		String extra = null;

		if( reference != null && StringUtils.isNoneBlank(reference.getExtra())) {
			extra = reference.getExtra();
			log.info("KAKAO COUNSEL PARSER, REFERENCE, EXTRA: {}", extra);

			params = createParameterForReference(reference.getExtra(), appUserId);

			// 채팅방 종료 후 URL 매핑이 아닌 채팅을 입력 해서 신규 채팅 시 상담원 지정을 위해서 추가
		} else if (lastReference != null && StringUtils.isNoneBlank(lastReference.getExtra())) {
			extra = lastReference.getExtra();
			log.info("KAKAO COUNSEL PARSER, LAST REFERENCE, EXTRA: {}", extra);

			lastParams = createParameterForReference(lastReference.getExtra(), appUserId);

			params = new HashMap<>();
			params.put("appUserId", appUserId);
			if (lastParams.containsKey("mid")) params.put("mid", lastParams.get("mid"));
		}


		log.info("KAKAO COUNSEL PARSER, REFERENCE, PARAMS: {}", params);

		return params == null ? Collections.emptyMap() : params;
	}

	/**
	 * extra 파싱
	 * @param extra
	 * @param appUserId
	 * @return
	 */
	private Map<String, Object> createParameterForReference(String extra, String appUserId) {
		Map<String, Object> params = new HashMap<>();
		params.put("appUserId", appUserId);

		String[] extraParams = extra.split("__");
		for (String param : extraParams) {
			if (!param.isEmpty()) {
				int underscoreIndex = param.indexOf("_");
				if (underscoreIndex != -1) {
					String key = param.substring(0, underscoreIndex);
					String value = param.substring(underscoreIndex + 1);
					params.putIfAbsent(key, value);
				}
			}
		}

		return params;
	}


	@Deprecated
	public IssuePayload parseMessage(@NotNull @Valid KakaoCounselReceiveEvent event) {

		// 새 포맷 확인
		if (!ObjectUtils.isEmpty(event.getContents())) {
			return parseMessageContents(event);
		}

		// 파일 (이미지, 오디오 등) 메세지인 경우, contents 필드 내용이 `JSON Object`로 구성되어있음
		// {"contents": {"url": "https://kakao.com/blah_blah"}}
		String contents = event.getContent().toString();

		IssuePayload.Section section = new IssuePayload.Section();

		switch (event.getType()) {
			case text:
				section.setType(IssuePayload.SectionType.text);
				break;

			case photo:
				section.setType(IssuePayload.SectionType.file);
				section.setDisplay("image");
				contents = contents.replaceAll("^\\{url=", "").replaceAll("}$", "");
				// TODO: 폐쇄망일 경우 (프록시 사용, 이미지 주소 변경)
				break;

			case video:
				section.setType(IssuePayload.SectionType.file);
				section.setDisplay("video");
				contents = contents.replaceAll("^\\{url=", "").replaceAll("}$", "");
				// TODO: 폐쇄망일 경우 (프록시 사용, 이미지 주소 변경)
				break;

			case audio:
				section.setType(IssuePayload.SectionType.file);
				section.setDisplay("audio");
				contents = contents.replaceAll("^\\{url=", "").replaceAll("}$", "");
				// TODO: 폐쇄망일 경우 (프록시 사용, 이미지 주소 변경)
				break;

			case file:
				section.setType(IssuePayload.SectionType.file);
				section.setDisplay("file");
				contents = contents.replaceAll("^\\{url=", "").replaceAll("}$", "");
				// TODO: 폐쇄망일 경우 (프록시 사용, 이미지 주소 변경)
				break;

			default:
				break;
		}

		section.setData(contents);
		if (!ObjectUtils.isEmpty(event.getExtra())) {
			section.setExtra(event.getExtra());
		}

		IssuePayload issuePayload = new IssuePayload(section);
		// 플랫폼 고유 정보 세팅
		if (ObjectUtils.isEmpty(issuePayload.getMeta())) {
			issuePayload.setMeta(new HashMap<>());
		}
		// 메세지 시간
		if (event.getTime() != null) {
			issuePayload.getMeta().put("created", TimeUtils.toZonedDateTime(event.getTime()));
		}

		return issuePayload;
	}

	/**
	 * 메세지 파싱
	 * <p>
	 * 파일 메세지인 경우
	 * <pre>
	 * "contents": [
	 *         {
	 *             "url": "https://www.kakaocorp.com/page/favicon.ico",
	 *             "comment": "코멘트1"
	 *         },
	 *         {
	 *             "url": "https://www.kakaocorp.com/page/favicon.ico",
	 *             "comment": "코멘트2"
	 *         }
	 *     ]
	 * </pre>
	 *
	 * 텍스트 메세지인 경우
	 * <pre>
	 * "contents": [
	 *         "안녕하세요"
	 *     ]
	 * </pre>
	 */
	public IssuePayload parseMessageContents(@NotNull @Valid KakaoCounselReceiveEvent event) {

		log.info("NEW FORMAT");

		List<Object> contents = event.getContents();
		List<IssuePayload.Section> sections;
		if (KakaoCounselReceiveEvent.MessageType.text.equals(event.getType())) {
			sections = parseTextContents(contents);
		} else {
			sections = parseFileContents(event.getType(), contents);
		}

		IssuePayload issuePayload = new IssuePayload(sections);
		// 플랫폼 고유 정보 세팅
		if (ObjectUtils.isEmpty(issuePayload.getMeta())) {
			issuePayload.setMeta(new HashMap<>());
		}
		// 메세지 시간
		if (event.getTime() != null) {
			issuePayload.getMeta().put("created", TimeUtils.toZonedDateTime(event.getTime()));
		}

		return issuePayload;
	}

	public IssuePayload.SectionType getSectionType(@NotNull KakaoCounselReceiveEvent.MessageType messageType) {

		switch (messageType) {
			case file:
			case audio:
			case photo:
			case video:
				return IssuePayload.SectionType.file;
			default:
				return IssuePayload.SectionType.text;
		}
	}

	public String getDisplayOfFileSection(@NotNull KakaoCounselReceiveEvent.MessageType messageType) {

		switch (messageType) {
			case audio:
				return "audio";
			case photo:
				return "image";
			case video:
				return "video";
			default:
				return "file";
		}
	}

	/**
	 * 봇 대화 내용 중 고객 질의
	 */
	public IssuePayload parseRelayAsk(@NotNull @Valid KakaoCounselReceiveRelayBotEvent event) {

		// ////////////////////////////////////////////////////////////////////
		// 고객 메세지
		if (ObjectUtils.isEmpty(event.getUtterance())) {
			return null;
		} else {
			IssuePayload issuePayload = new IssuePayload(IssuePayload.Section.builder()
					.type(IssuePayload.SectionType.text)
					.data(event.getUtterance())
					.build());

			// 카카오 봇 고유 데이터
			Map<String, Object> meta = new HashMap<>();
			meta.put("created", event.getTimestamp());
			meta.put("issuer", IssuerType.guest.name());
			issuePayload.setMeta(meta);

			return issuePayload;
		}
	}

	/**
	 * 봇 대화 내용 중 봇 답변
	 */
	public List<IssuePayload> parseRelayReply(@NotNull @Valid KakaoCounselReceiveRelayBotEvent event) {

		List<IssuePayload> issuePayloads = new ArrayList<>();

		for (KakaoCounselReceiveRelayBotEvent.Message message : event.getMessages()) {
			IssuePayload issuePayload = new IssuePayload();

			// 카카오 봇 고유 데이터
			Map<String, Object> meta = new HashMap<>();
			meta.put("created", event.getTimestamp());
			meta.put("issuer", IssuerType.bot.name());
			issuePayload.setMeta(meta);

			if (!ObjectUtils.isEmpty(message.getType())) {
				switch (message.getType()) {
					case "text":
						issuePayload.add(IssuePayload.Section.builder()
								.type(IssuePayload.SectionType.text)
								.data(message.getText())
								.build());
						break;

					case "image":
						issuePayload.add(IssuePayload.Section.builder()
								.type(IssuePayload.SectionType.text)
								.data("이미지")
								.build());
						break;

					case "card.text":
					case "card.image":
						for (KakaoCounselReceiveRelayBotEvent.Card card : message.getCards()) {
							IssuePayload.Chapter chapter = new IssuePayload.Chapter();

							if (!ObjectUtils.isEmpty(card.getImageUrl())) {
								issuePayload.add(IssuePayload.Section.builder()
										.type(IssuePayload.SectionType.file)
										.data(card.getImageUrl())
										.display("image/png")
										.build());
							}
							if (!ObjectUtils.isEmpty(card.getTitle())) {
								issuePayload.add(IssuePayload.Section.builder()
										.type(IssuePayload.SectionType.text)
										.data(card.getTitle())
										.build());
							}
							if (!ObjectUtils.isEmpty(card.getDescription())) {
								issuePayload.add(IssuePayload.Section.builder()
										.type(IssuePayload.SectionType.text)
										.data(card.getDescription())
										.build());
							}
							if (card.getButtons() != null) {
								List<IssuePayload.Action> actionList = new ArrayList<>();
								for (KakaoCounselReceiveRelayBotEvent.Button button : card.getButtons()) {
									IssuePayload.Action action = IssuePayload.Action.builder()
											.type(parseButtonType(button.getType()))
											.name(button.getLabel())
											.build();
									if (IssuePayload.ActionType.link.equals(parseButtonType(button.getType()))) {
										action.setData(button.getUrl());
										if (!ObjectUtils.isEmpty(button.getPhone())) {
											action.setData("tel:" + button.getPhone());
										}
										actionList.add(action);
									} else {
										action.setData(button.getMessage());
										if (button.getData() != null && !ObjectUtils.isEmpty(button.getData().getBlockId())) {
//											action.setExtra(button.getData().getBlockId());
											actionList.add(action);
										}
									}
								}
								if (!actionList.isEmpty()) {
									issuePayload.add(IssuePayload.Section.builder()
											.type(IssuePayload.SectionType.action)
											.actions(actionList)
											.build());
								}
							}

							if (!chapter.isEmpty())
								issuePayload.add(new IssuePayload.Chapter());
						}
						break;

//					case "quickreply":
//						break;

					default:
						break;
				}
			}

			if (!issuePayload.isEmpty()) {
				issuePayloads.add(issuePayload);
			}
		}

		// 파싱 불가
		if (issuePayloads.isEmpty()) {
			IssuePayload issuePayload = new IssuePayload(IssuePayload.Section.builder()
					.type(IssuePayload.SectionType.text)
					.data(!ObjectUtils.isEmpty(event.getSpeech()) ? event.getSpeech() : "봇 메세지 (인식 불가)")
					.build());

			// 카카오 봇 고유 데이터
			Map<String, Object> meta = new HashMap<>();
			meta.put("created", event.getTimestamp());
			meta.put("issuer", IssuerType.bot.name());
			issuePayload.setMeta(meta);

			issuePayloads.add(issuePayload);
		}

		return issuePayloads;
	}

	/**
	 * 버튼 타입 ({@link IssuePayload.ActionType}) 결정
	 */
	public IssuePayload.ActionType parseButtonType(@NotEmpty String buttonType) {

		if ("url".equals(buttonType)
				|| "phone".equals(buttonType)) {
			return IssuePayload.ActionType.link;
		} else {
			return IssuePayload.ActionType.message;
		}
	}

	/************************* Private methods ****************************/
	private List<IssuePayload.Section> parseTextContents(@NotEmpty List<Object> contents) {
		return contents.stream().map(content -> IssuePayload.Section.builder()
				.type(IssuePayload.SectionType.text)
				.data(content.toString())
				.build()).collect(Collectors.toList());
	}

	private List<IssuePayload.Section> parseFileContents(@NotNull KakaoCounselReceiveEvent.MessageType messageType, @NotEmpty List<Object> contents) {
		if (KakaoCounselReceiveEvent.MessageType.photo.equals(messageType) && !platformProperty.isAllowReceiveImage()) {
			log.warn("PREVENT IMAGE TYPE");
			return Collections.singletonList(IssuePayload.Section.builder()
					.type(IssuePayload.SectionType.text)
					.data(platformProperty.getReplaceMessageForPrevent()).build());
		} else if (!KakaoCounselReceiveEvent.MessageType.photo.equals(messageType) && !platformProperty.isAllowReceiveFile()) {
			log.warn("PREVENT FILE TYPE");
			return Collections.singletonList(IssuePayload.Section.builder()
					. type(IssuePayload.SectionType.text)
					.data(platformProperty.getReplaceMessageForPrevent()).build());
		}

		return contents.stream().map(content -> IssuePayload.Section.builder()
				.display(getDisplayOfFileSection(messageType))
				.data(((Map<String, String>) content).get("url"))
				.name(((Map<String, String>) content).get("comment"))
				.build()).collect(Collectors.toList());
	}
}
