package com.cmc.meeron.auth.provider;

import com.cmc.meeron.auth.domain.AuthUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private final String SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRATION_MILLISECONDS;
    private final long REFRESH_TOKEN_EXPIRATION_MILLISECONDS;
    private final String BEARER = "Bearer ";
    private final String USER_ID = "userId";
    private final String USER_PROVIDER = "userProvider";

    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,
                       @Value("${jwt.access-expiration-time}") long accessTokenExpirationMilliseconds,
                       @Value("${jwt.refresh-expiration-time}") long refreshTokenExpirationMilliSeconds) {
        this.SECRET_KEY = secretKey;
        this.ACCESS_TOKEN_EXPIRATION_MILLISECONDS = accessTokenExpirationMilliseconds;
        this.REFRESH_TOKEN_EXPIRATION_MILLISECONDS = refreshTokenExpirationMilliSeconds;
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, ACCESS_TOKEN_EXPIRATION_MILLISECONDS);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, REFRESH_TOKEN_EXPIRATION_MILLISECONDS);
    }

    private String createToken(Authentication authentication, long time) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + time);
        return Jwts.builder()
                .setSubject(authUser.getUsername())
                .setClaims(createClaimsByAuthUser(authUser))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    private Map<String, Object> createClaimsByAuthUser(AuthUser authUser) {
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ID, authUser.getUserId());
        map.put(USER_PROVIDER, authUser.getUserProvider());
        return map;
    }

    public String getUserEmail(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public long getRemainingMilliSecondsFromToken(String token){
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - (new Date()).getTime();
    }

    private Claims getClaims(String token) {
        return parse(token)
                .getBody();
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public boolean isStartWithBearer(String bearerToken) {
        return bearerToken.startsWith(BEARER);
    }
}
