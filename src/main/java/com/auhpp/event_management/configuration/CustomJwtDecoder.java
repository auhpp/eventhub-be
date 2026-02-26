package com.auhpp.event_management.configuration;

import com.auhpp.event_management.constant.RedisPrefix;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${spring.jwt.signer-key}")
    private String signerKey;

    private final RedisTemplate<String, String> stringValueRedisTemplate;

    private NimbusJwtDecoder nimbusJwtDecoder;

    public CustomJwtDecoder(RedisTemplate<String, String> stringValueRedisTemplate) {
        this.stringValueRedisTemplate = stringValueRedisTemplate;
    }

    private void initDecoder() {
        if (nimbusJwtDecoder == null) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        initDecoder();
        //Check: signature and expiration time
        Jwt jwt = nimbusJwtDecoder.decode(token);

        //Check: Access token black list in redis
        String jti = jwt.getId();
        if (Objects.isNull(jti)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        boolean isBlacklisted = stringValueRedisTemplate.hasKey(RedisPrefix.TOKEN_BLACKLIST + jti);
        if (isBlacklisted) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return jwt;
    }
}
