package com.tudo.chatify.config;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class JwtConstant {
    public static final String JWT_HEADER="Authorization";
    // Tách chuỗi ra một hằng số riêng
    private static final String SECRET_STRING = "daylamotchuoisieubaomatvadaitren32kytu";

    // Dùng Keys.hmacShaKeyFor để generate SecretKey từ chuỗi trên
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
}
