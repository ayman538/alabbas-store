package com.alabbas.store.repository;

import com.alabbas.store.entity.Customer;
import com.alabbas.store.entity.Order;
import com.alabbas.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerPhone(String phone);

    Page<Order> findById(Long categoryId, Pageable pageable);

}