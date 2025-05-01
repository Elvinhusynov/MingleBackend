package az.mingla.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey"; // min 256 bit

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000); // 1 gün

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)  // 'key' token-in imzalanması üçün istifadə olunur
                .build()
                .parseClaimsJws(token)  // Token-i analiz edir
                .getBody()  // Token-in body hissəsini alır
                .getSubject();  // 'subject' (yəni username) qaytarılır
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());   // Token-in bitiş vaxtı yoxlanır
    }

    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)  // 'key' token-in imzalanması üçün istifadə olunur
                .build()
                .parseClaimsJws(token)  // Token-i analiz edir
                .getBody();  // Token-in body hissəsini qaytarır
    }
}


