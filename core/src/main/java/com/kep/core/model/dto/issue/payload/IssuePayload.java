package com.kep.core.model.dto.issue.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.util.ObjectUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 솔루션 메세지 포맷
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssuePayload {

	public static final String CURRENT_VERSION = "0.1";

	@NotEmpty
	@Size(max = 10)
	@Builder.Default
	private String version = CURRENT_VERSION;

	@NotEmpty
	private List<Chapter> chapters;

	// 플랫폼 고유 데이터
	private Map<String, Object> meta;

	public enum SectionType {
		title, text, file, action, command, platform_answer
	}

	public enum ActionType {
		message, link, hotkey, submit
	}

	public enum DeviceType {
		all,
		web, pc, mobile,
		app, android, ios
	}

	/**
	 * 플랫폼에서 관리하는 메세지 (카카오 상담톡 무과금 메세지)
	 */
	public enum PlatformAnswer {
		off, // 상담불가
		no_operator, // 상담부재
		no_answer, // 무응답
		wait // 상담대기
	}

	/**
	 * 말풍선, 캐러셀은 배열로 표현
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Chapter {

		@NotEmpty
		private List<Section> sections;

		/**
		 * 컨텐츠 유무, {@code sections} 엘리먼트가 있으면 컨텐츠가 있는것으로 간주
		 */
		@JsonIgnore
		public boolean isEmpty() {

			return this.sections == null || this.sections.isEmpty();
		}
	}

	/**
	 * 말풍선 안에서 실제 컨텐츠를 감싸는 컨테이너
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Section {

		@NotNull
		private SectionType type;
		private String data;
		private String display;
		private String extra;
		private Map<String, Object> params;
		private List<Action> actions;

		/**
		 * 이름 (파일 이름 등)
		 */
		private String name;
		/**
		 * 크기 (파일 크키 등)
		 */
		private Long size;
	}

	/**
	 * 버튼 (이벤트가 가능한 엘리먼트)
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Action {

		@NotNull
		private ActionType type;
		@NotEmpty
		private String name;
		@NotEmpty
		private String data;
		private DeviceType deviceType;
		private String extra;
		private Map<String, Object> params;
		private String triggerData;
	}

	/**
	 * 말풍선 추가
	 */
	@JsonIgnore
	public void add(@NotNull Chapter chapter) {

		if (this.chapters == null)
			this.chapters = new ArrayList<>();
		this.chapters.add(chapter);
	}

	/**
	 * 마지막 말풍선에 섹션 추가
	 */
	@JsonIgnore
	public void add(@NotNull Section section) {

		Chapter lastChapter = getLastChapter();

		if (lastChapter.sections == null) {
			lastChapter.sections = new ArrayList<>();
		}

		lastChapter.sections.add(section);
	}

	/**
	 * 마지막 말풍선
	 */
	@JsonIgnore
	public Chapter getLastChapter() {

		if (this.chapters == null)
			this.chapters = new ArrayList<>();

		Chapter lastChapter;
		if (this.chapters.isEmpty()) {
			lastChapter = Chapter.builder()
					.sections(new ArrayList<>())
					.build();
			this.add(lastChapter);
		} else {
			lastChapter = this.chapters.get(this.chapters.size() - 1);
		}

		return lastChapter;
	}

	/**
	 * 컨텐츠 유무
	 */
	@JsonIgnore
	public boolean isEmpty() {

		if (this.chapters != null && !this.chapters.isEmpty()) {
			for (Chapter chapter : this.chapters) {
				if (!chapter.isEmpty()) {
					return false;
				}
			}
		}

		return true;
	}

	public IssuePayload(@NotNull @Valid Section section) {

		List<Section> sectionList = new ArrayList<>();
		sectionList.add(section);

		this.version = CURRENT_VERSION;
		this.chapters = new ArrayList<>();
		this.chapters.add(Chapter.builder()
						.sections(sectionList)
						.build());
	}

	public IssuePayload(@NotEmpty List<Section> sectionList) {

		this.chapters = new ArrayList<>();
		this.chapters.add(Chapter.builder()
						.sections(sectionList)
						.build());
	}

	public IssuePayload(@NotEmpty @Size(max = 1000) String text) {

		Section section = Section.builder()
				.type(SectionType.text)
				.data(text)
				.build();

		List<Section> sectionList = new ArrayList<>();
		sectionList.add(section);

		this.chapters = new ArrayList<>();
		this.chapters.add(Chapter.builder()
				.sections(sectionList)
				.build());
	}

	public IssuePayload(@NotNull PlatformAnswer platformAnswer) {

		Section section = Section.builder()
						.type(SectionType.platform_answer)
						.data(platformAnswer.name())
						.build();

		List<Section> sectionList = new ArrayList<>();
		sectionList.add(section);

		this.chapters = new ArrayList<>();
		this.chapters.add(Chapter.builder()
						.sections(sectionList)
						.build());
	}

	@JsonIgnore
	public Object getMetaValue(@NotEmpty String key) {
		if (!ObjectUtils.isEmpty(this.meta)) {
			return this.meta.get(key);
		}
		return "";
	}
}
