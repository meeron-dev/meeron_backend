package com.cmc.meeron.auth.infrastructure;

import com.cmc.meeron.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
