package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CategoryMetadataField {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "categoryMetadataField")
    Set<CategoryMetadataFieldValues> categoryMetadataFieldValues;

    private String name;
}
