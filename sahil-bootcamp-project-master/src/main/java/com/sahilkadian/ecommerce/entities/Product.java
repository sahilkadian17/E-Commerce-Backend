package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends Auditing{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "product")
    Set<ProductReview> productReviews;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sellerId")
    private Seller seller;

    private String brand;
    private Boolean isReturnable;
    private Boolean isCancellable;
    private Boolean isActive;
    private Boolean isDeleted;

}
