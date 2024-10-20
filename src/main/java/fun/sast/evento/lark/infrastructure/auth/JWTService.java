package fun.sast.evento.lark.infrastructure.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Component
public class JWTService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final ObjectMapper objectMapper;

    public JWTService(@Value("${app.auth.jwt.secret}") String secret,
                      ObjectMapper objectMapper) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.objectMapper = objectMapper;
    }

    public record Payload<T>(T value) {
    }

    @SneakyThrows
    public String generate(Payload<?> payload, long expire) {
        return JWT.create().withPayload(objectMapper.writeValueAsString(payload))
                .withExpiresAt(Instant.now().plus(expire, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    @SneakyThrows
    public <T> T verify(String token, TypeReference<Payload<T>> typeReference) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
        } catch (TokenExpiredException e) {
            throw new BusinessException(ErrorEnum.TOKEN_EXPIRED);
        }
        String payload = new String(Base64.getDecoder().decode(decodedJWT.getPayload()));
        return objectMapper.readValue(payload, typeReference).value;
    }
}
