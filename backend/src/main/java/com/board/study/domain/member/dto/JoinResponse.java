package com.board.study.domain.member.dto;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class JoinResponse {
    private Long memberId;
    private String loginId;
    private String nickname;
    private String createdAt;
}
