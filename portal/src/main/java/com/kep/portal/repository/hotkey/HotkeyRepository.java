/**
 * Hotkey Repository 추가
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */
package com.kep.portal.repository.hotkey;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.entity.hotkey.Hotkey;

// [2023.03.28 / asher.shin / 자주사용하는 문구 Repository]
@Repository
public interface HotkeyRepository extends JpaRepository<Hotkey,Long>{

    //직원기준 자주사용하는 문구 가져오기
    public List<Hotkey> findByMemberIdOrderBySortAsc(Long id);

    Optional<Hotkey> findByIdAndMemberId(Long id, Long memberId);

    void deleteByMemberId(Long memberId);
}
