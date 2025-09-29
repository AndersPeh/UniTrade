package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.model.Cart;
import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.service.cart.ICartItemService;
import com.doubleA.UniTrade.service.cart.ICartService;
import com.doubleA.UniTrade.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {

  private final ICartService cartService;

  @GetMapping("/user/{userId}/cart")
  public ResponseEntity<ApiResponse> getUserCart(@PathVariable Long userId) {
    Cart cart = cartService.getCartByUserId(userId);

    return ResponseEntity.ok(new ApiResponse("Success", cart));
  }

  @DeleteMapping("/cart/{cartId}/clear")
  public void clearCart(@PathVariable Long cartId) {
    cartService.clearCart(cartId);
  }
}
