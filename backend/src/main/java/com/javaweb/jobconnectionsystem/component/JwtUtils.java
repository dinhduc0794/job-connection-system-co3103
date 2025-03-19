package com.javaweb.jobconnectionsystem.component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.jsonwebtoken.SignatureAlgorithm.*;
@Component
public class JwtUtils {
    private final SecretKey SECRET_KEY;

    public JwtUtils() {
        // Chuỗi khóa bí mật cố định của bạn
        String secretString = "iBg39h8TbQrXEoIp7ubmj+QyCfgSp9VLC9WBFwHkC9F7K23yopojy8AOXTYuNTB4";

        // Chuyển đổi chuỗi thành byte array
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);

        // Kiểm tra và điều chỉnh độ dài khóa nếu cần
        if (keyBytes.length != 32) {
            // Nếu khóa không đủ 32 bytes, bạn có thể cắt bớt hoặc thêm bù
            byte[] newKeyBytes = new byte[32];
            System.arraycopy(keyBytes, 0, newKeyBytes, 0, Math.min(keyBytes.length, 32));
            SECRET_KEY = Keys.hmacShaKeyFor(newKeyBytes);
        } else {
            SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
        }
    }
    public String generateToken(UserDetails userDetails) {
        try {
            String roles = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining(","));
            System.out.println("Generating token for user: " + userDetails.getUsername());

            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("role", roles)  // Lưu roles vào claim "role"
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 120)) // Token hết hạn sau 20 phút
                    .signWith(SECRET_KEY)
                    .compact();
        }
        catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Chắc chắn rằng key này là chính xác
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Phương thức trích xuất claim chung
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Lấy username (subject) từ token
    public String extractUsername(String token) {
        String name = (String) extractClaim(token, Claims::getSubject);
        System.out.println(name);
        return extractClaim(token, Claims::getSubject);  // Trả về subject (username)
    }

    // Lấy role từ token
    public String getRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));  // Trích xuất claim "role"
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        System.out.println(expiration);
        return expiration.before(new Date());
    }

    public boolean hasRole(String token, String requiredRole) {
        String role = getRole(token);  // Lấy role từ token
        System.out.println(role);
        return role != null && role.equals(requiredRole);
    }

    public boolean validateToken(String token, String requiredRole) {
        return !isTokenExpired(token) && hasRole(token, requiredRole);  // Token không hết hạn và có role đúng
    }
}
