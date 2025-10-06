package com.doubleA.UniTrade.security.config;

import com.doubleA.UniTrade.security.jwt.AuthTokenFilter;
import com.doubleA.UniTrade.security.jwt.JwtEntryPoint;
import com.doubleA.UniTrade.security.user.ShopUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ShopConfig {
  @Value("${api.prefix}")
  private static String API;

  // URLs that require users to be authenticated before accessing.
  private static final List<String> SECURED_URLS =
      List.of(API + "/carts/**", API + "/cartItems/**", API + "/orders/**");
  private final ShopUserDetailsService userDetailsService;
  private final JwtEntryPoint authEntryPoint;

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public AuthTokenFilter authTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  // Primary authentication mechanism. It finds the user and check the password.
  public DaoAuthenticationProvider authenticationProvider() {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  // It defines UniTrade's security rules.
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Disable CSRF protection because UniTrade is configured to be stateless
    // (SessionCreationPolicy.STATELESS),
    // so the authentication is handled by a JWT sent with each request. As CSRF protection works by
    // using CSRF token stored in the HTTP session, stateless removes session so no need CSRF
    // protection.
    http.csrf(AbstractHttpConfigurer::disable)
        // set JwtEntryPoint to handle exception of authentication failures.
        .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
        // Configures the session policy to stateless so each request must be independently
        // authenticated. By setting session as stateless, the server doesnt store user's session.
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // requires authentication for SECURED_URLS and allows other URLs to be accessed by public.
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(SECURED_URLS.toArray(String[]::new))
                    .authenticated()
                    .anyRequest()
                    .permitAll());
    // tell the Authentication Manager to use DaoAuthenticationProvider for authenticating users.
    http.authenticationProvider(authenticationProvider());
    // inserts authTokenFilter to run it before the default authentication filter.
    // so Spring Security knows the authorisation of the user to allow/ deny access.
    http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  // allows Frontend to make API calls to the backend.
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry
            .addMapping("/**") // Apply to all endpoints
            .allowedOrigins("http://localhost:5180") // Allow this origin
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP methods
            .allowedHeaders("*") // Allow all headers
            .allowCredentials(true); // Allow credentials
      }
    };
  }
}
