package com.project.ecom.service;

import com.project.ecom.Exception.ResourceNotFoundException;
import com.project.ecom.dto.ProductRequestDto;
import com.project.ecom.dto.ProductResponseDto;
import com.project.ecom.model.Category;
import com.project.ecom.model.Product;
import com.project.ecom.repository.CategoryRepo;
import com.project.ecom.repository.ProductRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    private CategoryRepo categoryRepo;
    private ProductRepo productRepo;
    public ProductServiceImpl(CategoryRepo categoryRepo,
                          ProductRepo productRepo){
        this.categoryRepo=categoryRepo;
        this.productRepo=productRepo;

    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "product",key = "'all'"),
                     @CacheEvict(value = "products",key="#result.category_id")},
            put = {@CachePut(value = "product",key ="#result.id" )}
    )
    public ProductResponseDto createproduct(ProductRequestDto productRequestDto) {
        Category category=categoryRepo.findById(productRequestDto.getCategoryId()).
                orElseThrow(()-> new ResourceNotFoundException("the category does not exist by id - "+productRequestDto.getCategoryId()));

        Product product=new Product();
        product.setName(productRequestDto.getName());
        product.setCategory(category);
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setStockQuantity(productRequestDto.getStockQuantity());

        Product saved = productRepo.save(product);
        return mapToResponse(saved);
    }

    @Override
    @Cacheable(value = "product",key = "'all'")
    public List<ProductResponseDto> GetallProduct() {
        return productRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "product",key = "#id")
    public ProductResponseDto GetProductByid(Long id) {
        Product product=productRepo.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("the product does not exist with id - " + id ));
        return mapToResponse(product);
    }

    @Override
    @Cacheable(value = "products",key = "#categoryId")
    public List<ProductResponseDto> getProductByCategory(Long categoryId) {
        return productRepo.findByCategory_id(categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override

    @Caching(
            evict = {@CacheEvict(value = "product",allEntries = true),
                     @CacheEvict(value = "products",allEntries = true)}
    )
    public void deleteProduct(Long id) {
        if(!productRepo.existsById(id)){
            throw new ResourceNotFoundException("the [roduct does not exist");

        }
        productRepo.deleteById(id);
    }

    public ProductResponseDto mapToResponse(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription()  ,
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
