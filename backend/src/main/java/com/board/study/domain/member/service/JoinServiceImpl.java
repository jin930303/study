package com.board.study.domain.member.service;

import com.board.study.domain.member.dto.JoinRequest;
import com.board.study.domain.member.dto.JoinResponse;
import com.board.study.domain.member.entity.MemberEntity;
import com.board.study.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinServiceImpl implements JoinService {

    private final MemberRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public  JoinResponse join(JoinRequest request) {

        if(repository.existsByLoginId(request.getLoginId())) throw new RuntimeException("중복된 아이디");
        if(repository.existsByNickname(request.getNickname())) throw new RuntimeException("중복된 닉네임");

        MemberEntity member = MemberEntity.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
        MemberEntity saveMember = repository.save(member);


        return JoinResponse.builder()
                .memberId(saveMember.getId())
                .loginId(saveMember.getLoginId())
                .nickname(saveMember.getNickname())
                .createdAt(saveMember.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
