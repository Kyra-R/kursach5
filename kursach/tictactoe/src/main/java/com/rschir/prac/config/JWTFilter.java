package com.rschir.prac.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rschir.prac.security.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, ServletException {
        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (jwt.isBlank()) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
                return;
            } else {
                try {
                    SecurityContextHolder.getContext().setAuthentication(jwtUtil.validateTokenAndRetrieveAuth(jwt));
                } catch (JWTVerificationException exc) {
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                    return;
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        else {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Invalid JWT Token");
        }
    }
}