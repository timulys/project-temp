package com.kep.portal.repository.issue;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.kep.core.model.dto.issue.IssueType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.type.IssueStorageType;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, IssueSearchRepository {



	List<Issue> findAllByMemberInAndCreatedBetweenOrderByIdDesc(@NotNull List<Member> members , ZonedDateTime start , ZonedDateTime end);

	@Override
	@EntityGraph(attributePaths = { "channel", "guest", "issueCategory", "issueExtra", "lastIssueLog" })
	Optional<Issue> findById(@NotNull @Positive Long id);

	@Override
	@EntityGraph(attributePaths = { "channel", "guest", "issueCategory", "issueExtra", "lastIssueLog" })
	Page<Issue> findAll(@NotNull Example example, @NotNull Pageable pageable);

	@EntityGraph(attributePaths = { "channel", "guest", "issueCategory", "issueExtra", "lastIssueLog" })
	Page<Issue> findAllByBranchIdAndStatusIn(@NotNull Long branchId, List<IssueStatus> statuses, @NotNull Pageable pageable);

	@EntityGraph(attributePaths = { "channel", "guest", "issueCategory", "issueExtra", "lastIssueLog" })
	Page<Issue> findAllByMemberAndStatusIn(Member member, List<IssueStatus> statuses, @NotNull Pageable pageable);

	@EntityGraph(attributePaths = { "channel", "guest", "issueCategory", "issueExtra", "lastIssueLog" })
	Page<Issue> findAllByMemberAndStatusInAndType(Member member, List<IssueStatus> statuses, IssueType type , @NotNull Pageable pageable);

	@EntityGraph(attributePaths = { "channel", "guest", "issueCategory", "issueExtra", "lastIssueLog" })
	Page<Issue> findAllByMemberAndStatusInAndGuestIn(Member member, List<IssueStatus> statuses, List<Guest> guests, @NotNull Pageable pageable);

	@EntityGraph(attributePaths = { "channel", "guest", "issueCategory", "issueExtra", "lastIssueLog" })
	Page<Issue> findAllByMemberAndStatusInAndGuestInAndType(Member member, List<IssueStatus> statuses, List<Guest> guests, IssueType type, @NotNull Pageable pageable);

	Issue findOneByBranchIdAndChannelIdAndGuestIdAndStatusNot(@NotNull Long branchId, @NotNull Long channelId, @NotNull Long guestId, @NotNull IssueStatus status);

	Issue findOneByChannelIdAndGuestIdAndStatusNot(@NotNull Long channelId, @NotNull Long guestId, @NotNull IssueStatus status);

	List<Issue> findAllByChannelIdAndGuestIdAndStatusNotOrderByModifiedDesc(@NotNull Long channelId, @NotNull Long guestId, @NotNull IssueStatus status);

	Issue findFirstByChannelIdAndGuestIdAndStatusAndRelayedOrderByModifiedDesc(@NotNull Long channelId, @NotNull Long guestId, @NotNull IssueStatus status, @NotNull Boolean relayed);

	//bnk 고객 ID 기반으로 guest_id를 가지고 오기 위한 커스텀 쿼리
	List<Issue> findByGuestId(Long guestId);

	//기간계 알림 플래그값 조회
	List<Issue> findByCustomerId(Long customerId);

	/**
	 * 상담접수건
	 *
	 * @param start
	 * @param end
	 * @param status
	 * @return
	 */
//	List<Issue> findAllByCreatedBetweenAndStatusIn(ZonedDateTime start , ZonedDateTime end , List<IssueStatus> status);
	List<Issue> findAllByBranchIdAndCreatedBetweenAndStatusInAndMemberIn(Long branchId, ZonedDateTime start, ZonedDateTime end, List<IssueStatus> status, List<Member> members);

	List<Issue> findByStatusAndClosedGreaterThanEqualAndClosedLessThanAndMemberIdNotNull(IssueStatus status, ZonedDateTime startDateTime, ZonedDateTime endDateTime);

	List<Issue> findByCreatedGreaterThanEqualAndCreatedLessThan(ZonedDateTime startDateTime, ZonedDateTime endDateTime);

	List<Issue> findAllByGuestInOrderByCreatedDesc(List<Guest> entities);

	List<Issue> findAllByGuestInAndIssueExtraIsNotNullOrderByCreatedDesc(List<Guest> entities);

	List<Issue> findByCreatedGreaterThanEqualAndCreatedLessThanAndMemberIsNotNull(ZonedDateTime startDateTime, ZonedDateTime endDateTime);

	List<Issue> findByCreatedGreaterThanEqualAndCreatedLessThanAndMemberIsNotNullAndBranchId(ZonedDateTime startDateTime, ZonedDateTime endDateTime, Long branchId);

	Long countByMemberIdAndStatusInAndCreatedGreaterThanEqual(Long memberId, List<IssueStatus> statuses, ZonedDateTime created);

	List<Issue> findByMemberIdAndCreatedGreaterThanEqual(Long memberId, ZonedDateTime startDateTime);

	List<Issue> findAllByCreatedBetween(ZonedDateTime start , ZonedDateTime end);
}
