package com.kep.portal.service.customer;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.kep.core.model.dto.customer.CustomerAnniversaryDto;
import com.kep.core.model.dto.customer.CustomerAuthorizedDto;
import com.kep.core.model.dto.customer.CustomerContactDto;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.customer.CustomerMemberDto;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.core.model.dto.platform.AuthorizeType;
import com.kep.portal.client.LegacyClient;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.CustomerAnniversary;
import com.kep.portal.model.entity.customer.CustomerAnniversaryMapper;
import com.kep.portal.model.entity.customer.CustomerAuthorized;
import com.kep.portal.model.entity.customer.CustomerAuthorizedMapper;
import com.kep.portal.model.entity.customer.CustomerContact;
import com.kep.portal.model.entity.customer.CustomerContactMapper;
import com.kep.portal.model.entity.customer.CustomerMapper;
import com.kep.portal.model.entity.customer.CustomerMember;
import com.kep.portal.model.entity.customer.CustomerMemberMapper;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.platform.PlatformSubscribe;
import com.kep.portal.repository.customer.CustomerAnniversaryRepository;
import com.kep.portal.repository.customer.CustomerAuthorizedRepository;
import com.kep.portal.repository.customer.CustomerContactRepository;
import com.kep.portal.repository.customer.CustomerMemberRepository;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.service.platform.PlatformSubscribeService;
import com.kep.portal.util.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private CustomerContactRepository customerContactRepository;

	@Resource
	private CustomerMapper customerMapper;

	@Resource(name = "fixedEncryptor")
	private StringEncryptor defaultEncryptor;

	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private CustomerMemberRepository customerMemberRepository;

	@Resource
	private CustomerContactMapper customerContactMapper;

	@Resource
	private CustomerMemberMapper customerMemberMapper;

	@Resource
	private CustomerAnniversaryMapper customerAnniversaryMapper;

	@Resource
	private CustomerAnniversaryRepository customerAnniversaryRepository;

	@Resource
	private CustomerAuthorizedRepository customerAuthorizedRepository;

	@Resource
	private CustomerAuthorizedMapper customerAuthorizedMapper;

	@Resource
	private PlatformSubscribeService platformSubscribeService;

	@Resource
	private GuestRepository guestRepository;

	@Resource
	private IssueRepository issueRepository;

	@Resource
	private MemberRepository memberRepository;

	@Resource
	private LegacyClient legacyClient;


	/**
	 * 상담원 고객 기념일 목록
	 * @return
	 */
	public List<CustomerDto> anniversaries(){

		List<Customer> entities = this.getAllCustomerMember();

		LocalDate startDate = LocalDate.now().minusWeeks(1);
		LocalDate endDate = LocalDate.now().plusDays(2);

		int startMonth = startDate.getMonthValue();
		int startDay = startDate.getDayOfMonth();
		int endMonth = endDate.getMonthValue();
		int endDay = endDate.getDayOfMonth();

		List<Customer> customers = new ArrayList<>();
		for (Customer entity : entities){
			for (CustomerAnniversary anniversary : entity.getAnniversaries()){
				LocalDate date = anniversary.getAnniversary();
				int dateMonth = date.getMonthValue();
				int dateDay = date.getDayOfMonth();

				boolean isWithinOneWeek = (dateMonth > startMonth || (dateMonth == startMonth && dateDay >= startDay))
						&& (dateMonth < endMonth || (dateMonth == endMonth && dateDay <= endDay));

				if(isWithinOneWeek){
					customers.add(entity);
				}
			}
		}
		if(!customers.isEmpty()){
			return customerMapper.map(this.entire(customers));
		}
		return Collections.emptyList();
	}

	/**
	 * 고객 정보
	 * @param customerId
	 * @return
	 */
	public CustomerDto show(@NotNull @Positive Long id, Long issueId) {
		Customer customer = this.findById(id);
		if(customer == null) {
			throw new RuntimeException("Customer associated with customer ID" + id +"not found");
		}
		// BNK 고객 정보 요청 전에 sendFlag 설정 및 업데이트
		String sendFlag = issueRepository.findById(issueId)
				.map(issue -> issue.getSendFlag())
				.orElse("Y"); // issueId에 해당하는 sendFlag 조회
		try {
			customer.setContacts(this.contactsGetAll(customer));
			customer.setAuthorizeds(this.authorizedGetAll(customer));
			customer.setPlatformSubscribes(platformSubscribeService.getAll(this.getAllPlatformUserId(customer.getAuthorizeds())));
			List<IssueExtra> extras = this.getAllInflow(customer);
			customer.setInflows(this.getAllInflow(customer));

			//싱크없는 고객 체크
			CustomerDto customerDto = customerMapper.map(customer);
			if(!ObjectUtils.isEmpty(customerDto.getAuthorizeds()) && customerDto.getAuthorizeds().size() > 0){
				customerDto.getAuthorizeds().stream().filter(item ->
						item.getType().equals(AuthorizeType.kakao_sync)).count();
			}

			// BNK 응답데이터 => custNo와 custNm 추출 및 customer_guest DB에 저장
			LegacyCustomerDto legacyCustomerDto = legacyClient.getCustomerInfo(customerDto,sendFlag);
			log.info("[알림요청정보: [{}]▶▶▶:::BNK Response Data : {}]", sendFlag, legacyCustomerDto);

			String custNo = legacyCustomerDto.getCustNo();
			String custName = legacyCustomerDto.getCustNm();

			// custNo가 비어있는 경우에 대한 처리
			if (custNo == null || custNo.trim().isEmpty()) {
				custNo = "9999999";
				log.warn("CustNo가 null이거나 빈 문자열입니다. 임시 고객 ID: {} 가 할당되었습니다.", custNo);
			}

			// 데이터베이스에서 해당 고객을 조회
			Guest guest = guestRepository.findByCustomer(customer);
			if (guest == null) {
				guest = new Guest();
				guest.setCustomer(customer);
			}

			guest.setCustNo(custNo); // 고객 번호 저장
			log.info("[API 통신 후 받아온 고객명]: {}", custName); // API 통신 후 받아온 고객명
			customer.setName(custName); // 고객 이름 저장
			guestRepository.save(guest); // 변경 사항을 DB에 저장
			customerDto.setLegacyCustomerData(legacyCustomerDto);

			// 고객 정보가 성공적으로 처리된 경우, sendFlag를 'N'으로 업데이트
			updateIssueSendFlag(issueId, "N");
			return customerDto;
		} catch (Exception e) {
			log.error("Error occurred while processing customer details for guest ID: {}", id, e);
			throw new RuntimeException("Error occurred while processing customer details", e);
		}
	}
	// BNK sendFlag 값 업데이트 메서드
	private void updateIssueSendFlag(Long issueId, String updateFlag) {
		if (issueId != null && issueRepository.findById(issueId).isPresent()) {
			Issue issue = issueRepository.findById(issueId).get();
			if ("Y".equals(issue.getSendFlag())) {
				issue.setSendFlag(updateFlag);
				issueRepository.save(issue);
				log.info("Issue ID {}의 sendFlag를 '{}'으로 업데이트 완료", issueId, updateFlag);
			} else {
				log.info("Issue ID {}의 sendFlag는 이미 '{}' 상태입니다.", issueId, issue.getSendFlag());
			}
		} else {
			log.warn("updateIssueSendFlag: issueId가 null입니다.");
		}
	}

	/**
	 * 계약 정보에 대한 서비스 구현 메서드.
	 * 입력된 계약 번호(cntrtNum)를 통해 LegacyClient를 사용하여
	 * 외부 API에서 계약 정보를 가져온다.
	 *
	 * @param cntrtNum 계약 번호
	 * @return LegacyCustomerDto.CresData 계약 정보 응답 데이터
	 */
	public LegacyCustomerDto.CresData getContractByCntrtNum(String cntrtNum) {
		// API 호출을 통해 응답 데이터 가져오기
		LegacyCustomerDto.CresData response = legacyClient.getContractInfo(cntrtNum);

		// 데이터 유효성 검사
		if (response == null) {
			throw new RuntimeException("Failed to retrieve response data from the API.");
		}
		if (response.getCntrtNum() == null || response.getCntrtNum().trim().isEmpty()) {
			throw new RuntimeException("The cntrtNum[계약번호] returned from the API is invalid.");
		}
		return response;
	}

	private List<Customer> entire(@NotNull List<Customer> entities){
		Set<Long> customerIds = this.getAllCustomerId(entities);
		List<CustomerAuthorized> customerAuthorizeds = customerAuthorizedRepository.findAllByCustomerIdIn(customerIds);
		if(!customerAuthorizeds.isEmpty()){
			List<Map<Long , List<PlatformSubscribe>>> getAllPlatformSubscribe = this.getAllPlatformSubscribe(customerAuthorizeds);
			if(getAllPlatformSubscribe != null && !entities.isEmpty()){
				for (Customer entity : entities){
					for (Map<Long, List<PlatformSubscribe>> entry : getAllPlatformSubscribe){
						boolean isCustomerId = entry.containsKey(entity.getId());
						if(isCustomerId){
							List<PlatformSubscribe> platformSubscribeList =
									entry.values().stream()
											.flatMap(List::stream)
											.collect(Collectors.toList());

							entity.setPlatformSubscribes(platformSubscribeList);
						}
					}
				}
			}
		}

		return entities;

	}

	/**
	 * 유입 경로
	 * @param customer
	 * @return
	 */
	public List<IssueExtra> getAllInflow(@NotNull Customer customer){
		List<Guest> guests = guestRepository.findAll(Example.of(Guest.builder().customer(customer).build()));
		if(!guests.isEmpty()){
			return issueRepository.findAllByGuestInAndIssueExtraIsNotNullOrderByCreatedDesc(guests)
					.stream()
					.map(Issue::getIssueExtra)
					.filter(issueExtra -> issueExtra.getInflow() != null)
					.collect(Collectors.toList());

		}
		return Collections.emptyList();
	}

	public Page<Customer> getAllCustomer(@NotNull Pageable pageable){
		return customerRepository.findAll(pageable);
	}


	/**
	 * 고객 연락처 목록
	 * @param entity
	 * @return
	 */
	public List<CustomerContact> contactsGetAll(@NotNull Customer entity){
		if(entity != null){
			return customerContactRepository.findAllByCustomerId(entity.getId());
		}
		return Collections.emptyList();
	}

	/**
	 * 고객 연락처 목록
	 * @param entity
	 * @return
	 */
	public List<CustomerContactDto> contacts(@NotNull Customer entity){
		return customerContactMapper.map(this.contactsGetAll(entity));
	}

	/**
	 * 고객 정보 저장
	 * @param dto
	 */
	public Customer save(CustomerDto dto){

		Customer customer = customerRepository.findOne(
						Example.of(Customer.builder().identifier(dto.getIdentifier()).build()))
				.orElse(null);
		if(customer == null){
			customer = customerMapper.map(dto);
		}else {
			log.info("Customer ID: {}", customer.getId());
		}
		customer.setAge(dto.getAge());
		customer.setName(dto.getName());

		// 개인정보
		customer = customerRepository.save(customer);
		customerRepository.flush();

		this.contactStore(customer, dto.getContacts());
		this.anniversaryStore(customer, dto.getAnniversaries());
		return customer;
	}

	/**
	 * 고객 정보 저장
	 */
	public CustomerDto store(@NotNull CustomerDto dto) {

		Customer customer = customerRepository.save(customerMapper.map(dto));
		List<CustomerContact> contacts = customerContactMapper.mapDto(dto.getContacts());
		for (CustomerContact contact : contacts) {
			contact.setCustomerId(customer.getId());
		}

		contacts = customerContactRepository.saveAll(contacts);
		customer.setContacts(contacts);

		CustomerMember customerMember = CustomerMember.builder()
				.customer(customer)
				.memberId(securityUtils.getMemberId())
				.favorite(false)
				.build();
		customerMemberRepository.save(customerMember);

		return customerMapper.map(customer);
	}

	/**
	 * 연락처 저장
	 * @param entity
	 * @param dtos
	 * @return
	 */
	public List<CustomerContactDto> contactStore(@NotNull Customer entity , List<CustomerContactDto> dtos){
		if(entity != null && !dtos.isEmpty()){
			List<CustomerContact> entities = new ArrayList<>();
			for (CustomerContactDto dto : dtos){
				entities.add(CustomerContact.builder()
						.customerId(entity.getId())
						.type(dto.getType())
						.payload(dto.getPayload())
//						.payload(defaultEncryptor.encrypt(dto.getPayload()))
						.build());
			}

			if(!entities.isEmpty()){
				List<CustomerContact> customerContacts = customerContactRepository.findAllByCustomerId(entity.getId());
				if(!customerContacts.isEmpty()){
					customerContactRepository.deleteByCustomerId(entity.getId());
					customerContactRepository.flush();
				}
				customerContacts = customerContactRepository.saveAll(entities);
				return customerContactMapper.map(customerContacts);
			}
		}

		return Collections.emptyList();

	}

	/**
	 * 상담원 고객 즐겨 찾기
	 * @param dto
	 * @return
	 */
	public CustomerMemberDto favoritesStore(CustomerMemberDto dto){

		Customer customer = customerRepository.findById(dto.getCustomerId()).orElse(null);
		if(customer == null){
			return null;
		}
		CustomerMember customerMember = customerMemberRepository.findOne(
						Example.of(CustomerMember.builder()
								.memberId(securityUtils.getMemberId())
								.customer(customer)
								.build()))
				.orElse(null);

		//내 고객에 있으면
		if(customerMember != null){
			if(customerMember.getFavorite() == false){
				customerMember.setFavorite(true);
			} else {
				customerMember.setFavorite(false);
			}
			customerMember = customerMemberRepository.save(customerMember);
		}

		return customerMemberMapper.map(customerMember);
	}

	/**
	 * 상담원 고객목록 가져오기
	 * @return
	 */
	public List<Customer> getAllCustomerMember(){
		Long memberId = securityUtils.getMemberId();
		return customerMemberRepository.findAll(Example.of(
				CustomerMember.builder()
						.memberId(memberId)
						.build()
		)).stream().map(CustomerMember::getCustomer).collect(Collectors.toList());
	}


	/**
	 * 상담원 고객 즐겨 찾기
	 * @return
	 */
	public List<CustomerDto> favorites(){
		List<Customer> entities =
				customerMemberRepository.findAllByMemberIdAndFavorite(securityUtils.getMemberId(), true)
						.stream().map(CustomerMember::getCustomer)
						.collect(Collectors.toList());
		return customerMapper.map(this.entire(entities));
	}

	@Override
	public Customer findById(Long id) {
		return customerRepository.findById(id).orElse(null);
	}

	@Override
	public List<Customer> search(String subject, String query) {

		if (!ObjectUtils.isEmpty(subject) && !ObjectUtils.isEmpty(query)) {
			if ("name".equals(subject)) {
				return customerRepository.findAllByName(query);
			}
		}

		return Collections.emptyList();
	}

	@Nullable
	public CustomerAnniversary findOne(@NotNull Example<CustomerAnniversary> example) {
		return customerAnniversaryRepository.findOne(example).orElse(null);
	}


	/**
	 * 최근 대화 목록
	 */
	public List<CustomerDto> issueLatest(){
		List<Customer> entities = issueRepository.latestCustomers(securityUtils.getMemberId());
		if(!entities.isEmpty()){
			return customerMapper.map(this.entire(entities));
		}
		return Collections.emptyList();
	}

	public List<CustomerDto> getAll(@NotNull Pageable pageable , Long memberId , boolean favorite) {
		Page<Customer> page = null;

		if(memberId == null){
			page = customerRepository.findAll(pageable);
		} else {
			List<CustomerMember> customerMembers = customerMemberRepository.findAllByMemberIdAndFavorite(memberId , favorite);
			page = new PageImpl<>(customerMembers.stream()
					.map(CustomerMember::getCustomer)
					.collect(Collectors.toList()));
		}


		List<Customer> customers = page.getContent();
		if(!page.getContent().isEmpty()){
			Set<Long> customerIds = this.getAllCustomerId(page.getContent());
			List<CustomerAuthorized> customerAuthorizeds = customerAuthorizedRepository.findAllByCustomerIdIn(customerIds);
			List<Map<Long , List<PlatformSubscribe>>> getAllPlatformSubscribe = this.getAllPlatformSubscribe(customerAuthorizeds);
			if(!getAllPlatformSubscribe.isEmpty()){
				for (Customer entity : customers){
					for (Map<Long, List<PlatformSubscribe>> entry : getAllPlatformSubscribe){
						boolean isCustomerId = entry.containsKey(entity.getId());
						if(isCustomerId){
							List<PlatformSubscribe> platformSubscribeList =
									entry.values().stream()
											.flatMap(List::stream)
											.collect(Collectors.toList());

							entity.setPlatformSubscribes(platformSubscribeList);
						}
					}
				}
			}
		}

		return customerMapper.map(page.getContent());
	}

	/**
	 * 고객 상담원
	 * @param entity
	 * @param memberId
	 */
	public void customerMemberStore(@NotNull Customer entity , @NotNull Long memberId){
		if(entity != null  && memberId > 0){
			CustomerMember customerMember = customerMemberRepository.findOne(Example.of(
					CustomerMember.builder()
							.memberId(memberId)
							.customer(entity)
							.build())
			).orElse(null);

			if(customerMember == null){
				customerMemberRepository.save(CustomerMember.builder()
						.memberId(memberId)
						.customer(entity)
						.favorite(false)
						.build());
			}
		}
	}


	/**
	 * 기념일
	 * @param entity
	 * @param dto
	 * @return
	 */
	public List<CustomerAnniversaryDto> anniversaryStore(@NotNull Customer entity , List<CustomerAnniversaryDto> dto){
		if(entity != null && !dto.isEmpty()){
			List<CustomerAnniversary> entities = new ArrayList<>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			for (CustomerAnniversaryDto dtos : dto){
				LocalDate anniversary = LocalDate.parse(dtos.getAnniversary(), formatter);
				entities.add(CustomerAnniversary.builder()
						.customerId(entity.getId())
						.type(dtos.getType())
						.anniversary(anniversary)
						.created(ZonedDateTime.now())
						.build());
			}

			if(!entities.isEmpty()){
				List<CustomerAnniversary> customerAnniversarys = customerAnniversaryRepository.findAllByCustomerId(entity.getId());
				if(!customerAnniversarys.isEmpty()){
					customerAnniversaryRepository.deleteByCustomerId(entity.getId());
					customerAnniversaryRepository.flush();;
				}
				customerAnniversarys = customerAnniversaryRepository.saveAll(entities);
				return customerAnniversaryMapper.map(customerAnniversarys);
			}

		}
		return Collections.emptyList();
	}

	/**
	 * 고객 인증 히스토리
	 * @param entity
	 * @param dto
	 * @return
	 */
	public List<CustomerAuthorizedDto> authorizedStore(@NotNull Customer entity ,@NotNull CustomerAuthorizedDto dto){
		if(entity != null){
			dto.setCreated(ZonedDateTime.now());
			dto.setCustomerId(entity.getId());
			customerAuthorizedRepository.save(customerAuthorizedMapper.map(dto));
			return this.authorizeds(entity);
		}
		return null;
	}

	/**
	 * 고객 인증 히스토리
	 * @param entity
	 * @return
	 */
	public List<CustomerAuthorizedDto> authorizeds(@NotNull Customer entity){
		return customerAuthorizedMapper.map(this.authorizedGetAll(entity));
	}

	/**
	 * 고객 인증 히스토리
	 * @param entity
	 * @return
	 */
	public List<CustomerAuthorized> authorizedGetAll(@NotNull Customer entity){
		if(entity != null){
			return customerAuthorizedRepository.findAllByCustomerId(entity.getId());
		}
		return Collections.emptyList();
	}

	/**
	 * 플랫폼 앱 USER PK
	 * @param entities
	 * @return
	 */
	public Set<String> getAllPlatformUserId(List<CustomerAuthorized> entities){
		if(!entities.isEmpty()){
			return entities.stream()
					.filter(item->item.getPlatformUserId() != null)
					.collect(Collectors.groupingBy(CustomerAuthorized::getPlatformUserId))
					.keySet();
		}
		return Collections.emptySet();
	}

	/**
	 * 플랫폼 구독
	 * @param entities
	 * @return
	 */
	public List<Map<Long , List<PlatformSubscribe>>> getAllPlatformSubscribe(@NotNull List<CustomerAuthorized> entities) {
		if(!entities.isEmpty()) {
			Set<String> getAllPlatformUserId = this.getAllPlatformUserId(entities);
			List<PlatformSubscribe> platformSubscribeList = platformSubscribeService.getAll(getAllPlatformUserId);
			Map<Long, Set<String>> authorized = entities.stream()
					.filter(item->item.getCustomerId() != null && item.getPlatformUserId() != null)
					.collect(Collectors.groupingBy(CustomerAuthorized::getCustomerId
							, Collectors.mapping(CustomerAuthorized::getPlatformUserId, Collectors.toSet())));

			List<Map<Long , List<PlatformSubscribe>>> platformSubscribes = new ArrayList<>();

			for (Map.Entry<Long, Set<String>> entity : authorized.entrySet()) {
				Map<Long , List<PlatformSubscribe>> platformSubscribe = new HashMap<>();
				List<PlatformSubscribe> subscribes = new ArrayList<>();
				if(!platformSubscribeList.isEmpty()){
					for (String platfromUserId : entity.getValue()){
						subscribes = platformSubscribeList.stream()
								.filter(item->item.getPlatformUserId().equals(platfromUserId))
								.collect(Collectors.toList());
					}
				}

				if(!subscribes.isEmpty()){
					platformSubscribe.put(entity.getKey(), subscribes);
					platformSubscribes.add(platformSubscribe);
				}
			}
			return platformSubscribes;
		}
		return Collections.emptyList();
	}


	/**
	 * 고객 아이디 customer pk
	 * @param entities
	 * @return
	 */
	public Set<Long> getAllCustomerId(List<Customer> entities){
		if(!entities.isEmpty()){
			return entities.stream()
					.collect(Collectors.groupingBy(Customer::getId))
					.keySet();
		}
		return Collections.emptySet();
	}

}
