package com.recruiter.auth_service.service;

import com.recruiter.auth_service.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {


    private static final String SECRET_KEY = "dd307dd1f7e60eb498704935c0c6438a18700e0bcb1aea06cbd50f6e43d94593";

    private final UserDetailsServiceImpl userService;

    public JwtService(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }


    public  String extractUsername(String token){
        return extractClaim(token , Claims::getSubject);
    }

    public String extractRole(String token) {
        String role= extractAllClaims(token).get("roles").toString();
        return role;
    }


    public boolean isValid(String token, UserDetails user){
        String username= extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims= extractAllClaims((token));
        return  resolver.apply(claims);
    }


    public Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String generateToken(User user){
        String token = Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 24*60*60*1000))
                .claim("roles", user.getRole().toString())
                .claim("userId", user.getId().toString())
                .signWith(getSigninKey())
                .compact();
        System.out.println(token);
        return  token;
    }


    public SecretKey getSigninKey(){
        byte[] KeyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(KeyBytes);
    }
}
