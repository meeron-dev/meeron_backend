package com.cmc.meeron.auth.application.port.out;

import com.cmc.meeron.auth.domain.RefreshToken;

import java.util.Optional;

public interface TokenQueryPort {

    Optional<RefreshToken> findRefreshTokenByUsername(String username);

    boolean existsLogoutAccessTokenById(String id);

    boolean existsLogoutRefreshTokenById(String id);

    boolean existsRefreshTokenByUsername(String username);
}
