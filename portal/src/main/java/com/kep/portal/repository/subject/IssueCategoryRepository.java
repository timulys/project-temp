package com.kep.portal.repository.subject;

import com.kep.portal.model.entity.subject.IssueCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Repository
public interface IssueCategoryRepository extends JpaRepository<IssueCategory, Long> {

	@Query(value = "select ic from IssueCategory ic"
			+ " where"
//			+ " (ic.channelId = :channelId or ic.exposed = :exposed)"
			+ " ic.channelId = :channelId"
			+ " and ic.enabled = :enabled"
			+ " and ic.exposed = :exposed"
//			+ " and ic.depth = :depth"
			+ " and ic.name like %:name%"
			+ " order by ic.sort"
	)
	List<IssueCategory> search(@Param(value = "channelId") @NotNull Long channelId,
							   @Param(value = "exposed") @NotNull Boolean exposed,
							   @Param(value = "enabled") @NotNull Boolean enabled,
//							   @Param(value = "depth") @NotNull Integer depth,
							   @Param(value = "name") @NotEmpty String name);

	@Query(value = "select ic from IssueCategory ic"
			+ " where 1=1"
			+ " and ic.depth = :depth"
			+ " and ic.id = :id"
	)
	List<IssueCategory> searchById(@Param(value = "depth") @NotNull Integer depth,
							   @Param(value = "id") @NotEmpty Long id);

	@Query(value = "select ic from IssueCategory ic"
			+ " where"
//			+ " (ic.channelId = :channelId or ic.exposed = :exposed)"
			+ " ic.channelId = :channelId"
			+ " and ic.enabled = :enabled"
			+ " and ic.exposed = :exposed"
			+ " and ic.depth = :depth"
			+ " order by ic.sort"
	)
	List<IssueCategory> search(@Param(value = "channelId") @NotNull Long channelId,
							   @Param(value = "exposed") @NotNull Boolean exposed,
							   @Param(value = "enabled") @NotNull Boolean enabled,
							   @Param(value = "depth") @NotNull Integer depth);

	@Query(value = "select ic from IssueCategory ic"
			+ " where"
			+ " ic.channelId in (:channelIds)"
			+ " and (:parent is null or ic.parent = :parent)"
			+ " and (:depth is null or ic.depth = :depth)"
			+ " and ic.enabled = true"
			+ " order by ic.channelId, ic.sort"
	)
	List<IssueCategory> search(@Param(value = "channelIds") @NotNull List<Long> channelIds,
							   @Param(value = "parent") @Positive IssueCategory parent,
							   @Param(value = "depth") @NotNull Integer depth);

	List<IssueCategory> findAllByParentAndEnabledIsTrue(IssueCategory category);

	List<IssueCategory> findByParentIdIn(List<Long> parentIds);

	@Query("select ic, ic2 from IssueCategory ic left join fetch IssueCategory ic2 on ic.parent = ic2 where ic.channelId = :channelId")
	List<IssueCategory> findAllByChannelIdWithParent(Long channelId);

	@Query("select ic, ic2 from IssueCategory ic left join fetch IssueCategory ic2 on ic.parent = ic2 where ic.enabled = true and (ic.channelId = :channelId or ic.exposed = true)")
	List<IssueCategory> findAllByChannelIdAndEnabledIsTrueAndExposedIsTrueWithParent(@Param("channelId") Long channelId);

	IssueCategory findTopByChannelIdOrderByDepthDescParentIdAscIdAsc(Long channelId);

	/**
	 * 사용 가능 요건 카테고리 전체 조회 (전체 오픈 + 채널 오픈 + 사용중)
	 * @return
	 */
	@Query("select ic from IssueCategory ic where ic.depth = :depth and ic.enabled = true and (ic.channelId = :channelId or ic.exposed = true) order by ic.sort")
	List<IssueCategory> findAllByDepthOnlyUse(Long channelId, Integer depth);

	//관리용
	List<IssueCategory> findAllByChannelIdAndDepthAndEnabledIsTrueOrderBySort(Long channelId, Integer depth);
	List<IssueCategory> findAllByChannelIdAndEnabledIsTrueAndNameLike(Long channelId, String name);
}
