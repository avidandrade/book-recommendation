package com.bookstore.book_store.Auth0;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController { 
  
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
        
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Cookie set successfully");
    }

    //Creates cookie to overwrite the existing one holding token and sends to browswer
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        ResponseCookie cookie =  ResponseCookie.from("authToken","")
        .httpOnly(true)
        .secure(true)
        .sameSite("none")
        .path("/")
        .maxAge(0)
        .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Cleared Cookie!");
    }
}

