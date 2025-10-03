package com.doubleA.UniTrade.controller;

import com.doubleA.UniTrade.model.Cart;
import com.doubleA.UniTrade.model.User;
import com.doubleA.UniTrade.response.ApiResponse;
import com.doubleA.UniTrade.service.cart.ICartItemService;
import com.doubleA.UniTrade.service.cart.ICartService;
import com.doubleA.UniTrade.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final ICartItemService cartItemService;
  private final IUserService userService;
  private final ICartService cartService;

   @PostMapping("/item/add")
   public ResponseEntity<ApiResponse> addItemToCart( @RequestParam Long productId,  @RequestParam int quantity) {
       // User user = userService.getAuthenticatedUser();
       // Cart cart = cartService.initializeNewCartForUser(user);
       cartItemService.addItemToCart(2L, productId, quantity);
       return ResponseEntity.ok(new ApiResponse("Item added successfully!", null));
   }

  @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
  public ResponseEntity<ApiResponse> removeItemFromCart(
      @PathVariable Long cartId, @PathVariable Long itemId) {
    cartItemService.removeItemFromCart(cartId, itemId);

    return ResponseEntity.ok(new ApiResponse("Item Deletion Success", null));
  }

  @PutMapping("/cart/{cartId}/item/{itemId}/update")
  public ResponseEntity<ApiResponse> updateCartItem(
      @PathVariable Long cartId, @PathVariable Long itemId, @RequestParam int quantity) {
    cartItemService.updateItemQuantity(cartId, itemId, quantity);
    return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
  }
}
