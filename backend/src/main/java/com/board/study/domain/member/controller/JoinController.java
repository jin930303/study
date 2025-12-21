package com.board.study.domain.member.controller;

import com.board.study.domain.member.dto.JoinRequest;
import com.board.study.domain.member.dto.JoinResponse;
import com.board.study.domain.member.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class JoinController {
    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<JoinResponse> join(@RequestBody JoinRequest request){
        return ResponseEntity.ok(joinService.join(request));
    }

}
