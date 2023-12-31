package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "userId")
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    List<Address> addresses;

    @OneToMany(mappedBy = "customer")
    List<ProductReview> productReviews;

    @OneToMany(mappedBy = "customer")
    List<Order> orders;

    private String contact;
}
