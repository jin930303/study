package com.board.study.domain.member.controller;

import com.board.study.domain.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/logout")
@RestController
public class LogoutController {

    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping
    public ResponseEntity<?> logout(@RequestBody Map<String,String> request){
        String loginId = request.get("loginId");

        if(loginId != null ){
            refreshTokenRepository.deleteById(loginId);
            log.info("사용자 {} 로그아웃 : Redis에서 리프레시 토큰 삭제 완료",loginId);
            return ResponseEntity.ok("로그아웃 성공");
        }
        return ResponseEntity.badRequest().body("잘못된 요청입니다.");
    }
}
