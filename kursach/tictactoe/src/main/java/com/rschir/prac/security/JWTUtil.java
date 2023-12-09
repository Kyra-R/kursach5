package com.rschir.prac.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    public UsernamePasswordAuthenticationToken validateTokenAndRetrieveAuth(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("marketplace")
                .withClaimPresence("login")
                .withClaimPresence("role")
                .withClaimPresence("playerId")
                .build();

        DecodedJWT jwt = verifier.verify(token);

        Set<SimpleGrantedAuthority> grantedValues = new HashSet<>();
        grantedValues.add(new SimpleGrantedAuthority(jwt.getClaim("role").asString()));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("", "", grantedValues);
        auth.setDetails(jwt.getClaim("playerId").asLong());
        return auth;
    }
}