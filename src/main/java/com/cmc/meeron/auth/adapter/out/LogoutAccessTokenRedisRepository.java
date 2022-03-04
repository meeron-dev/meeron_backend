package com.cmc.meeron.auth.adapter.out;

import com.cmc.meeron.auth.domain.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
