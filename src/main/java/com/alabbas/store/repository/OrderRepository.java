package com.alabbas.store.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.alabbas.store.entity.Customer;
import com.alabbas.store.entity.Order;
import com.alabbas.store.entity.Product;
import com.alabbas.store.enums.OrderStatus;
import com.alabbas.store.enums.PaymentMethod;
import com.alabbas.store.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerPhone(String phone);

    Page<Order> findById(Long categoryId, Pageable pageable);

    @Query("""
        SELECT o FROM Order o
        WHERE
          (
              :customerName IS NULL
              OR :customerName = ''
              OR LOWER(o.customer.name) LIKE LOWER(CONCAT('%', :customerName, '%'))
          )
          AND (
              :customerPhone IS NULL
              OR :customerPhone = ''
              OR o.customer.phone LIKE CONCAT('%', :customerPhone, '%')
          )
          AND (
              :status IS NULL
              OR o.status = :status
          )
          AND (
              :paymentStatus IS NULL
              OR o.paymentStatus = :paymentStatus
          )
          AND (
              :paymentMethod IS NULL
              OR o.paymentMethod = :paymentMethod
          )
          AND (
                 CAST(:createdFrom AS timestamp) IS NULL
              OR o.createdAt >= :createdFrom
          )
          AND (
                CAST(:createdTo AS timestamp) IS NULL
              OR o.createdAt <= :createdTo
          )
        ORDER BY o.createdAt DESC
        """)
    Page<Order> searchOrders(
            @Param("customerName") String customerName,
            @Param("customerPhone") String customerPhone,
            @Param("status") OrderStatus status,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("createdFrom") LocalDateTime createdFrom,
            @Param("createdTo") LocalDateTime createdTo,
            Pageable pageable
    );

}