package com.doubleA.UniTrade.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component

// For defining what should happen when an unauthenticated user tries to access a secured endpoint
// without a valid JWT token.
public class JwtEntryPoint implements AuthenticationEntryPoint {

  @Override
  // When an AuthenticationException is thrown, this method is called automatically.
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    // Indicate the server is sending back JSON data.
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    // Set HTTP status code to 401 Unauthorized.
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    // Create a custom JSON body containing error message.
    final Map<String, Object> body = new HashMap<>();
    body.put("Error :", "Unauthorized");
    body.put("Message :", "Invalid credentials");
    final ObjectMapper mapper = new ObjectMapper();
    // write the JSON object to the HTTP response.
    mapper.writeValue(response.getOutputStream(), body);
  }
}
