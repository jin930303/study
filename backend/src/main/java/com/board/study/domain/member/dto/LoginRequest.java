package com.board.study.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    private String loginId;
    private String password;
}
