package com.board.study.domain.member.service;

import com.board.study.domain.member.dto.JoinRequest;
import com.board.study.domain.member.dto.JoinResponse;

public interface JoinService {
     JoinResponse join(JoinRequest request);

     boolean existsByLoginId(String loginId);

     boolean existsByNickname(String nickname);

     boolean existsByEmail(String email);

     boolean existsByPhone(String phone);
}
