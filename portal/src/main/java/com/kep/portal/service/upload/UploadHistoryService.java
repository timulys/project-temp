package com.kep.portal.service.upload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.upload.UploadHistoryDto;
import com.kep.portal.model.dto.upload.UploadHistorySearchCondition;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.model.entity.upload.UploadHistory;
import com.kep.portal.model.entity.upload.UploadHistoryMapper;
import com.kep.portal.repository.upload.UploadHistoryRepository;
import com.kep.portal.service.customer.GuestService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.service.subject.IssueCategoryService;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        Page<UploadHistory> page = uploadHistoryRepository.search(condition, pageable);
        if(page.isEmpty() && condition.getIssueCategoryId() != null){
            ArrayList<Long> issueCategoryIds = new ArrayList<>();
            issueCategoryIds.add(condition.getIssueCategoryId());
            List<IssueCategory> byParentId = issueCategoryService.findByParentIdIn(issueCategoryIds);
            IssueCategory byId = issueCategoryService.findById(condition.getIssueCategoryId());

            if(!byParentId.isEmpty()){
                List<IssueCategory> issueList = byParentId.stream().filter(q -> q.getParent().getId().equals(condition.getIssueCategoryId())).collect(Collectors.toList());
                if (byId.getParent() == null){
                    issueCategoryIds.clear();
                    for (IssueCategory issueCategory : issueList) {
                        issueCategoryIds.add(issueCategory.getId());
                    }
                    List<IssueCategory> byParentIds = issueCategoryService.findByParentIdIn(issueCategoryIds);
                    issueCategoryIds.clear();
                    for (IssueCategory parentId : byParentIds) {
                        issueCategoryIds.add(parentId.getId());
                    }
                    page = uploadHistoryRepository.findByIssueCategoryId(issueCategoryIds, pageable);
                } else {
                    issueCategoryIds.clear();
                    for (IssueCategory issueCategory : issueList) {
                        issueCategoryIds.add(issueCategory.getId());
                    }
                    page = uploadHistoryRepository.findByIssueCategoryId(issueCategoryIds, pageable);
                }
            }
        }

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
