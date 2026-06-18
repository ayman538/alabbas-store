package com.alabbas.store.repository;


import com.alabbas.store.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPhone(String phone);
    Customer getByPhone(String phone);

    boolean existsByEmailIgnoreCase(String email);
}