package com.kep.portal.service.customer;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.customer.*;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.core.model.dto.platform.AuthorizeType;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.client.LegacyClient;
import com.kep.portal.model.dto.customer.request.PatchCustomerRequestDto;
import com.kep.portal.model.dto.customer.request.PostCustomerRequestDto;
import com.kep.portal.model.dto.customer.request.PatchFavoriteCustomerRequestDto;
import com.kep.portal.model.dto.customer.response.*;
import com.kep.portal.model.entity.customer.*;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.issue.IssueMapper;
import com.kep.portal.model.entity.platform.PlatformSubscribe;
import com.kep.portal.repository.customer.*;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.service.platform.PlatformSubscribeService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
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

	private final IssueMapper issueMapper;
	private final CustomerGroupRepository customerGroupRepository;

	/** Message Source Util **/
	private final MessageSourceUtil messageUtil;

	/**
	 * 고객 정보
	 * @param id
	 * @return
	 */
	@Transactional
	public CustomerDto show(@NotNull @Positive Long id) {
		Customer customer = this.findById(id);
		if(customer == null) {
			throw new RuntimeException("Customer associated with customer ID" + id +"not found");
		}

		try {
			customer.setContacts(this.contactsGetAll(customer));
			customer.setAuthorizeds(this.authorizedGetAll(customer));
			customer.setPlatformSubscribes(platformSubscribeService.getAll(this.getAllPlatformUserId(customer.getAuthorizeds())));
			List<IssueExtra> extras = this.getAllInflow(customer);
			customer.setInflows(this.getAllInflow(customer));

			// 싱크없는 고객 체크
			CustomerDto customerDto = customerMapper.map(customer);
			if(!ObjectUtils.isEmpty(customerDto.getAuthorizeds()) && customerDto.getAuthorizeds().size() > 0){
				customerDto.getAuthorizeds().stream().filter(item ->
						item.getType().equals(AuthorizeType.kakao_sync)).count();
			}

			// 소속된 그룹 존재 유무 확인
			if (customer.getCustomerGroup() != null) {
				customerDto.setCustomerGroupId(customer.getCustomerGroup().getId());
			}

			// 고객의 마지막 대화 이력 확인(고객 상세 조회용)
			Issue issue = issueRepository.findTopByCustomerIdOrderByStatusModifiedDesc(customerDto.getId())
					.orElseThrow(() -> new RuntimeException("Customer last issue not founr : " + customerDto.getId()));
			customerDto.setLastIssueId(issue.getId());

			return customerDto;
		} catch (Exception e) {
			log.error("Error occurred while processing customer details for guest ID: {}", id, e);
			throw new RuntimeException("Error occurred while processing customer details", e);
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
	 * @param customerDto
	 */
	public Customer save(CustomerDto customerDto){
		Customer customer = customerRepository.findOne(
						Example.of(Customer.builder().identifier(customerDto.getIdentifier()).build()))
				.orElse(null);

		if(customer == null){
			customer = customerMapper.map(customerDto);
		}

		CommonUtils.copyProperties(customerDto, customer);

		// 고객 정보 저장
		customerRepository.save(customer);
		customerRepository.flush();

		// 고객 개인 정보 저장
		this.contactStore(customer, customerDto.getContacts());
		this.anniversaryStore(customer, customerDto.getAnniversaries());

		return customer;
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
				// 이름 검색 시
				return customerRepository.findAllByName(query);
			} else {
				// 전화번호 & 이메일 검색 시
				CustomerContactType type = "phone".equals(subject) ? CustomerContactType.call : CustomerContactType.email;
				List<CustomerContact> contactList = customerContactRepository.findCustomerContactsByType(type)
						.stream().filter(contact -> contact.getPayload().contains(query)).collect(Collectors.toList());
				return customerRepository.findAllByIdIn(contactList.stream()
						.map(contact -> contact.getCustomerId()).collect(Collectors.toList()));
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

	/** V2 Service Methods **/
	/**
	 * 고객 정보 저장
	 */
	@Override
	public ResponseEntity<? super PostCustomerResponseDto> createCustomer(PostCustomerRequestDto requestDto) {
		// 고객 등록 시 선택된 그룹 ID로 고객 그룹 조회
		boolean existedCustomerGroup = customerGroupRepository.existsById(requestDto.getCustomerGroupId());
		if (!existedCustomerGroup) return ResponseDto.notExistedCustomerGroup(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER_GROUP));

		CustomerGroup customerGroup = customerGroupRepository.findById(requestDto.getCustomerGroupId()).get();

		Customer customer = customerRepository.save(Customer.builder()
				.name(requestDto.getName())
				.customerGroup(customerGroup)
				.build());

		// 고객 Contact 데이터 추가
		List<CustomerContact> contactList = Optional.ofNullable(requestDto.getContacts())
				.orElse(Collections.emptyList())
				.stream()
				.map(contact -> CustomerContact.builder()
						.customerId(customer.getId())
						.type(contact.getType())
						.payload(contact.getPayload())
						.build()
				).collect(Collectors.toList());
		customerContactRepository.saveAll(contactList);

		// 고객-상담원 ID 연결 & 실제 저장할 CustomerMember 객체 생성 및 저장
		if (securityUtils.getMemberId() == null) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));

		customerMemberRepository.save(CustomerMember.builder()
				.customer(customer)
				.memberId(securityUtils.getMemberId())
				.favorite(false)
				.build());

		return PostCustomerResponseDto.success(messageUtil.success());
	}

	/**
	 * 즐겨찾기 고객 수정
	 * @param requestDto
	 * @return
	 */
	@Override
	public ResponseEntity<? super PatchFavoriteCustomerResponseDto> patchFavoriteCustomer(PatchFavoriteCustomerRequestDto requestDto) {
		boolean existedByCustomer = customerRepository.existsById(requestDto.getCustomerId());
		if (!existedByCustomer) return ResponseDto.notExistedCustomer(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER));

		CustomerMember customerMember = customerMemberRepository.findOne(
				Example.of(CustomerMember.builder()
						.memberId(requestDto.getMemberId())
						.customer(Customer.builder()
								.id(requestDto.getCustomerId())
								.build())
						.build()
				)
		).orElse(null);
		if (customerMember == null) return PatchFavoriteCustomerResponseDto.notExistedCustomerMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_DATA));

		customerMember.setFavorite(!customerMember.getFavorite());

		return PatchFavoriteCustomerResponseDto.success(messageUtil.success());
	}

	/**
	 * 고객 정보 전체 조회
	 * @return
	 */
	@Override
	public ResponseEntity<? super GetCustomerListResponseDto> findAllCustomer(Long memberId) {
		boolean existedByMemberId = memberRepository.existsById(memberId);
		if (!existedByMemberId) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));

		List<CustomerDto> customerDtoList = customerMemberRepository.findAllByMemberId(memberId)
				.stream().map(customerMember -> customerMapper.map(customerMember.getCustomer())).collect(Collectors.toList());

		return GetCustomerListResponseDto.success(customerDtoList, messageUtil.success());
	}

	/**
	 * 고객 정보 단건 조회
	 * TODO : 객체 <-> 엔티티간 전환 내용 정리 후 리팩토링
	 */
	@Override
	public ResponseEntity<? super GetCustomerResponseDto> findCustomer(Long customerId) {
		boolean existedByCustomerId = customerRepository.existsById(customerId);
		if (!existedByCustomerId) return ResponseDto.notExistedCustomer(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER));

		Customer customer = customerRepository.findById(customerId).get();
		customer.setContacts(this.contactsGetAll(customer));
		customer.setAuthorizeds(this.authorizedGetAll(customer));
		customer.setPlatformSubscribes(platformSubscribeService.getAll(this.getAllPlatformUserId(customer.getAuthorizeds())));
		customer.setInflows(this.getAllInflow(customer));

		CustomerDto customerDto = customerMapper.map(customer);
		if(!ObjectUtils.isEmpty(customerDto.getAuthorizeds()) && customerDto.getAuthorizeds().size() > 0){
			customerDto.getAuthorizeds().stream().filter(item ->
					item.getType().equals(AuthorizeType.kakao_sync)).count();
		}

		// 소속된 그룹 존재 유무 확인
		if (customer.getCustomerGroup() != null) {
			customerDto.setCustomerGroupId(customer.getCustomerGroup().getId());
		}

		return GetCustomerResponseDto.success(customerDto, messageUtil.success());
	}

	/**
	 * 즐겨찾기 고객 목록 조회
	 * @param memberId
	 * @return
	 */
	@Override
	public ResponseEntity<? super GetFavoriteCustomerListResponseDto> findAllFavoriteCustomerList(Long memberId) {
		boolean existedByMemberId = memberRepository.existsById(memberId);
		if (!existedByMemberId) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));

		List<CustomerDto> favoriteCustomerDtoList = customerMapper.map(this.entire(
				customerMemberRepository.findAllByMemberIdAndFavorite(memberId, true)
				.stream()
				.map(CustomerMember::getCustomer)
				.collect(Collectors.toList())));

		return GetFavoriteCustomerListResponseDto.success(favoriteCustomerDtoList, messageUtil.success());
	}

	@Override
	public ResponseEntity<? super GetAnniversariesCustomerListResponseDto> findAllAnniversariesCustomerList(Long memberId) {
		boolean existedByMemberId = memberRepository.existsById(memberId);
		if (!existedByMemberId) return ResponseDto.notExistedMember(messageUtil.getMessage(MessageCode.NOT_EXISTED_MEMBER));

		LocalDate today = LocalDate.now();
		LocalDate startDate = today.minusDays(7);
		LocalDate endDate = today.plusDays(7);
		List<CustomerDto> customerList = customerRepository.findCustomersWithAnniversaries(memberId, startDate, endDate)
				.stream().map(customer -> customerMapper.map(customer)).collect(Collectors.toList());

		return GetAnniversariesCustomerListResponseDto.success(customerList, messageUtil.success());
	}

	/**
	 * 고객 정보 수정
	 */
	@Override
	public ResponseEntity<? super PatchCustomerResponseDto> updateCustomer(PatchCustomerRequestDto requestDto) {
		// 변경 수정할 Group 조회
		boolean existedByCustomerGroupId = customerGroupRepository.existsById(requestDto.getCustomerGroupId());
		if (!existedByCustomerGroupId) return ResponseDto.notExistedCustomerGroup(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER_GROUP));

		// 고객 정보 조회
		boolean existedByCustomerId = customerRepository.existsById(requestDto.getId());
		if (!existedByCustomerId) return ResponseDto.notExistedCustomer(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER));

		Customer customer = customerRepository.findById(requestDto.getId()).get();

		// 고객 관리 그룹이 추가/변경 되었을 경우에만 데이터 변경
		if (customer.getCustomerGroup() == null || !customer.getCustomerGroup().getId().equals(requestDto.getCustomerGroupId())) {
			CustomerGroup customerGroup = customerGroupRepository.findById(requestDto.getCustomerGroupId()).get();
			customer.setCustomerGroup(customerGroup);
		}

		// 고객 데이터 저장
		customerRepository.save(customer);

		return PatchCustomerResponseDto.success(messageUtil.success());
	}

	/**
	 * 고객 정보 삭제
	 */
	@Override
	public ResponseEntity<? super DeleteCustomerResponseDto> deleteCustomer(Long customerId) {
		boolean existedByCustomerId = customerRepository.existsById(customerId);
		if (!existedByCustomerId) return ResponseDto.notExistedCustomer(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER));

		// 고객 관련 데이터 삭제
		customerContactRepository.deleteByCustomerId(customerId);
		customerAuthorizedRepository.deleteByCustomerId(customerId);
		customerAnniversaryRepository.deleteByCustomerId(customerId);
		customerMemberRepository.deleteByCustomerId(customerId);

		// Guest와 Customer 관계 끊기
		guestRepository.findAllByCustomerId(customerId).stream().forEach(customer -> customer.setCustomer(null));

		// FIXME : 추가로 식별되는 Customer 비즈니스가 있다면 추가할 것

		// 고객 정보 삭제
		customerRepository.deleteById(customerId);

		return DeleteCustomerResponseDto.success(messageUtil.success());
	}

}
