/**
 * Guest Memo Repository 추가
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.04.06 / asher.shin   / 신규
 *  2023.05.24 / ahser.shin   / 고객Id 로 검색 기능 추가
 */
package com.kep.portal.service.customer;




import com.kep.portal.model.dto.customer.GuestMemoDto;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.customer.GuestMemo;
import com.kep.portal.model.entity.customer.GuestMemoMapper;
import com.kep.portal.repository.customer.GuestMemoRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.util.SecurityUtils;
import com.mysema.commons.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Objects;


@Service
@Transactional
@Slf4j
public class GuestMemoService {

    @Resource
    private GuestMemoRepository guestMemoRepository;

    @Resource
    private CustomerService customerService;

    @Resource
    private GuestMemoMapper guestMemoMapper;

    @Resource
    private GuestRepository guestRepository;

    @Resource
    private SecurityUtils securityUtils;

    public GuestMemoDto findGuestMemo(Long guestId){

        Assert.notNull(guestId,"guestId is null");



        GuestMemo guestmemo = guestMemoRepository.findByGuestIdAndMemberId(guestId,securityUtils.getMemberId());



        return guestMemoMapper.map(guestmemo);
    }

    public GuestMemoDto save(GuestMemoDto dto){

        Assert.notNull(dto.getGuestId(),"guestId is null");

        Guest guest = guestRepository.findById(dto.getGuestId()).orElse(null);

        Assert.notNull(guest,"guest entity is null");

        Assert.notNull(guest.getCustomer(),"customer is null");

        Assert.notNull(dto.getMemberId(),"memberId is null");



        GuestMemo guestMemo = null;





        Assert.isTrue(dto.getMemberId().equals(securityUtils.getMemberId()),"request Member not equal dto Member");

        if(dto.getId()== null){
            guestMemo = GuestMemo.
                            builder()
                            .guestId(dto.getGuestId())
                            .memberId(dto.getMemberId())
                            .customerId(guest.getCustomer().getId())
                            .content(dto.getContent())
                            .created(ZonedDateTime.now())
                            .modified(ZonedDateTime.now()).build();
        } else {
            guestMemo =guestMemoRepository.findByGuestIdAndMemberId(dto.getGuestId(),securityUtils.getMemberId());
            Assert.notNull(guestMemo,"guest_memo is null");
            Assert.isTrue(guestMemo.getId().equals(dto.getId()),"requestId is not equals guest and memberId");
            guestMemo.setContent(dto.getContent());
            guestMemo.setModified(ZonedDateTime.now());
        }

        guestMemoRepository.save(guestMemo);

        return guestMemoMapper.map(guestMemo);
    }

    public GuestMemoDto findCustomerMemo(Long customerId){

        Assert.notNull(customerId,"guestId is null");

        GuestMemo guestmemo = guestMemoRepository.findByCustomerIdAndMemberId(customerId,securityUtils.getMemberId());

        return guestMemoMapper.map(guestmemo);
    }


    public GuestMemoDto saveCustomerMemo(GuestMemoDto dto){

        Assert.notNull(dto.getCustomerId(),"customerId is null");

        Customer customer = customerService.findById(dto.getCustomerId());

        Assert.notNull(customer,"customer entity is null");

        Guest guest = guestRepository.findByCustomer(customer);

        if(guest==null){
            log.info("guest is null");
        }

        Assert.notNull(dto.getMemberId(),"memberId is null");

        GuestMemo guestMemo = null;

        Assert.isTrue(dto.getMemberId().equals(securityUtils.getMemberId()),"request Member not equal dto Member");
        guestMemo =guestMemoRepository.findByCustomerIdAndMemberId(dto.getCustomerId(),securityUtils.getMemberId());

        if(dto.getId()== null){
            Assert.isTrue(guestMemo==null," entity already exist ");
            guestMemo = GuestMemo.
                    builder()
                    .guestId(guest!=null?guest.getId():null)
                    .memberId(dto.getMemberId())
                    .customerId(dto.getCustomerId())
                    .content(dto.getContent())
                    .created(ZonedDateTime.now())
                    .modified(ZonedDateTime.now()).build();
        } else {

            Assert.notNull(guestMemo,"guest_memo is null");
            Assert.isTrue(guestMemo.getId().equals(dto.getId()),"request Memo Id is not equals Entity Id");
            guestMemo.setContent(dto.getContent());
            guestMemo.setModified(ZonedDateTime.now());
        }

        guestMemoRepository.save(guestMemo);

        return guestMemoMapper.map(guestMemo);
    }

}
