package com.kep.portal.service.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.customer.GuestRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class GuestServiceTest {

	@Resource
	private GuestService guestService;

	@Resource
	private GuestRepository guestRepository;
	@Resource
	private CustomerRepository customerRepository;
	@Resource
	private ChannelRepository channelRepository;
	@PersistenceContext
	private EntityManager entityManager;
	@Resource
	private ObjectMapper objectMapper;

	@BeforeEach
	void beforeEach() throws Exception {

		Channel channel = channelRepository.save(Channel.builder()
				.name("UNIT_TEST_CHANNEL")
				.platform(PlatformType.kakao_counsel_talk)
				.serviceId("TEST_SERVICE_ID")
				.serviceKey("TEST_SERVICE_KEY")
				.modifier(1L)
				.modified(ZonedDateTime.now())
				.build());
		channelRepository.flush();

		// 식별 고객1
		Customer customer1 = customerRepository.save(Customer.builder()
				.name("MOCK_CUSTOMER_1")
				.build());
		// 식별 고객2
		Customer customer2 = customerRepository.save(Customer.builder()
				.name("TEST_CUSTOMER_2")
				.build());
		customerRepository.flush();

		// 비식별 고객1
		guestRepository.save(Guest.builder()
				.channelId(channel.getId())
				.userKey("UNIT_TEST_USER_KEY_1")
				.name("MOCK_GUEST_1")
				.customer(customer1)
				.created(ZonedDateTime.now())
				.build());
		// 비식별 고객2
		guestRepository.save(Guest.builder()
				.channelId(channel.getId())
				.userKey("UNIT_TEST_USER_KEY_2")
				.name("MOCK_GUEST_2")
				.customer(customer2)
				.created(ZonedDateTime.now())
				.build());
		// 비식별 고객3
		guestRepository.save(Guest.builder()
				.channelId(channel.getId())
				.userKey("UNIT_TEST_USER_KEY_3")
				.name("TEST_GUEST_3")
				.created(ZonedDateTime.now())
				.build());
		guestRepository.flush();

		entityManager.clear();
	}

	@Test
	void searchGuestAndCustomer() throws Exception {

		List<Guest> guests = guestService.search("name", "TEST_");
		log.info(objectMapper.writeValueAsString(guests));
		assertFalse(guests.isEmpty());
		assertEquals(1, guests.size());

		guests = guestService.search("name", "MOCK_");
		log.info(objectMapper.writeValueAsString(guests));
		assertFalse(guests.isEmpty());
		assertEquals(2, guests.size());

		guests = guestService.searchGuestAndCustomer("name", "TEST_");
		assertFalse(guests.isEmpty());
		assertEquals(1, guests.size());

		guests = guestService.searchGuestAndCustomer("name", "MOCK_");
		assertFalse(guests.isEmpty());
		assertEquals(2, guests.size());
	}
}