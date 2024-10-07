package com.kep.portal.service.system;

import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.core.model.dto.system.SystemEventHistoryDto;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.system.SystemEventHistory;
import com.kep.portal.model.entity.system.SystemEventHistoryMapper;
import com.kep.portal.repository.system.SystemEventHistoryRepository;
import com.kep.portal.util.ZonedDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class SystemEventService {

    @Resource
    private SystemEventHistoryRepository systemEventHistoryRepository;

    @Resource
    private SystemEventHistoryMapper systemEventHistoryMapper;

    public Page<SystemEventHistoryDto> index(LocalDate from , LocalDate to , Long branchId , Long teamId , Long memberId
            , List<SystemEventHistoryActionType> actions , @NotNull Pageable pageable){



        ZonedDateTime start = ZonedDateTimeUtil.start(from.toString());
        ZonedDateTime end = ZonedDateTimeUtil.end(to.toString());

        Page<SystemEventHistory> entities = systemEventHistoryRepository.search(pageable , start , end , branchId , teamId , memberId , actions);
        return new PageImpl<>(systemEventHistoryMapper.map(entities.getContent()), entities.getPageable(), entities.getTotalElements());
    }

    /**
     *
     * @param fromMember
     * @param toMemberId
     * @param action
     * @param entity
     * @param before
     * @param after
     * @param cud
     */
    public void store(Member fromMember , Long toMemberId , SystemEventHistoryActionType action , String entity , String before
            , String after , String clientIp , String userAgent , String cud , Long teamId){

        SystemEventHistory systemEventHistory = SystemEventHistory.builder()
                .branchId(fromMember.getBranchId())
                .fromMember(fromMember)
                .toMemberId(toMemberId)
                .action(action)
                .entity(entity)
                .beforPayload(before)
                .afterPayload(after)
                .created(ZonedDateTime.now())
                .clientIp(clientIp)
                .userAgent(userAgent)
                .cud(cud)
                .teamId(teamId)
                .build();

        systemEventHistoryRepository.save(systemEventHistory);
        systemEventHistoryRepository.flush();
    }

}
