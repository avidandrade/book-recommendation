package com.bookstore.book_store.Auth0;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController { 
    
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }
    //Creates cookie and sends to browswer
    @PostMapping("/auth/set-cookie")
    public ResponseEntity<?> setCookie(@RequestBody Map<String,String> body, HttpServletResponse response){
        String token = body.get("token");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is missing");
        }

        ResponseCookie cookie = ResponseCookie.from("authToken", token)
        .httpOnly(true)
        .secure(true)
        .sameSite("none")
        .path("/")
        .maxAge(3600)
        .build();
        
        if(cookie == null){
            System.out.println("Error creating cookie");
        }
        
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
        .path("/")
        .maxAge(0)
        .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Cleared Cookie!");
    }
    
    @GetMapping("/auth/verify")
    public ResponseEntity<?> verifyAuth(HttpServletRequest request){
        String authToken = null;

        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if("authToken".equals(cookie.getName())){
                    authToken = cookie.getValue();
                }
            }
        }

        if (authToken != null) {
            try {
                Claims claims = jwtUtil.validateToken(authToken);
                String userId = claims.getSubject();
                return ResponseEntity.ok(Map.of("authenticated", true, "userId", userId));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
    }
}

