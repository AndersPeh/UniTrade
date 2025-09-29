package com.doubleA.UniTrade.repository;

import com.doubleA.UniTrade.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  List<CartItem> findByProductId(Long productId);

  void deleteAllByCartId(Long cartId);
}
