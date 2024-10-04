package com.kep.portal.repository.assign;

import com.kep.portal.model.dto.member.MemberStatusSyncDto;

import java.util.List;

public interface OfficeHoursSearchRepository {

    List<MemberStatusSyncDto> searchMemberAndMemberOfficeHoursListUseBranchId(Long branchId);

    List<MemberStatusSyncDto> searchMemberAndBranchOfficeHoursList();

}