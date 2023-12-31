package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderProduct extends Auditing{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(mappedBy = "orderProduct")
    OrderStatus orderStatus;

    @ManyToOne
    private Order order;

    @ManyToOne
    private ProductVariation productVariation;

    private Long quantity;
    private Double price;

}
