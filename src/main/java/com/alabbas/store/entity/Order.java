package com.alabbas.store.entity;

import com.alabbas.store.enums.OrderStatus;
import com.alabbas.store.enums.PaymentMethod;
import com.alabbas.store.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  relation مع customer
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    //  order items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    //  status
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //  total price
    private BigDecimal totalPrice;

    private String notes;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private BigDecimal discountAmount;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = OrderStatus.NEW;

    }
    // we fel order hzwed payment state we field for remaining amount (partial payment)
    // we handle cart incase logged in API  incase not logged in save local

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
