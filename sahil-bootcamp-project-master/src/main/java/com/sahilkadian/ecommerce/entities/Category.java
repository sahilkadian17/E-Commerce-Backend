package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentCategory")
    Category category;

    @OneToMany(mappedBy = "category")
    Set<Product> products;

    @OneToMany(mappedBy = "category")
    Set<CategoryMetadataFieldValues> categoryMetadataFieldValues;

    private String name;
}
