package com.project.ecom.controller;

import com.project.ecom.dto.OrderRequestDto;
import com.project.ecom.dto.OrderResponseDto;
import com.project.ecom.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderRequestDto requestDto) {
        return new ResponseEntity<>(orderService.createOrder(requestDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.GetOrderById(id));
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser() {
        return ResponseEntity.ok(orderService.GetOrderByUser());
    }
}