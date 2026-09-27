package com.project.ecom.service;

import com.project.ecom.dto.OrderRequestDto;
import com.project.ecom.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    OrderResponseDto GetOrderById(Long id);
    List<OrderResponseDto> GetOrderByUser();
}
