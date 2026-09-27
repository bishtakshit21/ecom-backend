package com.project.ecom.service;

import com.project.ecom.dto.ProductRequestDto;
import com.project.ecom.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto createproduct(ProductRequestDto productRequestDto);
    List<ProductResponseDto> GetallProduct();
    ProductResponseDto GetProductByid(Long id);
    List<ProductResponseDto> getProductByCategory(Long categoryId);
    void deleteProduct (Long id);
}
