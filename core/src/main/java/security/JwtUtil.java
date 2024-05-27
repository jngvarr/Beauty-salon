package security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.*;


@RequiredArgsConstructor

public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60 * 10; // 10 часов
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7 дней

    public String generateToken(UserDetails userDetails, boolean tT) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(ga -> ga.getAuthority().substring(5))
                .toList();
        claims.put("roles", roles);
        return createToken(claims, userDetails.getUsername(), tT ? ACCESS_TOKEN_VALIDITY : REFRESH_TOKEN_VALIDITY);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTimeMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    public Date extractExpiration(Claims claims) {
        return claims.getExpiration();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(Claims claims) {
        return extractExpiration(claims).before(new Date());
    }

    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public List<String> extractRoles(Claims claims) {
        ArrayList<String> roles = claims.get("roles", ArrayList.class);
        List<String> authorities = new ArrayList<>();
        for (String role : roles) {
            String authority = "ROLE_" + role;
            authorities.add(authority);
        }
        return authorities;
    }

    public boolean validateToken(Claims claims, UserDetails userDetails) {
        final String username = extractUsername(claims);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(claims));
    }
}

