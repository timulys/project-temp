package com.kep.portal.service.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.upload.UploadHistoryDto;
import com.kep.core.model.exception.BizException;
import com.kep.portal.model.dto.upload.UploadHistorySearchCondition;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.model.entity.upload.UploadHistory;
import com.kep.portal.model.entity.upload.UploadHistoryMapper;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.upload.UploadHistoryRepository;
import com.kep.portal.service.customer.GuestService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.subject.IssueCategoryService;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UploadHistoryService {

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private UploadHistoryRepository uploadHistoryRepository;

    @Resource
    private UploadHistoryMapper uploadHistoryMapper;

    @Resource
    private IssueCategoryService issueCategoryService;

    @Resource
    private MemberService memberService;

    @Resource
    private GuestService guestService;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private ChannelRepository channelRepository;

    public UploadHistoryDto store(UploadHistoryDto dto, Issue issue) throws Exception {
        Assert.notNull(issue, "issue is null");
        Member member = memberService.findById(securityUtils.getMemberId());


        IssueCategory category = null;

        if (issue.getIssueCategory() != null) {
            category = issueCategoryService.findById(issue.getIssueCategory().getId());
        }

        UploadHistory uploadHistory = uploadHistoryMapper.map(dto);
        uploadHistory.setTeamId(securityUtils.getTeamId());
        uploadHistory.setIssueId(issue.getId());
        uploadHistory.setIssueCategory(category);
        uploadHistory.setGuest(issue.getGuest());
        uploadHistory.setCreator(member);

        return uploadHistoryMapper.map(uploadHistoryRepository.save(uploadHistory));
    }

    public Page<UploadHistoryDto> searchOpen(@NotNull @Valid UploadHistorySearchCondition condition, @NotNull Pageable pageable) {

        if (!setGuestCondition(condition)) {
            return new PageImpl<>(Collections.emptyList());
        }

        if (condition.getChannelId() != null) {
            channelRepository.findById(condition.getChannelId()).orElseThrow(() -> new IllegalArgumentException("not found channel"));
        }

        if (condition.getIssueCategoryId() != null) {
            /**
             * FIXME :: 채널아이디 없을 때 카테고리 아이디로 채널ID 가져옴. 지금 프론트 검색용 컴포넌트가 디폴트 채널 세팅하는데 그대로 API 콜할 때 채널ID를 넘겨주지 않음.
             * FIXME :: 프론트 컴포넌트는 상담 카테고리 조회조건 쪽에서 동일하게 사용하므로 백엔드 쪽이 사이드 이펙트가 더 적을것 같아 여기에 반영 -> 추후엔 없애야해여 20241128 volka
             */
            if (condition.getChannelId() == null) {
                IssueCategory category = issueCategoryService.findById(condition.getIssueCategoryId());
                if (category == null) throw new BizException("not found category");
                condition.setChannelId(category.getChannelId());
            }
            condition.setIssueCategoryIds(issueCategoryService.getLowestCategoriesById(condition.getChannelId(), condition.getIssueCategoryId()));
        }

        Page<UploadHistory> page = uploadHistoryRepository.search(condition, pageable);

        List<UploadHistoryDto> map = uploadHistoryMapper.map(page.getContent());

        return new PageImpl<>(map, page.getPageable(), page.getTotalElements());
    }

    private boolean setGuestCondition(@NotNull UploadHistorySearchCondition condition) {
        if (!ObjectUtils.isEmpty(condition.getCustomerSubject()) && !ObjectUtils.isEmpty(condition.getCustomerQuery())) {
            List<Guest> guests = guestService.searchGuestAndCustomer(condition.getCustomerSubject(), condition.getCustomerQuery());
            if (guests.isEmpty()) {
                return false;
            }
            condition.setGuests(guests);
        }
        return true;
    }
}
