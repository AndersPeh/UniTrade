package com.doubleA.UniTrade.service.cart;

import com.doubleA.UniTrade.model.Cart;
import com.doubleA.UniTrade.model.User;

import java.math.BigDecimal;

public interface ICartService {
  Cart getCart(Long cartId);

  Cart getCartByUserId(Long userId);

  void clearCart(Long cartId);

  Cart initialiseNewCartForUser(User user);

  BigDecimal getTotalPrice(Long cartId);
}
