package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.dtos.OrderDto;
import com.doubleA.UniTrade.model.Order;
import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  private final IOrderService orderService;

  @PostMapping("/user/order")
  public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId) {
    Order order = orderService.placeOrder(userId);
    OrderDto orderDto = orderService.convertToDto(order);
    return ResponseEntity.ok(new ApiResponse("Successful Order", orderDto));
  }

  @GetMapping("/user/{userId}/order")
  public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
    List<Order> orders = orderService.getUserOrders(userId);

    return ResponseEntity.ok(new ApiResponse("Success", orders));
  }
}
