package com.javaweb.jobconnectionsystem.component;

import com.javaweb.jobconnectionsystem.service.auth.impl.UserDetailServiceImpl;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtil;
    @Autowired
    private  HandlerExceptionResolver handlerExceptionResolver;
    @Autowired
    private UserDetailServiceImpl userDertailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return; // Bỏ qua việc xử lý JWT và tiếp tục với filter chain
            }
            String authHeader = request.getHeader("Authorization");
            String jwtToken = null;
            String username = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7);
            }
            username = jwtUtil.extractUsername(jwtToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String role = userDertailService.loadRoleByUsername(username);
                UserDetails userDetails=userDertailService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwtToken,role)){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/public/jobpostings", "GET"),
                Pair.of("/public/companies", "GET"),
                Pair.of("/login", "POST"),
                Pair.of("/register/applicant","POST"),
                Pair.of("/register/company","POST"),// Bỏ qua xác thực cho POST /public/login
                Pair.of("/jobtypes","GET")
        );
        String uri = request.getRequestURI();
        if (uri.startsWith("/public")) {
            return true; // Bỏ qua lọc đối với tất cả các đường dẫn bắt đầu bằng /public/
        }
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true; // Bỏ qua xác thực cho đường dẫn này
            }
        }
        return false; // Không bỏ qua xác thực
    }
}
