package com.kep.portal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.repository.customer.CustomerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
class JasyptConfigTest {

	@TestConfiguration
	static class ContextConfig {
		@Bean
		public JPAQueryFactory queryFactory(EntityManager entityManager) {
			return new QueryDslConfig().jpaQueryFactory(entityManager);
		}
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapperConfig().objectMapper();
		}
		@Bean(name = "defaultEncryptor")
		@Primary
		public StringEncryptor defaultEncryptor() {
			return new JasyptConfig().defaultEncryptor();
		}
		@Bean(name = "fixedEncryptor")
		public StringEncryptor fixedEncryptor() {
			return new JasyptConfig().fixedEncryptor();
		}
		@Bean(name = "propertyEncryptor")
		public StringEncryptor propertyEncryptor() {
			return new JasyptConfig().propertyEncryptor();
		}
	}

	@Resource(name = "defaultEncryptor")
	private StringEncryptor defaultEncryptor;
	@Resource(name = "fixedEncryptor")
	private StringEncryptor fixedEncryptor;
	@Resource(name = "propertyEncryptor")
	private StringEncryptor propertyEncryptor;
	@Resource
	private CustomerRepository customerRepository;
	@Resource
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("암호화, 복호화")
	void testStringEncryptor() {

		String originalText = "sa";

		// Default Encryptor
		String encryptedTextByDefault = defaultEncryptor.encrypt(originalText);
		log.info("encryptedText By Default: {}", encryptedTextByDefault);
		assertEquals(originalText, defaultEncryptor.decrypt(encryptedTextByDefault));

		// Encryptor for Property File
		String encryptedTextByProperties = propertyEncryptor.encrypt(originalText);
		log.info("encryptedText By Properties: {}", encryptedTextByProperties);
		assertEquals(originalText, propertyEncryptor.decrypt(encryptedTextByProperties));
	}

	@Test
	@DisplayName("'fixedEncryptor'로 암호화시, 항상 같은 값을 반환")
	void testDefaultEncryptor() {

		String originalText = "코드클릭 개발팀";
		String encryptedText = null;

		for (int i = 0; i < 10; i++) {
			String encryptedTextByDefault = fixedEncryptor.encrypt(originalText);
			log.info("encryptedText By Default: {}", encryptedTextByDefault);
			assertEquals(originalText, fixedEncryptor.decrypt(encryptedTextByDefault));

			if (encryptedText != null) {
				assertEquals(encryptedText, encryptedTextByDefault);
			}
			encryptedText = encryptedTextByDefault;
		}
	}

	@Test
	void testCustomerEntity() throws Exception {

		customerRepository.save(Customer.builder()
						.name("코드클릭")
						.age("16")
				.build());
		customerRepository.flush();

		List<Customer> searchedCustomers = customerRepository.findAllByName("코드클릭");
		for (Customer searchedCustomer : searchedCustomers) {
			log.info("{}", objectMapper.writeValueAsString(searchedCustomer));
		}
	}
}
