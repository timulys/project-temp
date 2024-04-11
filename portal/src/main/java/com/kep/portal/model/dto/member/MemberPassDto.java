/**
  * MemberPassDto
  * 
  *  @생성일자     / 만든사람			/ 수정내용
  *  2023.04.04 / philip.lee7	/ 신규
  */

package com.kep.portal.model.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPassDto {
    private Long id;
    private String password;
    private String newPassword;

    private String confirmNewPassword;
}
