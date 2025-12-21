package com.board.study.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinRequest {
    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private String phone;
}
