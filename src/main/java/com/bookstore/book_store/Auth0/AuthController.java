package com.bookstore.book_store.Auth0;

import java.security.Key;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes()); // Convert secret to a Key object
    }
    
    @PostMapping("/auth/set-cookie")
    public ResponseEntity<?> setCookie(@RequestBody Map<String,String> body, HttpServletResponse response){
        String token = body.get("token");

        ResponseCookie cookie = ResponseCookie.from("authToken", token)
        .httpOnly(true)
        .secure(!"development".equals(System.getenv("ENV")))
        .sameSite("Strict")
        .path("/")
        .maxAge(3600)
        .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Cookie set successfully");
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        ResponseCookie cookie =  ResponseCookie.from("authToken","")
        .httpOnly(true)
        .secure(!"development".equals(System.getenv("ENV")))
        .sameSite("Strict")
        .path("/")
        .maxAge(0)
        .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Cleared Cookie!");
    }
    
    @GetMapping("/auth/validate")
    public ResponseEntity<?> validateAuth(@CookieValue(value = "authToken", required = false) String token) {
        if (token == null || token.isEmpty()) {
            logger.warn("No authToken cookie found");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) 
                .build()
                .parseClaimsJws(token)
                .getBody();
            logger.info("Token is valid for user: {}", claims.getSubject());
            return ResponseEntity.ok().body("Token is valid");
        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage());
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}
