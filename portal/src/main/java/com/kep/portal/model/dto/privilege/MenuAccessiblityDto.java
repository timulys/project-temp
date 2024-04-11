package com.kep.portal.model.dto.privilege;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MenuAccessiblityDto {

	private Long memberId;

	private Long uxObjectId;

	private boolean accessible;
}