package com.kep.platform.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * 카카오 상담톡 수신 이벤트
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoCounselReceiveRelayBotEvent implements Comparable<KakaoCounselReceiveRelayBotEvent> {

	@NotNull
	@Positive
	private Long timestamp;
	@NotNull
	@Positive
	private Long profileId;
	@NotEmpty
	private String userKey;
	@NotEmpty
	private String utterance;
	@NotEmpty
	private String speech;
	@NotEmpty
	private String appUserId;
	@NotEmpty
	private String footprint;
	@NotEmpty
	private String blockId;
	@NotEmpty
	private String blockName;
	private String trigger;
	private Map<String, Object> extra;
	@NotEmpty
	@Size(max = 4)
	private List<Message> messages;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Message {

		@NotEmpty
		private String type;
		private String text;
		private boolean forwardable;
		private boolean lock;
		@Size(max = 10)
		private List<Card> cards;
		private List<Button> items;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Card {

		private LinkUrl linkUrl;
		private String description;
		private Integer height;
		private List<Button> buttons;
		private boolean fixedRatio;
		private String originVersion;
		private String title;
		private String imageUrl;
		private Integer width;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class DataType {

		private String blockId;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class LinkUrl {

		@NotEmpty
		private String type;
		private String webUrl;
		private String pcCustomScheme;
		private String androidStoreUrl;
		private String androidUrl;
		private String iosStoreUrl;
		private String macCustomScheme;
		private String moUrl;
		private String pcUrl;
		private String iosUrl;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Button {

		@NotEmpty
		private String type;
		@NotEmpty
		private String label;
		private String message;
		private String url;
		private String phone;
		private String blockId;
		private DataType data;
	}

	@Override
	public int compareTo(KakaoCounselReceiveRelayBotEvent o) {
		return Long.compare(this.getTimestamp(), o.getTimestamp());
	}
}
