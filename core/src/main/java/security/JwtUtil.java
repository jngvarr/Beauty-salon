package security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.jngvarr.authservice.services.UserDetailsServiceImpl;
//import ru.jngvarr.authservice.services.UserDetailsServiceImpl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor

public class JwtUtil {
//    private final UserDetailsServiceImpl userDetailsService;
//    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60 * 10; // 10 часов
//    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7 дней

    //    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Генерация ключа для HMAC-SHA256

//    public String generateToken(UserDetails userDetails, boolean tT) {
//        Map<String, Object> claims = new HashMap<>();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(ga -> ga.getAuthority().substring(5))
//                .toList();
//        claims.put("user_id", getUserId(userDetails));
//        claims.put("roles", roles);
//        return createToken(claims, userDetails.getUsername(), tT ? ACCESS_TOKEN_VALIDITY : REFRESH_TOKEN_VALIDITY);
//    }
//
//    private Long getUserId(UserDetails userDetails) {
//        return userDetailsService.loadUserByUsername(userDetails.getUsername()).getId();
//    }
//
//
//    private String createToken(Map<String, Object> claims, String subject, long expirationTimeMillis) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
//                .signWith(SignatureAlgorithm.HS256, jwtSecret)
//                .compact();
//    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        userDetailsService.loadUserByUsername(jwtUtil.extractUsername(jwtString))
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
