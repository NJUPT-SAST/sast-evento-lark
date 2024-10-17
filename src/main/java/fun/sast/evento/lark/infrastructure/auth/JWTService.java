package fun.sast.evento.lark.infrastructure.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JWTService {
    private final ObjectMapper objectMapper;
    private final Algorithm algorithm;
    private final Long expire;
    private final JWTVerifier verifier;

    public JWTService(@Value("${app.auth.jwt.secret}") String secret, @Value("${app.auth.jwt.expire}") Long expire, ObjectMapper objectMapper) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.objectMapper = objectMapper;
        this.expire = expire;
    }

    public record Payload<T>(T value) {
    }

    @SneakyThrows
    public String generate(Payload<?> payload) {
        return JWT.create().withPayload(objectMapper.writeValueAsString(payload))
                .withExpiresAt(Instant.now().plus(expire, ChronoUnit.MILLIS))
                .sign(algorithm);
    }

    @SneakyThrows
    public <T> T verify(String token, TypeReference<Payload<T>> typeReference) throws JWTVerificationException {
        final DecodedJWT decodedJWT = verifier.verify(token);
        return objectMapper.readValue(decodedJWT.getPayload(), typeReference).value;
    }

}
