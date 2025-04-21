package com.openclassroom.Rental.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;

@Service
public class JwtUtil {

    byte[] secretBytes = com.openclassroom.Rental.Service.SecretManagerService.getSecret();
    Key SECRET_KEY = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, @NotNull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
       return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
      boolean  isValid;
        try {
            extractAllClaims(token);
            isValid = true;
        } catch (Exception e) {
            isValid =  false;
        }
        if (isTokenExpired(token)) {
            isValid = false;
        }

        return isValid;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
      return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {


       return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 heures
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserEmail(String token) {
        return  extractClaim(token, Claims::getSubject);
    }
}
