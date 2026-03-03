package com.auth_server.jwt;

import com.auth_server.entity.Role;
import com.auth_server.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access-token-expiration}")
    private long expirationMs;

    private final JwtKeyProvider keys;

    public JwtTokenProvider(JwtKeyProvider keys) {
        this.keys = keys;
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles()
                        .stream().map(Role::getName).toList())
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(keys.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }
}