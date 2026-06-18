package com.alabbas.store.controller;


import com.alabbas.store.entity.Customer;
import com.alabbas.store.entity.Order;
import com.alabbas.store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class HealthController {
   private final OrderService orderService;

    @GetMapping("/api/public/health")
    public String health() {
        return "Al Abbas Store API is running";

    }

    @GetMapping("/api/test")
    public String test() {
        return "Al Abbas Store API is running";

    }




}
