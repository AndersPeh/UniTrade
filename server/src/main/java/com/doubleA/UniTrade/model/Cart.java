package com.doubleA.UniTrade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount;

// Cart is selected to be the owning side, so it has to store the foreign key to establish
// the One User to One Cart relationship.
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
