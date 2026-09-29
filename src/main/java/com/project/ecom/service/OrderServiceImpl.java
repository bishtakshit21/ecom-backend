package com.project.ecom.service;

import com.project.ecom.Exception.ResourceNotFoundException;
import com.project.ecom.dto.OrderItemRequestDto;
import com.project.ecom.dto.OrderItemResponseDto;
import com.project.ecom.dto.OrderRequestDto;
import com.project.ecom.dto.OrderResponseDto;
import com.project.ecom.model.*;
import com.project.ecom.repository.OrderRepo;
import com.project.ecom.repository.ProductRepo;
import com.project.ecom.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service

public class OrderServiceImpl implements OrderService{
    private ProductRepo productRepo;
    private OrderRepo orderRepo;
    private UserRepo userRepo;
    public OrderServiceImpl(ProductRepo productRepo,OrderRepo orderRepo,UserRepo userRepo){
        this.orderRepo=orderRepo;
        this.productRepo=productRepo;
        this.userRepo=userRepo;
    }
    @Override
@Transactional
    @Caching(put={@CachePut(value = "order", key = "#result.orderId")},
            evict = {@CacheEvict(value = "order",key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")})
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();//here we got the user from the current security context means we do not need to provide the user the currently logged-in user gets automatically picked up
        User user =userRepo.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("you are not authorized to perform this action first log in or register  "));
        Order order=new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("placed");
        order.setUser(user);
        List<OrderItem> orderItems=new ArrayList<>();
        double totalprice=0.0;

        for (OrderItemRequestDto orderItemRequestDto: orderRequestDto.getItems()){
            Product product=productRepo.findById(orderItemRequestDto.getProductId()).
                    orElseThrow(()->new ResourceNotFoundException("the product with this id does not exist:"+orderItemRequestDto.getProductId()));

            if (product.getStockQuantity()<orderItemRequestDto.getQuantity()){
                throw new ResourceNotFoundException("the quantity is less available then required");
            }
            product.setStockQuantity(product.getStockQuantity()-orderItemRequestDto.getQuantity());
            productRepo.save(product);

            OrderItem orderItem=new OrderItem(orderItemRequestDto.getQuantity(),product.getPrice(),order,product);
            orderItems.add(orderItem);
            totalprice+=product.getPrice()*orderItemRequestDto.getQuantity();
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalprice);
        Order saveorder=orderRepo.save(order);
        return mapToResponseDto(saveorder);
    }

    @Override
    @Cacheable(value = "order",key = "#id")
    public OrderResponseDto GetOrderById(Long id) {
        Order order=orderRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("the order does not exist with the id : "+id));
        return mapToResponseDto(order);
    }

    @Override
    @Cacheable(value = "order",key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
    public List<OrderResponseDto> GetOrderByUser() {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        return orderRepo.findByUserEmail(email).stream()
                                  .map(this::mapToResponseDto)
                                  .toList();
    }
    private OrderResponseDto mapToResponseDto(Order order) {
        List<OrderItemResponseDto> itemDtos = order.getOrderItems().stream()
                .map(item -> new OrderItemResponseDto(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()))
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getUser().getName(),
                order.getTotalAmount(),
                order.getOrderDate(),
                itemDtos
        );
}}
