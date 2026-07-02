package com.alabbas.store.entity;

import com.alabbas.store.enums.OrderItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation with order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 🔥 relation with product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)

    private Product product;

    // 🔥 snapshot
    private String productName;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    private Integer returnedQuantity;

    @PrePersist
    public void prePersist() {
        this.status = OrderItemStatus.ACTIVE;
        this.returnedQuantity=0;
    }

}