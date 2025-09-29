package com.doubleA.UniTrade.service.order;

import com.doubleA.UniTrade.model.Order;

import java.util.List;

public interface IOrderService {
  Order placeOrder(Long userId);

  List<Order> getUserOrders(Long userId);
}
