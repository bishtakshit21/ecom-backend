package com.project.ecom.repository;

import com.project.ecom.model.Category;
import com.project.ecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
