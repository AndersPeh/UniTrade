package com.doubleA.UniTrade.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtils {

  @Value("${app.useSecureCookie}")
  private boolean useSecureCookie;

  public void addRefreshTokenCookie(
      HttpServletResponse response, String refreshToken, long maxAge) {
    if (response == null) {
      throw new IllegalArgumentException("HttpServletResponse cannot be null");
    }
    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    // it prevents cookie from being accessed by client-side scripts to mitigate cross-site
    // scripting attacks. Javascript code cannot read the cookie so any malicious code cannot read
    // it.
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge((int) (maxAge / 1000));
    // Sends cookie over HTTPS connection only if useSecureCookie is true.
    refreshTokenCookie.setSecure(useSecureCookie);
    // mitigates CSRF attacks by controlling when the browser sends the cookie.
    // Lax means the browser only sends the cookie for UniTrade or when a user navigates directly
    // to UniTrade site. It won't send the cookie to another site.
    // None means cross-site requests are allowed but it required Secure flag (HTTPS).
    String sameSite = useSecureCookie ? "None" : "Lax";
    setResponseHeader(response, refreshTokenCookie, sameSite);
  }

  private void setResponseHeader(
      HttpServletResponse response, Cookie refreshTokenCookie, String sameSite) {
    StringBuilder cookieHeader = new StringBuilder();
    cookieHeader
        .append(refreshTokenCookie.getName())
        .append("=")
        .append(refreshTokenCookie.getValue())
        .append("; HttpOnly; Path=")
        .append(refreshTokenCookie.getPath())
        .append("; Max-Age=")
        .append(refreshTokenCookie.getMaxAge())
        .append(useSecureCookie ? "; Secure" : "")
        .append("; SameSite=")
        .append(sameSite);
    response.setHeader("Set-Cookie", cookieHeader.toString());
  }

  // retrieves refresh token from HttpServletRequest.
  public String getRefreshTokenFromCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      // iterates through cookies from the browser to return the cookie named refreshToken.
      for (Cookie cookie : cookies) {
        System.out.println("Names of the cookie found: " + cookie.getName());
        if ("refreshToken".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  // for debugging by printing out name and value of all cookies in a HttpServletRequest.
  public void logCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    System.out.println("Cookies: " + (cookies != null ? Arrays.toString(cookies) : "null"));
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        System.out.println("Cookie name: " + cookie.getName() + ", value: " + cookie.getValue());
      }
    }
  }
}
