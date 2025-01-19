//package com.self.PureSkyn.security;
//
//import com.self.PureSkyn.Model.Role;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//public class JwtUtils {
//
//    @Value("${security.jwt.secret-key}")
//    private String secretKey;
//
//    @Value("${security.jwt.expiration-time}")
//    private long jwtExpiration;
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
////    public String generateToken(String email, String password) {
////        UserDetails userDetails = new User(email, password, List.of(new SimpleGrantedAuthority(Role.USER.toString())));
////        return generateToken(new HashMap<>(), userDetails);
////    }
//
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return buildToken(extraClaims, userDetails.getUsername());
//    }
//    public String generateToken(UserDetails userDetails) {
//        return generateToken(new HashMap<>(), userDetails);
//    }
//    public String generateToken(String email) {
//        Map<String, Object> extraClaims = new HashMap<>();
//        return buildToken(extraClaims, email);
//    }
//
//    private String buildToken(
//            Map<String, Object> extraClaims,
//            UserDetails userDetails
//    ) {
//
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date())
//                .expiration(new Date((new Date()).getTime() + jwtExpiration))
//                .signWith(getSignInKey())
//                .compact();
//    }
//
//    private String buildToken(
//            Map<String, Object> extraClaims,
//            String email
//    ) {
//
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .subject(email)
//                .issuedAt(new Date())
//                .expiration(new Date((new Date()).getTime() + jwtExpiration))
//                .signWith(getSignInKey())
//                .compact();
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }
//    public boolean validateToken(String token, String email) {
//        String extractedEmail = extractUsername(token);
//        return extractedEmail.equals(email) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parser()
//                .verifyWith(getSignInKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    private SecretKey getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return new SecretKeySpec(keyBytes, "HmacSHA256");
//    }
//
//
////
////    public boolean validateJwtToken(String authToken) {
////        try {
////            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
////            return true;
////        } catch (SecurityException | MalformedJwtException | ExpiredJwtException
////                 | UnsupportedJwtException | IllegalArgumentException e) {
////            System.out.println("Invalid JWT: " + e.getMessage());
////        }
////        return false;
////    }
//}

package com.self.PureSkyn.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token, String email) {
        try {
            String extractedEmail = extractUsername(token);
            return extractedEmail.equals(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

}
