package com.sahilkadian.ecommerce.entities;

import com.sahilkadian.ecommerce.entities.compositeKey.ProductReviewCK;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductReview {

    @EmbeddedId
    private ProductReviewCK compositeKey;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    @ManyToOne
    @MapsId("customerUserId")
    private Customer customer;

    private Double rating;
    private String review;
}
