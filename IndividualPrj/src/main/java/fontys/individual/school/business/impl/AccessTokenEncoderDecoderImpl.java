package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.AccessTokenDecoder;
import fontys.individual.school.business.Interface.AccessTokenEncoder;
import fontys.individual.school.business.exception.InvalidAccessTokenException;
import fontys.individual.school.domain.AccessToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class AccessTokenEncoderDecoderImpl implements AccessTokenDecoder, AccessTokenEncoder {
    private final Key key;

    public AccessTokenEncoderDecoderImpl(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String encode(AccessToken accessToken) {
        Map<String, Object> claimsMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(accessToken.getRoles())) {
            claimsMap.put("roles", accessToken.getRoles());
        }
        if (accessToken.getAccountId() != null) {
            claimsMap.put("accountId", accessToken.getAccountId());
        }

        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(accessToken.getSubject())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(2, ChronoUnit.HOURS)))
                .addClaims(claimsMap)
                .signWith(key)
                .compact();
    }

    @Override
    public AccessToken decode(String accessTokenEncoded) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(accessTokenEncoded)
                    .getBody();

            List<String> roles = claims.get("roles", List.class);

            return AccessToken.builder()
                    .subject(claims.getSubject())
                    .roles(roles)
                    .accountId(claims.get("accountId", Long.class))
                    .build();
        } catch (JwtException e) {
            throw new InvalidAccessTokenException(e.getMessage());
        }
    }
}
