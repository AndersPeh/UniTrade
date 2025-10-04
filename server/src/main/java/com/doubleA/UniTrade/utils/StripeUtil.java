package com.doubleA.UniTrade.utils;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter

// When the server runs, StripeUtil will be created in the Spring container.
public class StripeUtil {

  // Then the api key will be injected to here.
  @Value("${stripe.secret.key}")
  private String stripeSecretKey;

  // Then the API key will be used to initialise Stripe.apiKey. Whenever
  // Stripe needs an apiKey, it is always available throughout the server.
  // For example, createPaymentIntent method in OrderService needs Stripe.apiKey to create
  // PaymentIntent.
  @PostConstruct
  public void init() {
    Stripe.apiKey = stripeSecretKey;
  }
}
