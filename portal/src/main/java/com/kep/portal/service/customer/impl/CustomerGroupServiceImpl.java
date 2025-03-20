package com.kep.portal.service.customer.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.customerGroup.CustomerGroupDto;
import com.kep.portal.model.dto.customerGroup.request.PostCustomerGroupRequestDto;
import com.kep.portal.model.dto.customerGroup.request.PutCustomerGroupRequestDto;
import com.kep.portal.model.dto.customerGroup.response.DeleteCustomerGroupResponseDto;
import com.kep.portal.model.dto.customerGroup.response.GetCustomerGroupListResponseDto;
import com.kep.portal.model.dto.customerGroup.response.PostCustomerGroupResponseDto;
import com.kep.portal.model.dto.customerGroup.response.PutCustomerGroupResponseDto;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.CustomerGroup;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.customer.CustomerGroupRepository;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.service.customer.CustomerGroupService;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerGroupServiceImpl implements CustomerGroupService {
    /** repositories **/
    private final MessageSourceUtil messageUtil;
    private final MemberRepository memberRepository;
    private final CustomerRepository customerRepository;
    private final CustomerGroupRepository customerGroupRepository;

    /**
     * 고객 그룹 등록
     * @param dto
     * @return
     */
    @Override
    public ResponseEntity<? super PostCustomerGroupResponseDto> createCustomerGroup(PostCustomerGroupRequestDto dto) {
        boolean exitedGroupName = customerGroupRepository.existsByGroupName(dto.getGroupName());
        if (exitedGroupName) return PostCustomerGroupResponseDto.existedGroupName(messageUtil.getMessage(MessageCode.DUPLICATED_DATA));

        boolean existedMember = memberRepository.existsById(dto.getMemberId());
        if (!existedMember) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));

        Member member = memberRepository.findById(dto.getMemberId()).get();

        CustomerGroup customerGroup = new CustomerGroup(dto, member);
        customerGroupRepository.save(customerGroup);

        return PostCustomerGroupResponseDto.success(messageUtil.success());
    }

    /**
     * 고객 그룹 전체 조회
     * @param memberId
     * @return
     */
    @Override
    public ResponseEntity<? super GetCustomerGroupListResponseDto> findAllCustomerGroupByMemberId(Long memberId) {
        boolean existedMember = memberRepository.existsById(memberId);
        if (!existedMember) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));

        List<CustomerGroupDto> customerGroupList = customerGroupRepository.findAllByMemberId(memberId)
                .stream().map(customerGroup -> CustomerGroupDto.of(customerGroup)).collect(Collectors.toList());

        return GetCustomerGroupListResponseDto.success(messageUtil.success(), customerGroupList);
    }

    /**
     * 고객 그룹 수정
     * @param dto
     * @return
     */
    @Override
    public ResponseEntity<? super PutCustomerGroupResponseDto> updateCustomerGroup(PutCustomerGroupRequestDto dto) {
        boolean existedMemberId = memberRepository.existsById(dto.getMemberId());
        if (!existedMemberId) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));

        boolean existedCustomerGroup = customerGroupRepository.existsById(dto.getId());
        if (!existedCustomerGroup) return ResponseDto.notExistedCustomerGroup(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER_GROUP));

        boolean existedCustomerGroupName = customerGroupRepository.existsByGroupName(dto.getGroupName());
        if (existedCustomerGroupName) return PutCustomerGroupResponseDto.existedGroupName(messageUtil.getMessage(MessageCode.DUPLICATED_DATA));

        CustomerGroup customerGroup = customerGroupRepository.findById(dto.getId()).get();
        if (!customerGroup.getMember().getId().equals(dto.getMemberId())) return ResponseDto.noPermission(messageUtil.getMessage(MessageCode.NO_PERMISSION));

        customerGroup.updateGroupName(dto.getGroupName());
        customerGroupRepository.save(customerGroup);

        return PutCustomerGroupResponseDto.success(messageUtil.success());
    }

    /**
     * 고객 그룹 삭제
     * @param customerGroupId
     * @return
     */
    @Override
    public ResponseEntity<? super DeleteCustomerGroupResponseDto> deleteCustomerGroup(Long customerGroupId) {
        boolean existedCustomerGroup = customerGroupRepository.existsById(customerGroupId);
        if (!existedCustomerGroup)
            return ResponseDto.notExistedCustomerGroup(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER_GROUP));

        CustomerGroup customerGroup = customerGroupRepository.findById(customerGroupId).get();

        List<Customer> customerList = customerRepository.findAllByCustomerGroup(customerGroup);
        customerList.stream().forEach(customer -> {
            customer.setCustomerGroup(null);
        });

        customerGroupRepository.delete(customerGroup);

        return DeleteCustomerGroupResponseDto.success(messageUtil.success());
    }
}
