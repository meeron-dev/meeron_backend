package com.cmc.meeron.auth.domain.repository;

import com.cmc.meeron.auth.domain.LogoutRefreshToken;
import org.springframework.data.repository.CrudRepository;

interface LogoutRefreshTokenRedisRepository extends CrudRepository<LogoutRefreshToken, String> {
}
