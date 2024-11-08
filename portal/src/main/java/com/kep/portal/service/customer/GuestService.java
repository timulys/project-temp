package com.kep.portal.service.customer;

import com.kep.core.model.dto.customer.GuestDto;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.customer.GuestMapper;
import com.kep.portal.repository.customer.GuestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class GuestService {

	@Resource
	private GuestRepository guestRepository;
	@Resource
	private GuestMapper guestMapper;
	@Resource
	private CustomerService customerService;

	@Nullable
	public Guest findById(@NotNull Long id) {

		return guestRepository.findById(id).orElse(null);
	}

	public List<Guest> findAllByCustomerIn(@NotEmpty List<Customer> customers) {

		return guestRepository.findAllByCustomerIn(customers);
	}

	@Nullable
	public GuestDto getById(@NotEmpty Long id) {

		Guest guest = this.findById(id);
		return guestMapper.map(guest);
	}

	public Guest findByPlatformUserId(String platformUserId) {
		return guestRepository.findByPlatformUserId(platformUserId);
	}
	
	@Nullable
	public Guest findOne(@NotNull Example<Guest> example) {

		return guestRepository.findOne(example).orElse(null);
	}

//	@Nullable
//	public Guest findOneByPlatform(@NotNull Long channelId, @NotNull String userKey) {
//
//		return guestRepository.findOne(Example.of(Guest.builder()
//				.channelId(channelId)
//				.userKey(userKey)
//				.build())).orElse(null);
//	}

	public List<GuestDto> getAll(@NotNull Pageable pageable) {

		Page<Guest> page = guestRepository.findAll(pageable);
		return guestMapper.map(page.getContent());
	}

	public List<GuestDto> getAll(@NotNull Example<Guest> example, @NotNull Pageable pageable) {

		Page<Guest> page = guestRepository.findAll(example, pageable);
		return guestMapper.map(page.getContent());
	}

	public List<Guest> search(String subject, String query) {
		if (!ObjectUtils.isEmpty(subject) && !ObjectUtils.isEmpty(query)) {
			if ("name".equals(subject))
				// KICA-383, 상담 이력 게스트 이름 검색 시(Guest는 핸드폰 번호가 없음)
				return guestRepository.findAllByNameContaining(query);
		}
		return Collections.emptyList();
	}

	/**
	 * 비식별 고객 목록 조회 by 식별 고객 정보
	 */
	private List<Guest> searchByCustomer(String subject, String query) {

		List<Customer> customers = customerService.search(subject, query);
		if (!customers.isEmpty()) {
			return this.findAllByCustomerIn(customers);
		}

		return Collections.emptyList();
	}
	
	/**
	 * BNK 싱크되지 않은 고객, (비식별 고객 목록 조회) 
	 * @param query 
	 * @param subject 
	 */
//	public List<Guest> searchUnsyncedGuests(String subject, String query) {
//	    if (!ObjectUtils.isEmpty(subject) && !ObjectUtils.isEmpty(query)) {
//	        if ("guestId".equals(subject)) {
//	            try {
//	                Long guestId = Long.parseLong(query); // query가 guest ID의 문자열 표현이라고 가정합니다.
//	                return guestRepository.findByCustomerIdIsNullAndId(guestId);
//	            } catch (NumberFormatException e) {
//	                // query에 유효한 long 숫자가 포함되어 있지 않은 경우를 처리합니다.
//	                return Collections.emptyList();
//	            }
//	        }
//	        // 필요에 따라 여기에 다른 `subject`에 대한 조건을 추가합니다.
//	    }
//
//	    return Collections.emptyList();
//	}



	/**
	 * 식별 고객, 비식별 고객 검색
	 */
	public List<Guest> searchGuestAndCustomer(String subject, String query) {
		Set<Guest> guests = new HashSet<>();
		guests.addAll(search(subject, query));
		guests.addAll(searchByCustomer(subject, query));
		log.info("GUEST: {}", guests.stream().map(Guest::getId).collect(Collectors.toList()));
		return new ArrayList<>(guests);
	}

	public Guest save(@NotNull Guest guest) {

		guest = guestRepository.save(guest);
		if (ObjectUtils.isEmpty(guest.getName())) {
			guest.setName("고객" + " " + guest.getId());
			guestRepository.save(guest);
		}

		return guest;
	}

	public List<Guest> findAllByCustomerId(Long customerId) {
		return guestRepository.findAllByCustomerId(customerId);
	}
	
}
