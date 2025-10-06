package com.doubleA.UniTrade.service.cart;

import com.doubleA.UniTrade.dtos.CartItemDto;
import com.doubleA.UniTrade.model.CartItem;

public interface ICartItemService {
  CartItem addItemToCart(Long cartId, Long productId, int quantity);

  void removeItemFromCart(Long cartId, Long productId);

  void updateItemQuantity(Long cartId, Long productId, int quantity);

  CartItem getCartItem(Long cartId, Long productId);

  CartItemDto convertToDto(CartItem cartItem);
}
