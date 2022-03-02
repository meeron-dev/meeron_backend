package com.cmc.meeron.auth.domain;

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
