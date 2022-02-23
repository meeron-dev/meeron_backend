package com.cmc.meeron.auth.domain.repository;

import com.cmc.meeron.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
