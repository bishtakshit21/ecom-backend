package com.project.ecom.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category",cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Product> products=new ArrayList<>();

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
