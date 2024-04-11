package com.kep.portal.repository.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.portal.config.ObjectMapperConfig;
import com.kep.portal.config.QueryDslConfig;
import com.kep.portal.model.entity.customer.Guest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class GuestRepositoryTest {

	@TestConfiguration
	public static class ContextConfig {
		@Bean
		public JPAQueryFactory queryFactory(EntityManager entityManager) {
			return new QueryDslConfig().jpaQueryFactory(entityManager);
		}
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
	}

	@Resource
	private GuestRepository guestRepository;
	@Resource
	private EntityManager entityManager;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("hashcode 검증")
	void testHashcode() throws Exception {

		Guest guest1 = guestRepository.save(Guest.builder()
				.channelId(1L)
				.userKey("user_key")
				.created(ZonedDateTime.now())
				.build());

		Guest guest2 = guestRepository.save(Guest.builder()
				.channelId(1L)
				.userKey("user_key")
				.created(ZonedDateTime.now())
				.build());

		assertNotEquals(guest1.hashCode(), guest2.hashCode());
	}

	@Test
	@DisplayName("equal 검증")
	void testEqual() throws Exception {

		Guest guest1 = guestRepository.save(Guest.builder()
				.channelId(1L)
				.userKey("user_key")
				.created(ZonedDateTime.now())
				.build());

		Guest guest2 = guestRepository.save(Guest.builder()
				.channelId(1L)
				.userKey("user_key")
				.created(ZonedDateTime.now())
				.build());

		assertNotEquals(guest1, guest2);

		Guest guest3 = guestRepository.findById(guest1.getId()).orElse(null);
		assertNotNull(guest3);
		assertEquals(guest1, guest3);
	}

	@Test
	void testFindAllByNameContaining() throws Exception {

		guestRepository.save(Guest.builder()
				.channelId(1L)
				.userKey("user_key")
				.name("고객1234")
				.created(ZonedDateTime.now())
				.build());

		List<Guest> guests = guestRepository.findAllByNameContaining("고객1234");
		log.info(objectMapper.writeValueAsString(guests));
		assertNotNull(guests);
		assertEquals(1, guests.size());
	}
}
