package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "userId")
public class Seller extends User {

    @OneToOne(mappedBy = "seller",cascade = CascadeType.ALL)
    Address address;

    @OneToMany(mappedBy = "seller")
    List<Product> products;

    private String gst;
    private String companyContact;
    private String companyName;
}
