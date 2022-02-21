package com.cmc.meeron.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.concurrent.TimeUnit;

@RedisHash("refreshToken")
@Getter
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    private String id;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

    public static RefreshToken of(String username, String refreshToken, long expiration) {
        return RefreshToken.builder()
                .id(username)
                .refreshToken(refreshToken)
                .expiration(expiration)
                .build();
    }
}
