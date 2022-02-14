package com.cmc.meeron.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.concurrent.TimeUnit;

@RedisHash("logoutRefreshToken")
@Getter
@AllArgsConstructor
@Builder
public class LogoutRefreshToken {

    @Id
    private String id;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

    public static LogoutRefreshToken of(String refreshToken, long expiration) {
        return LogoutRefreshToken.builder()
                .id(refreshToken)
                .expiration(expiration)
                .build();
    }
}
