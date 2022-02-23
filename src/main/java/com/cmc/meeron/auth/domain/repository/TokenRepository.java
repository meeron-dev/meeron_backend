package com.cmc.meeron.auth.domain.repository;

import com.cmc.meeron.auth.domain.LogoutAccessToken;
import com.cmc.meeron.auth.domain.LogoutRefreshToken;
import com.cmc.meeron.auth.domain.RefreshToken;

import java.util.Optional;

public interface TokenRepository {

    void saveRefreshToken(RefreshToken refreshToken);

    void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken);

    void saveLogoutRefreshToken(LogoutRefreshToken logoutRefreshToken);

    Optional<RefreshToken> findRefreshTokenByUsername(String username);

    boolean existsLogoutAccessTokenById(String id);

    boolean existsLogoutRefreshTokenById(String id);

    boolean existsRefreshTokenByUsername(String username);
}
