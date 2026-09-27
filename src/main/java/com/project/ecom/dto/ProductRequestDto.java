package com.project.ecom.dto;

import com.project.ecom.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Long categoryId;

}
