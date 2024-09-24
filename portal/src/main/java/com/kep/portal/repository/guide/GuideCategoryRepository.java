package com.kep.portal.repository.guide;

import com.kep.portal.model.entity.guide.GuideCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuideCategoryRepository extends JpaRepository<GuideCategory, Long> {

    @Query("select c from GuideCategory c where c.id = :categoryId and (c.branch.id = :branchId or c.isOpen = true)")
    List<GuideCategory> findByIdAndBranchIdOrIsOpenTrue(@Param("categoryId") Long categoryId, @Param("branchId") Long branchId);

    @Query("select c from GuideCategory c where c.depth = :depth and (c.branch.id = :branchId or c.isOpen = true)")
    List<GuideCategory> findByBranchAndDepthCategory(@Param("branchId") Long branchId, @Param("depth") Integer depth);

    @Query("select c from GuideCategory c where c.depth = :depth")
    List<GuideCategory> findAllByDepthCategory(@Param("depth") Integer depth);

    @Query("select c from GuideCategory c where c.id = :categoryId")
    List<GuideCategory> findAllById(@Param("categoryId") Long categoryId);

    Optional<GuideCategory> findByIdAndBranchId(Long id, Long branchId);

    //FIXME :: this.findByBranchAndDepthCategory()과 동일 쿼리 -> 삭제 필요 volka
    @Deprecated
    @Query("select c from GuideCategory c where c.depth = :depth and (c.branch.id = :branchId or c.isOpen = true)")
    List<GuideCategory> findByMyBranchDepthCategory(@Param("branchId") Long branchId, @Param("depth") Integer depth);


    List<GuideCategory> findByBranchIdAndDepthAndIsOpenTrue(Long branchId, Integer depth);

    List<GuideCategory> findByBranchIdAndDepth(Long branchId, Integer depth);
}
