package com.tudo.chatify.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Dùng hằng số đã định nghĩa thay vì gõ tay
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if(jwt != null && jwt.startsWith("Bearer ")){
            try{
                // Cắt bỏ chữ "Bearer " (7 ký tự)
                jwt = jwt.substring(7);

                // Lấy khóa đã được tạo chuẩn từ JwtConstant
                SecretKey key = JwtConstant.SECRET_KEY;

                Claims claim = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = String.valueOf(claim.get("username"));
                String authorities = String.valueOf(claim.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch(Exception e) {
                throw new BadCredentialsException("Invalid JWT token");
            }
        }
        filterChain.doFilter(request, response);
    }
}