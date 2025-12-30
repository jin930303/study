package com.board.study.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //1.헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization");
        String token = null;

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            token = bearerToken.substring(7);
        }

        //2.토큰이 유효하면 SecurityContext에 인증 정보 저장
        if(token != null && jwtTokenProvider.validateToken(token)){
            String loginId = jwtTokenProvider.getLoginId(token);

            //유저 정보를 담은 인증 객체 생성 (비밀번호는 null로 처리)
            UsernamePasswordAuthenticationToken authentication=
                    new UsernamePasswordAuthenticationToken(loginId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);

    }
}
