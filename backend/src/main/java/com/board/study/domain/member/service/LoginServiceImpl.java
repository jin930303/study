package com.board.study.domain.member.service;

import com.board.study.domain.member.dto.LoginRequest;
import com.board.study.domain.member.entity.MemberEntity;
import com.board.study.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberRepository repository;
    private final PasswordEncoder encoder;
    @Override
    public boolean login(LoginRequest request) {
       Optional<MemberEntity> memberId= repository.findByLoginId(request.getLoginId());
       if(memberId.isPresent()){
           MemberEntity member = memberId.get();
           return encoder.matches(request.getPassword(),member.getPassword());
       }
        return false;
    }
}
