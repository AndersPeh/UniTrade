package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.request.LoginRequest;
import com.doubleA.UniTrade.security.jwt.JwtUtils;
import com.doubleA.UniTrade.security.user.ShopUserDetailsService;
import com.doubleA.UniTrade.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
  private final JwtUtils jwtUtils;

  private final CookieUtils cookieUtils;

  private final ShopUserDetailsService userDetailsService;

  private final AuthenticationManager authenticationManager;

  @Value("${auth.token.refreshExpirationInMils}")
  private Long refreshTokenExpirationTime;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(
      @RequestBody LoginRequest request, HttpServletResponse response) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    String accessToken = jwtUtils.generateAccessTokenForUser(authentication);

    String refreshToken = jwtUtils.generateRefreshToken(request.getEmail());

    cookieUtils.addRefreshTokenCookie(response, refreshToken, refreshTokenExpirationTime);

    Map<String, String> token = new HashMap<>();

    token.put("accessToken", accessToken);

    // As refresh token is sent as an HttpOnly cookie, addRefreshTokenCookie adds Set-Cookie header
    // to the HTTP response,
    // so the browser will store the refresh token away from being able to accessed by Javascript.
    return ResponseEntity.ok(token);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
    cookieUtils.logCookies(request);

    String refreshToken = cookieUtils.getRefreshTokenFromCookies(request);

    if (refreshToken != null) {
      boolean isValid = jwtUtils.validateToken(refreshToken);

      if (isValid) {
        String userNameFromToken = jwtUtils.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userNameFromToken);
        String newAccessToken =
            jwtUtils.generateAccessTokenForUser(
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()));
        if (newAccessToken != null) {
          Map<String, String> token = new HashMap<>();

          token.put("accessToken", newAccessToken);

          return ResponseEntity.ok(token);
        } else {
          return ResponseEntity.status(500).body("Error generating new access token.");
        }
      }
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or Expired access token");
  }
}
