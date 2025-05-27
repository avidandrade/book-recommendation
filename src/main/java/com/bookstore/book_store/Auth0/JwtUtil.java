package com.bookstore.book_store.Auth0;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwt_secret; 

    public Claims validateToken(String token){
        try{
            Key key = Keys.hmacShaKeyFor(jwt_secret.getBytes());

            return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT Token is expired", e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("JWT Token is unsupported", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }
}