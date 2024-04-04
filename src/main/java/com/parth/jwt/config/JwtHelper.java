package com.parth.jwt.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.parth.jwt.model.UserEntity;
import com.parth.jwt.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.function.Function;



@Component
public class JwtHelper {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        UserEntity role = userRepository.findUserByUsername(userDetails.getUsername());
        Object[] roles = role.getRole().toString().split(",");
        claims.put("Roles",roles);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        byte[] keyBytes = secret.getBytes();
        SecretKey key = new SecretKeySpec(keyBytes,0, keyBytes.length, "HmacSHA512");

        String jws = Jwts.builder()
                .setIssuer("Admin")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key, SignatureAlgorithm.HS512).compact();

        return jws;
//        Nimbus implementation
//        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512).build();
//
//        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//                .issuer("Admin")
//                .claim("Roles", claims.get("Roles"))
//                .subject(subject)
//                .issueTime(new Date(System.currentTimeMillis()))
//                .expirationTime(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//                .build();
//
//        Payload payload = new Payload(claimsSet.toJSONObject());
//
//        byte[] keyBytes = secret.getBytes();
//        SecretKey key = new SecretKeySpec(keyBytes,0, keyBytes.length, "HmacSHA512");
//
//        JWSObject jwsObject = new JWSObject(header, payload);
//
//        try {
//            jwsObject.sign(new MACSigner( key));
//        } catch (JOSEException e) {
//            throw new RuntimeException(e);
//        }
//        return jwsObject.serialize();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
