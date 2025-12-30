package com.board.study.domain.member.controller;

import com.board.study.domain.member.dto.LoginRequest;
import com.board.study.domain.member.entity.RefreshToken;
import com.board.study.domain.member.repository.RefreshTokenRepository;
import com.board.study.domain.member.service.LoginService;
import com.board.study.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        boolean isSuccess = loginService.login(request);
        if(isSuccess){
            String accessToken = jwtTokenProvider.createAccessToken(request.getLoginId());
            String refreshToken = jwtTokenProvider.createRefreshToken(request.getLoginId());

            //Redis 저장
            refreshTokenRepository.save(new RefreshToken(request.getLoginId(),refreshToken));

            //토큰을 Map에 담아서 보냄
            Map<String,String> tokens = new HashMap<>();
            tokens.put("accessToken",accessToken);
            tokens.put("refreshToken",refreshToken);
            return ResponseEntity.ok(tokens);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String,String> request){
        String refreshToken = request.get("refreshToken");
        log.info("Refresh Token 수신 : {}",refreshToken);
        //Refresh Token 유효성 검증
        if(jwtTokenProvider.validateToken(refreshToken)){
            String loginId = jwtTokenProvider.getLoginId(refreshToken);

            //Redis에 저장된 토큰과 일치하는지 확인
            return refreshTokenRepository.findById(loginId)
                    .map(saveToken ->{
                        if (saveToken.getRefreshToken().equals(refreshToken)){
                            String newAccess = jwtTokenProvider.createAccessToken(loginId);
                            Map<String,String> map = new HashMap<>();
                            map.put("accessToken",newAccess);
                            return ResponseEntity.ok(map);
                        }
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 일치하지 않습니다.");
                    })
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("저장된 토큰이 없습니다."));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }

}
