package com.doubleA.UniTrade.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal totalAmount = BigDecimal.ZERO;

  // Cart is selected to be the owning side, so it has to store the foreign key to establish
  // the One User to One Cart relationship.
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  // Cart is the inverse side, when it is deleted, associated CartItem should also be deleted.
  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CartItem> items = new HashSet<>();

  public void removeItem(CartItem cartItem) {
    this.items.remove(cartItem);
    cartItem.setCart(null);

    updateTotalAmount();
  }

  public void addItem(CartItem cartItem) {
    this.items.add(cartItem);
    cartItem.setCart(this);

    updateTotalAmount();
  }

  private void updateTotalAmount() {
    this.totalAmount =
        // For every cartItem,
        items.stream()
            .map(
                item -> {
                  // get the unit price and set it as 0 if it is null
                  BigDecimal unitPrice = item.getUnitPrice();
                  if (unitPrice == null) {
                    return BigDecimal.ZERO;
                  }
                  // multiply the unit price by the quantity of current cartItem
                  return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
            // sum up the result of multiplication (unitPrice x quantity).
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void clearCart() {
    this.items.clear();
    updateTotalAmount();
  }
}
