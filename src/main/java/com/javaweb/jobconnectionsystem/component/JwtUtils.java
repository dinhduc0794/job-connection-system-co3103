package com.javaweb.jobconnectionsystem.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public String generateToken(UserDetails userDetails) {
        try {
            String roles = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining(","));
            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("role", roles)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // Token hết hạn sau 10 phút
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        }
        catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }
    public boolean hasRole(String token, String requiredRole) {
        // Trích xuất và tách các vai trò từ token
        Claims claims = extractAllClaims(token);
        String roles = claims.get("role", String.class);
        List<String> rolesList = List.of(roles.split(","));
        return rolesList.contains(requiredRole);
    }
    public String role (String token){
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token, String requiredRole) {
        return !isTokenExpired(token) && hasRole(token, requiredRole);  //con thoi han su dung va dung role
    }


}
