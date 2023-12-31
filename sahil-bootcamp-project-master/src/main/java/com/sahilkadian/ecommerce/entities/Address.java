package com.sahilkadian.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String city;
    private String state;
    private String country;
    private Long address_line;
    private Long zip_code;
    private String label;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id")
    Customer customer;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "seller_id")
    Seller seller;
}
