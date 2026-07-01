package com.alabbas.store.controller;
import com.alabbas.store.dto.*;
import com.alabbas.store.entity.Order;
import com.alabbas.store.enums.OrderStatus;
import com.alabbas.store.enums.PaymentMethod;
import com.alabbas.store.enums.PaymentStatus;
import com.alabbas.store.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MessageSource messageSource;

    @PostMapping("/api/public/orders")
    public ApiResponse createOrder(@Valid @RequestBody OrderRequest request) {

        Order order = orderService.createOrder(request);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "order.created.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(201)
                .message(message + " - ID: " + order.getId())
                .build();
    }

    @PatchMapping("/api/private/orders/{id}/status")
    public ApiResponse updateOrderStatus(@PathVariable Long id,
                                         @Valid @RequestBody UpdateOrderStatusRequest request) {

        orderService.updateOrderStatus(id, request);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "order.status.updated.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

    @GetMapping("/api/private/orders")
    public Page<OrderResponse> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/api/public/orders/{id}/details")
    public OrderResponse getOrderDetails(@PathVariable Long id) {
        return orderService.getOrderDetails(id );
    }


    @GetMapping("/api/public/orders/customer")
    public List<OrderResponse> getOrdersByCustomerPhone(@RequestParam String phone) {
        return orderService.getOrdersByCustomerPhone(phone);
    }


    @GetMapping("/api/public/orders/search")
    public Page<OrderResponse> searchOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerPhone,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return orderService.searchOrders(
                customerName,
                customerPhone,
                status,
                paymentStatus,
                paymentMethod,
                createdFrom,
                createdTo,
                pageable
        );
    }

    @PatchMapping("/api/public/orders/{id}/pay")
    public ApiResponse payOrder(@PathVariable Long id,
                                @Valid @RequestBody PayOrderRequest request) {

        orderService.payOrder(id, request);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "order.payment.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

    @PatchMapping("/api/private/orders/{orderId}/items/{itemId}/return")
    public ApiResponse markOrderItemAsReturned(@PathVariable Long orderId,
                                               @PathVariable Long itemId,
                                               @Valid @RequestBody ReturnOrderItemRequest request) {

        orderService.ChangeItemStatus(orderId, itemId , request);

        Locale locale = LocaleContextHolder.getLocale();

        String message = messageSource.getMessage(
                "order.item.returned.success",
                null,
                locale
        );

        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }

}