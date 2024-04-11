package com.kep.portal.config.property;

import com.kep.portal.model.entity.issue.IssueLog;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "application.portal.mode")
@Validated
@Data
public class ModeProperty {

	/**
	 * {@link IssueLog#assigner} 세팅 여부 == 상담원별 상담 이력 (본인의 이력만 노출) 기능 사용 여부
	 */
	@NotNull
	private Boolean useAssignerOnIssueLog;

	/**
	 * 이슈 생성시, 봇 이력 수집
	 */
	@NotNull
	private Boolean saveBotHistoryWhenOpen;

	/**
	 * 대화 목록에 봇 이력 제외
	 */
	@NotNull
	private Boolean excludeRelayedIssueLog;

	/**
	 * (솔루션에서) 이슈 종료 가능 여부 (by 상담원)
	 * TODO: 프론트단에서 종료 기능을 막으려면, 설정 정보 제공 필요
	 */
	@NotNull
	private Boolean closableIssueByOperator;
}
