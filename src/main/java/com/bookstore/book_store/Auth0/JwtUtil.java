package com.bookstore.book_store.Auth0;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;

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
        }catch(Exception e){
            throw new RuntimeException("Invalid or Expired JWT Token");
        }
    }
}