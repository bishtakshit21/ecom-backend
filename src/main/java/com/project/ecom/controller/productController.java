package com.project.ecom.controller;

import com.project.ecom.dto.ProductRequestDto;
import com.project.ecom.dto.ProductResponseDto;
import com.project.ecom.model.Product;
import com.project.ecom.service.ProductService;
import com.project.ecom.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Retention;
import java.util.List;

@RestController
@RequestMapping("/api/product")

public class productController {
    private ProductService productService;
    public productController (ProductService productService){
        this.productService=productService;
    }
@GetMapping("/fetchall")
public ResponseEntity <List<ProductResponseDto>> FetchAll(){
    return ResponseEntity.ok(productService.GetallProduct());
}
@GetMapping("/fetch/{id}")
    public ResponseEntity<ProductResponseDto> GetById(@PathVariable Long id){
        return ResponseEntity.ok(productService.GetProductByid(id));
}
    @GetMapping("/fetchbycategory/{category_id}")
    public ResponseEntity<List<ProductResponseDto>> GetBycategoryId(@PathVariable Long category_id){
        return ResponseEntity.ok(productService.getProductByCategory(category_id));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/Create")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto){
        return new ResponseEntity<>(productService.createproduct(productRequestDto), HttpStatus.CREATED);
}

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> Delete (@PathVariable Long id){
    productService.deleteProduct(id);
    return ResponseEntity.ok("sucessfully deleted");
        }

}

