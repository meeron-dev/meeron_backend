package com.cmc.meeron.auth.domain.repository;

import com.cmc.meeron.auth.domain.LogoutRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutRefreshTokenRepository extends CrudRepository<LogoutRefreshToken, String> {
}
