package com.doubleA.UniTrade.model;

import com.doubleA.UniTrade.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity

//Because order table name is a reserved keyword in SQL databases, need to rename it to orders.
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;

// Tells JPA to store OrderStatus (PENDING, SHIPPED etc) as enum value
// instead of enum position number. So when order changes in server/src/main/java/com/doubleA/UniTrade/enums/OrderStatus.java,
// the value will persist instead of changing according to enum position number.
// public enum OrderStatus {
//    PENDING (this is enum string),   // 0 (this is enum position number / ordinal)
//    SHIPPED,   // 1 }
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

// Add a column named user_id to Order entity as foreign key to establish Many Order to One User relationship.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

// Order is the inverse side, when it is deleted, associated OrderItem should also be deleted.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();
}
