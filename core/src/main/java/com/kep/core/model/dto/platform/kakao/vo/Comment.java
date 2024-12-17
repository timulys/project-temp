package com.kep.core.model.dto.platform.kakao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    /* 댓글아이디 */
    private Integer id;
    /* 댓글 내용 */
    private String content;
    /* 작성자 */
    private String userName;
    /* 작성일자 */
    private String createAt;
    /* 댓글 상태
     * INQ: 문의
     * APR: 승인
     * REJ: 반려
     * REP: 답변
     * REQ: 검수요청
     **/
    private String status;
//    /* 첨부파일 */
//    private List<String> attachment; // FIXME : 현재 미구현
}
