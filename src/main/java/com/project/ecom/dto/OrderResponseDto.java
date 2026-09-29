package com.project.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto implements Serializable {
    private static final long serialVersionUID=1L;
    private Long OrderId;
    private Long UserId;
    private String Status;
    private String Username;
    private Double TotalAmount;
    private LocalDateTime OrderDate;
    private List<OrderItemResponseDto> items;
}
