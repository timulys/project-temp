package com.kep.portal.service.guestMemo.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.guestMemo.GuestMemoDto;
import com.kep.portal.model.dto.guestMemo.request.PostGuestMemoRequestDto;
import com.kep.portal.model.dto.guestMemo.response.GetGuestMemoResponseDto;
import com.kep.portal.model.dto.guestMemo.response.PostGuestMemoResponseDto;
import com.kep.portal.model.entity.customer.GuestMemo;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.customer.GuestMemoRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.service.guestMemo.GuestMemoService;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GuestMemoServiceImpl implements GuestMemoService {
    // Autowired Components
    private final GuestRepository guestRepository;
    private final CustomerRepository customerRepository;
    private final GuestMemoRepository guestMemoRepository;

    private final SecurityUtils securityUtils;
    private final MessageSourceUtil messageUtil;


    @Override
    public ResponseEntity<? super PostGuestMemoResponseDto> postGuestMemo(PostGuestMemoRequestDto dto) {
        if (dto.getGuestId() == null && dto.getCustomerId() == null) {
            return ResponseDto.validationFailedMessage(messageUtil.getMessage(MessageCode.VALIDATION_FAILED));
        }
        GuestMemo guestMemo = null;
        if (dto.getGuestId() != null) {
            boolean existedByGuestId = guestRepository.existsById(dto.getGuestId());
            if (!existedByGuestId)
                return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));
            guestMemo = guestMemoRepository.findByGuestIdAndMemberId(dto.getGuestId(), securityUtils.getMemberId());
        } else {
            boolean existedByCustomerId = customerRepository.existsById(dto.getCustomerId());
            if (!existedByCustomerId)
                return ResponseDto.notExistedCustomer(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER));
            guestMemo = guestMemoRepository.findByCustomerIdAndMemberId(dto.getCustomerId(), securityUtils.getMemberId());
        }

        if(guestMemo != null) {
            guestMemo.setMemberId(securityUtils.getMemberId());
            guestMemo.setCustomerId(dto.getCustomerId());
            guestMemo.setContent(dto.getContent());
            guestMemo.setModified(ZonedDateTime.now());
        } else {
            GuestMemo newGuestMemo = GuestMemo.builder()
                    .guestId(dto.getGuestId())
                    .customerId(dto.getCustomerId())
                    .content(dto.getContent())
                    .memberId(securityUtils.getMemberId())
                    .created(ZonedDateTime.now())
                    .build();
            guestMemoRepository.save(newGuestMemo);
        }


        return PostGuestMemoResponseDto.success(messageUtil.success());
    }

    @Override
    public ResponseEntity<? super GetGuestMemoResponseDto> findGuestMemo(Long guestId, Long customerId) {
        if (guestId == null && customerId == null) {
            return ResponseDto.validationFailedMessage(messageUtil.getMessage(MessageCode.VALIDATION_FAILED));
        }
        GuestMemo guestMemo = null;
        if (guestId != null) {
            boolean existedByGuestId = guestRepository.existsById(guestId);
            if (!existedByGuestId)
                return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));
            guestMemo = guestMemoRepository.findByGuestIdAndMemberId(guestId, securityUtils.getMemberId());
        }
        if (guestMemo == null && customerId != null) {
            boolean existedByCustomerId = customerRepository.existsById(customerId);
            if (!existedByCustomerId)
                return ResponseDto.notExistedCustomer(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER));
            guestMemo = guestMemoRepository.findByCustomerIdAndMemberId(customerId, securityUtils.getMemberId());
        }

        if (guestMemo == null) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

        return GetGuestMemoResponseDto.success(GuestMemoDto.of(guestMemo), messageUtil.success());
    }
}
