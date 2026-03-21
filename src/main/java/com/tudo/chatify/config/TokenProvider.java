package com.tudo.chatify.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenProvider {
    SecretKey key = JwtConstant.SECRET_KEY;

    public String generateToken(Authentication authentication){
        String jwt = Jwts.builder()
                .setIssuer("Chatify")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 8640000)).claim("email", authentication.getName())
                .signWith(key).compact();
        return jwt;
    }
    public String getEmailFromToken(String jwt){
        jwt = jwt.substring(7); // seperate token from Bearer token
        Claims claim = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claim.get("email"));
        return email;

    }

}
