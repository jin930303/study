package com.board.study.domain.member.service;

import com.board.study.domain.member.dto.LoginRequest;
import com.board.study.domain.member.entity.MemberEntity;
import org.jspecify.annotations.Nullable;

public interface LoginService {
    boolean login(LoginRequest request);
}
