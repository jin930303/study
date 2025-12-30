package com.board.study.domain.member.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken",timeToLive = 604800)//7일
public class RefreshToken {

    @Id
    private String loginId;
    private String refreshToken;

    public RefreshToken(String loginId, String refreshToken){
        this.loginId=loginId;
        this.refreshToken=refreshToken;
    }

    public void updateToken(String newToken){
        this.refreshToken=newToken;
    }
}
