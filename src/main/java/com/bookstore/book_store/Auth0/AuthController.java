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

    @Value("${jwt.secret}")
    private String secret;
    
    //Creates cookie and sends to browswer
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

    //Creates cookie to overwrite the existing one holding token and sends to browswer
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        ResponseCookie cookie =  ResponseCookie.from("authToken","")
        .httpOnly(true)
        .secure(!"development".equals(System.getenv("ENV")))
        .sameSite("Strict")
        .maxAge(0)
        .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Cleared Cookie!");
    }
    
}
