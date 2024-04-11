package com.kep.portal.model.entity.issue;

import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.entity.customer.GuestMapper;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.customer.CustomerMapper;
import com.kep.portal.model.entity.branch.BranchMapper;
import com.kep.portal.model.entity.channel.ChannelMapper;
import com.kep.portal.model.entity.subject.IssueCategoryMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {BranchMapper.class, ChannelMapper.class, CustomerMapper.class, GuestMapper.class,
		IssueLogMapper.class, MemberMapper.class, IssueExtraMapper.class, IssueCategoryMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueMapper {

	IssueMapper INSTANCE = Mappers.getMapper(IssueMapper.class);

	Issue map(IssueDto dto);

	IssueDto map(Issue entity);

	List<IssueDto> map(List<Issue> entities);

	@Deprecated
	// 스케줄러로 계산 후 실제 status 변경하도록 변경됨
//	@AfterMapping
	default void setIssueStatus(@MappingTarget IssueDto.IssueDtoBuilder issueDto, Issue issue) {

		// 미답변 시간 n분 경과시, IssueStatus.urgent 상태
		if (IssueStatus.ask.equals(issue.getStatus()) && issue.getFirstAsked() != null) {
			ZonedDateTime now = ZonedDateTime.now();
			long minutes = issue.getFirstAsked().until(now, ChronoUnit.MINUTES);
			if (minutes >= 5) { // TODO: 관리 가능
				issueDto.status(IssueStatus.urgent);
			}
		}
	}
}
