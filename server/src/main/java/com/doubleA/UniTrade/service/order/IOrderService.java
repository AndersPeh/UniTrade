package com.doubleA.UniTrade.service.order;

import com.doubleA.UniTrade.dtos.OrderDto;
import com.doubleA.UniTrade.model.Order;
import com.doubleA.UniTrade.request.PaymentRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface IOrderService {
  Order placeOrder(Long userId);

  List<OrderDto> getUserOrders(Long userId);

  // Because Stripe only accepts the smallest denomination, for AUD, the smallest is cent,
  // so need to convert the amount from frontend to cent by multiplying 100.
  String createPaymentIntent(PaymentRequest request) throws StripeException;

  OrderDto convertToDto(Order order);
}
