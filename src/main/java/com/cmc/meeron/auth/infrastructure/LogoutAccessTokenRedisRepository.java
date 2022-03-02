package com.cmc.meeron.auth.infrastructure;

import com.cmc.meeron.auth.domain.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
