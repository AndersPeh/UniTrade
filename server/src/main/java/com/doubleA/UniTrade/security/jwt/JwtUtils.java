package com.doubleA.UniTrade.security.jwt;

import com.doubleA.UniTrade.security.user.ShopUserDetails;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
  @Value("${auth.token.jwtSecret}")
  private String jwtSecret;

  @Value("${auth.token.accessExpirationInMils}")
  private String expirationTime;

  @Value("${auth.token.refreshExpirationInMils}")
  private String refreshExpirationTime;

  public String generateAccessTokenForUser(Authentication authentication) {
    // get the login user.
    ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();

    // get roles of the login user,
    List<String> roles =
        userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

    // To generate a JWT access token.
    return Jwts.builder()
        // Set subject as email to identify user.
        .setSubject(userPrincipal.getEmail())
        // set properties to embed in the access token using claim. need to make sure these data are
        // available in ShopUserDetails.
        .claim("id", userPrincipal.getId())
        .claim("roles", roles)
        .setIssuedAt(new Date())
        .setExpiration(calculateExpirationDate(expirationTime))
        // use SHA-256 in digital signature for integrity.
        .signWith(key(), SignatureAlgorithm.HS256)
        // combines the header, payload (claims) and the signature into a JWT token.
        // When combining, it uses Base64Url encoding to replace characters that have special
        // meaning URLs with alternatives. So the JWT token wont be misinterpreted when transmitted.
        // For example, / is replaced with _
        .compact();
  }

  // To generate a refresh token for retrieving access token.
  public String generateRefreshToken(String email) {
    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(calculateExpirationDate(refreshExpirationTime))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  private Date calculateExpirationDate(String expirationTimeString) {
    long expirationTime = Long.parseLong(expirationTimeString); // Convert String to long
    return new Date(System.currentTimeMillis() + expirationTime);
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
  }

  //
  public boolean validateToken(String token) {
    try {
      // creates a JwtParserBuilder to configure how the JWT string will be parsed.
      Jwts.parser()
          // use the secret key to recalculate the token's signature from the header and payload,
          // then compare with the signature in the token for integrity.
          // It is the same secret key used to sign the token initially.
          .setSigningKey(key())
          // constructs the JwtParser instance.
          .build()
          // carries out verification of the token then parses the token's payload into claims.
          .parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      throw new JwtException(e.getMessage());
    }
  }
}
