package com.doubleA.UniTrade.service.cart;

import com.doubleA.UniTrade.dtos.CartDto;
import com.doubleA.UniTrade.model.Cart;
import com.doubleA.UniTrade.model.User;
import com.doubleA.UniTrade.repository.CartItemRepository;
import com.doubleA.UniTrade.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ModelMapper mapper;

  @Override
  public Cart getCart(Long cartId) {
    Cart cart =
        cartRepository
            .findById(cartId)
            .orElseThrow(() -> new EntityNotFoundException("Cart not found!"));
    BigDecimal totalAmount = cart.getTotalAmount();
    cart.setTotalAmount(totalAmount);
    return cartRepository.save(cart);
  }

  @Override
  public Cart getCartByUserId(Long userId) {
    return cartRepository.findByUserId(userId).orElse(null);
  }

  @Override
  public void clearCart(Long cartId) {
    Cart cart = getCart(cartId);
    cartItemRepository.deleteAllByCartId(cartId);
    cart.clearCart();
    cartRepository.deleteById(cartId);
  }

  @Override
  public Cart initialiseNewCartForUser(User user) {
    return Optional.ofNullable(getCartByUserId(user.getId()))
        // Only create a new cart if there is no cart belongs to the user.
        .orElseGet(
            () -> {
              Cart cart = new Cart();
              cart.setUser(user);
              return cartRepository.save(cart);
            });
  }

  @Override
  public BigDecimal getTotalPrice(Long cartId) {
    Cart cart = getCart(cartId);
    return cart.getTotalAmount();
  }

  @Override
  public CartDto convertToDto(Cart cart) {
    return mapper.map(cart, CartDto.class);
  }
}
