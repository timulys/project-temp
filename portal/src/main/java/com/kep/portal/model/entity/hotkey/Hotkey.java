/**
 * 자주사용하는 문구 Entity 추가
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */

package com.kep.portal.model.entity.hotkey;

import com.kep.portal.model.dto.hotkey.HotkeyDto;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kep.core.model.dto.guide.GuideType;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.guide.Guide;
import com.kep.portal.model.entity.guide.GuideBlock;
import com.kep.portal.model.entity.guide.GuideCategory;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [2023.03.28 / asher.shin / 자주사용하는 문구 테이블]
 */
@Table(indexes = {
		@Index(name = "IDX_HOTKEY_MEMBER" , columnList = "memberId")
})
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Hotkey {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Comment("PK")
  private Long id;

  @Comment("단축키 첫번째")
  private String firstHotKey;

  @Comment("단축키 두번째")
  private String secondHotKey;

  @Comment("단축키 두번째의 키코드")
  private int hotkeyCode;

  @Comment("사용할 문구 내용")
  @Column(length=1000)
  private String content;

  @Comment("사용하는 직원 PK")
  private Long memberId;

  @Column(length = 1)
  @Comment("사용여부")
  @Convert(converter = BooleanConverter.class)
  private boolean enabled;

  @Comment("생성한 시간")
  private ZonedDateTime created;

  @Comment("수정된 시간")
  private ZonedDateTime modified;


  @Comment("정렬순서")
  private Long sort;

  public static Hotkey fromDto(HotkeyDto dto, Long memberId) {
    ZonedDateTime now = ZonedDateTime.now();
    return Hotkey.builder()
        .firstHotKey(dto.getFirstHotKey())
        .secondHotKey(dto.getSecondHotKey())
        .hotkeyCode(dto.getHotkeyCode())
        .content(dto.getContent())
        .memberId(memberId)
        .modified(now)
        .enabled(true)
        .sort(dto.getSort())
        .created(now)
        .build();
  }

}
