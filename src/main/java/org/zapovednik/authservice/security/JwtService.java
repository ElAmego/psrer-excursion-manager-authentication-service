package org.zapovednik.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.zapovednik.authservice.model.entity.type.UserRole;

@Service
public class JwtService {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    public JwtService(
            @Value("${jwt.key-path.private-key}") final Resource privateKeyPath,
            @Value("${jwt.key-path.public-key}") final Resource publicKeyPath
    ) throws Exception {
        this.privateKey = loadPrivateKey(privateKeyPath);
        this.publicKey = loadPublicKey(publicKeyPath);
    }

    public String generateAccessToken(final Long userId, final String login, final UserRole userRole) {
        return Jwts.builder()
                .subject(login)
                .claim("userId", userId)
                .claim("userRole", userRole.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(privateKey)
                .compact();
    }

    public String extractLogin(final String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        final String login = extractLogin(token);
        return login.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private RSAPrivateKey loadPrivateKey(final Resource resource) throws Exception {
        final String key = new String(resource.getInputStream().readAllBytes())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        final byte[] keyBytes = Base64.getDecoder().decode(key);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    private RSAPublicKey loadPublicKey(final Resource resource) throws Exception {
        final String key = new String(resource.getInputStream().readAllBytes())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        final byte[] keyBytes = Base64.getDecoder().decode(key);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }
}