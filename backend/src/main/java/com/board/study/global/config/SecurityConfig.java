package com.board.study.global.config;

import com.board.study.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        http
                // 1. CSRF 비활성화 (REST API이므로 불필요)
                .csrf(AbstractHttpConfigurer :: disable)
                // 2. CORS 설정 연결
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 세션 사용 안함
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 3. 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/member/me").authenticated()
                        .requestMatchers("/member/join","/member/check-*").permitAll()
                        .requestMatchers("/member/refresh").permitAll()
                        .requestMatchers("/login/**","/logout/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer :: disable)
                .httpBasic(AbstractHttpConfigurer :: disable)
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // 5. CORS 세부 설정 (Vue.js와의 통신을 위함)
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173")); // Vue 기본 포트
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
