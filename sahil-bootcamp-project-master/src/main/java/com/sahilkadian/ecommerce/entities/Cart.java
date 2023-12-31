package com.sahilkadian.ecommerce.entities;

import com.sahilkadian.ecommerce.entities.compositeKey.CartCK;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @EmbeddedId
    private CartCK compositeKey;

    private Long quantity;
}
