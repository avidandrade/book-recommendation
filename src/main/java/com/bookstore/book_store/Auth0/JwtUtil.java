package com.bookstore.book_store.Auth0;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwt_secret; 
    public String extractUserId(String token) {
        // Create a signing key using the secret
        Key key = Keys.hmacShaKeyFor(jwt_secret.getBytes());

        // Use the new JwtParserBuilder API
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Set the signing key
                .build() // Build the parser
                .parseClaimsJws(token.replace("Bearer ", "")) // Parse the token
                .getBody();

        return claims.getSubject(); // The "sub" claim contains the Supabase UID
    }
}