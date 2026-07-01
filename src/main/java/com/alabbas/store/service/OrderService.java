package com.alabbas.store.service;

import com.alabbas.store.dto.*;
import com.alabbas.store.entity.*;
import com.alabbas.store.enums.OrderItemStatus;
import com.alabbas.store.enums.OrderStatus;
import com.alabbas.store.enums.PaymentMethod;
import com.alabbas.store.enums.PaymentStatus;
import com.alabbas.store.exception.BusinessException;
import com.alabbas.store.exception.ResourceNotFoundException;
import com.alabbas.store.repository.*;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CustomerRepository customerRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder(OrderRequest request) {

        // 🔥 1. get or create customer
        Customer customer = customerRepository
                .findAll()
                .stream()
                .filter(c -> c.getPhone().equals(request.getPhone()))
                .findFirst()
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .name(request.getName())
                            .phone(request.getPhone())
                            .email(request.getEmail())
                            .area(request.getArea())
                            .buildingNumber(request.getBuildingNumber())
                            .floor(request.getFloor())
                            .apartment(request.getApartment())
                            .address(request.getAddress())
                            .build();
                    return customerRepository.save(newCustomer);
                });

        // 🔥 2. create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setNotes(request.getNotes());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setPaidAmount(request.getPaidAmount());


        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // 🔥 3. loop on items
        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("product.not.found"));

            BigDecimal unitPrice = product.getPrice();
            int quantity = itemRequest.getQuantity();
            if (product.getStockQuantity() < quantity) {
                product.setStockQuantity(0);
            } else {
                product.setStockQuantity(product.getStockQuantity() - quantity);
            }
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getNameEn()) // snapshot
                    .unitPrice(unitPrice)
                    .quantity(quantity)
                    .totalPrice(itemTotal)
                    .build();

            orderItems.add(orderItem);
            total = total.add(itemTotal);

        }

        // 🔥 4. set order fields
        order.setItems(orderItems);
        order.setTotalPrice(total);
        calculatePayment(order);
        // 🔥 5. save
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long id, UpdateOrderStatusRequest request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order.not.found"));

        order.setStatus(request.getStatus());
        return orderRepository.save(order);
    }

    public OrderResponse getOrderDetails(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order.not.found"));
        return mapToOrderResponse(order);

    }

    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::mapToOrderResponse);

    }


    public List<OrderResponse> getOrdersByCustomerPhone(String phone) {
        return orderRepository.findByCustomerPhone(phone)
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }


    private void calculatePayment(Order order) {

        BigDecimal total = order.getTotalPrice();
        BigDecimal paid = order.getPaidAmount() != null
                ? order.getPaidAmount()
                : BigDecimal.ZERO;

        if (order.getPaymentStatus() == PaymentStatus.PAID) {

            BigDecimal discount = total.subtract(paid);

            order.setDiscountAmount(discount.max(BigDecimal.ZERO));
            order.setRemainingAmount(BigDecimal.ZERO);
            order.setPaymentStatus(PaymentStatus.PAID);

        } else if (order.getPaymentStatus() == PaymentStatus.PARTIALLY_PAID) {

            BigDecimal remaining = total.subtract(paid);

            order.setRemainingAmount(remaining.max(BigDecimal.ZERO));
            order.setDiscountAmount(BigDecimal.ZERO);
            order.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);

        } else if (order.getPaymentStatus() == PaymentStatus.REFUNDED) {
            order.setPaymentStatus(PaymentStatus.REFUNDED);

        } else {

            order.setPaidAmount(BigDecimal.ZERO);
            order.setRemainingAmount(total);
            order.setDiscountAmount(BigDecimal.ZERO);
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }
    }

    public Order payOrder(Long orderId, PayOrderRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order.not.found"));

        BigDecimal currentPaid = order.getPaidAmount() != null
                ? order.getPaidAmount()
                : BigDecimal.ZERO;

        BigDecimal newPaidAmount = currentPaid.add(request.getPaidAmount());

        order.setPaidAmount(newPaidAmount);

        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
        }
        calculatePayment(order);
        return orderRepository.save(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomer().getName())
                .customerPhone(order.getCustomer().getPhone())
                .customerArea(order.getCustomer().getArea())
                .customerAddress(order.getCustomer().getAddress())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .paidAmount(order.getPaidAmount())
                .remainingAmount(order.getRemainingAmount())
                .discountAmount(order.getDiscountAmount())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .items(order.getItems()
                        .stream()
                        .map(item -> OrderItemResponse.builder()
                                .productId(item.getProduct().getId())
                                .productName(item.getProductName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .status(item.getStatus())
                                .itemId(item.getId())
                                .build())
                        .toList())
                .build();
    }

    public void ChangeItemStatus(Long orderId, Long itemId, ReturnOrderItemRequest request) {

        OrderItem item = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order.item.not.found"));
        Product product = item.getProduct();

        int remainingQuantity = item.getQuantity() - item.getReturnedQuantity();

        if (request.getQuantity() > remainingQuantity) {
            throw new BusinessException("returned.quantity.exceeds.available.quantity");
        }

        item.setReturnedQuantity(item.getReturnedQuantity() + request.getQuantity());

        if (item.getReturnedQuantity().equals(item.getQuantity())) {
            item.setStatus(OrderItemStatus.RETURNED);
        } else {
            item.setStatus(OrderItemStatus.PARTIALLY_RETURNED);
        }
        //update Product quantity Stock
        product.setStockQuantity(product.getStockQuantity() + request.getQuantity());

        orderItemRepository.save(item);
    }

    public Page<OrderResponse> searchOrders(
            String customerName,
            String customerPhone,
            OrderStatus status,
            PaymentStatus paymentStatus,
            PaymentMethod paymentMethod,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            Pageable pageable) {
        return orderRepository.searchOrders(
                customerName,
                customerPhone,
                status,
                paymentStatus,
                paymentMethod,
                createdFrom,
                createdTo,
                pageable
        ).map(this::mapToOrderResponse);
    }
}




