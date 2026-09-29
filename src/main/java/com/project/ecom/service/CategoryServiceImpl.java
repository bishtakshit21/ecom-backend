package com.project.ecom.service;

import com.project.ecom.Exception.ResourceNotFoundException;
import com.project.ecom.dto.CategoryDto;
import com.project.ecom.model.Category;
import com.project.ecom.repository.CategoryRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepo categoryRepo;
    public CategoryServiceImpl(CategoryRepo categoryRepo){
        this.categoryRepo=categoryRepo;
    }

    @Override
    @Caching(put={ @CachePut( value = "category",key = "#result.id")},
             evict = {@CacheEvict(value = "category",key = "'all'")})

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category=new Category(categoryDto.getName(),categoryDto.getDescription());
        Category saved=categoryRepo.save(category);
        return new CategoryDto(saved.getId(),saved.getName(),saved.getDescription());
    }

    @Override
    @Cacheable(value = "category",key = "'all'")
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "category" , key = "#id")
    public CategoryDto getCategoryById(Long id) {
        Category category=categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("category not found with id - " + id));
        return new CategoryDto(category.getId(), category.getName(), category.getDescription());
    }

    @Override
    @Caching(evict={ @CacheEvict( value = "category",key = "#id"),
                     @CacheEvict(value = "category",key = "'all'")})
    public void deleteCategory(Long id) {
        if(!categoryRepo.existsById(id)){
            throw new ResourceNotFoundException("the categpry does not exists with given id - "+ id);
        }
        categoryRepo.deleteById(id);
    }
}
