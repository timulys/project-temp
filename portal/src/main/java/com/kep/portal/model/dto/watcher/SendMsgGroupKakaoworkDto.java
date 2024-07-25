package com.kep.portal.model.dto.watcher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMsgGroupKakaoworkDto {

   private String from; // 메시지를 보내는 봇 이름. default : watchtower.bot
   private int to; // 와치센터 그룹 ID
   private String msg; // 전송 메시지
   private boolean ps; // 값이 true 이면 그룹멤버 개개인에게 메시지를 전송합니다. default : false
}
