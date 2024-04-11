/**
 * Guest Memo Mapper 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.04.06 / asher.shin   / 신규
 */
package com.kep.portal.model.entity.customer;

import com.kep.portal.model.dto.customer.GuestMemoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring"
        , uses = {GuestMemoMapper.class}
        , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuestMemoMapper {

    GuestMemo map(GuestMemoDto dto);

    GuestMemoDto map (GuestMemo entity);


}

