package com.sahilkadian.ecommerce.entities.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CartCK implements Serializable {

    private Long customerUserId;
    private Long productVariationId;
}
