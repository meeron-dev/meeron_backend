package com.cmc.meeron.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.concurrent.TimeUnit;

@RedisHash("logoutAccessToken")
@Getter
@AllArgsConstructor
@Builder
public class LogoutAccessToken {

    @Id
    private String id;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

    public static LogoutAccessToken of(String accessToken, long expiration) {
        return LogoutAccessToken.builder()
                .id(accessToken)
                .expiration(expiration)
                .build();
    }
}
