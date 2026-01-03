package com.board.study.domain.member.controller;

import com.board.study.domain.member.dto.JoinRequest;
import com.board.study.domain.member.dto.JoinResponse;
import com.board.study.domain.member.repository.MemberRepository;
import com.board.study.domain.member.service.JoinService;
import com.board.study.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final JoinService joinService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository repository;

    @PostMapping("/join")
    public ResponseEntity<JoinResponse> join(@RequestBody JoinRequest request){
        return ResponseEntity.ok(joinService.join(request));
    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkId(@RequestParam("loginId") String loginId) {
        return ResponseEntity.ok(joinService.existsByLoginId(loginId));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(joinService.existsByNickname(nickname));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(joinService.existsByEmail(email));
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhone(@RequestParam("phone") String phone) {
        return ResponseEntity.ok(joinService.existsByPhone(phone));
    }

    //내정보
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request){
        //요청헤더에서 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        //토큰이 유효한지 검증
        if(token !=null && jwtTokenProvider.validateToken(token)){
            String loginId = jwtTokenProvider.getLoginId(token);

            return repository.findByLoginId(loginId)
                    .map(member -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("loginId",member.getLoginId());
                        response.put("nickname",member.getNickname());
                        response.put("email",member.getEmail());
                        response.put("createAt",member.getCreatedAt());
                        response.put("phone",member.getPhone());
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    @PutMapping("/me")
    public ResponseEntity<?> updateMyInfo(HttpServletRequest request, @RequestBody Map<String,String> updateData) {
        String token = jwtTokenProvider.resolveToken(request);

        String loginId = null;
        if (token != null && jwtTokenProvider.validateToken(token)) {
            loginId = jwtTokenProvider.getLoginId(token);

        return repository.findByLoginId(loginId)
                .map(member -> {
                    member.setNickname(updateData.get("nickname"));
                    member.setEmail(updateData.get("email"));
                    member.setPhone(updateData.get("phone"));
                    repository.save(member);
                    return ResponseEntity.ok("정보가 수정되었습니다.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다."));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
    }

}
