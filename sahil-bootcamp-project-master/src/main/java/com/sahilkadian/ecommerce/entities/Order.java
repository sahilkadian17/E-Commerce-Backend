package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends Auditing{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="customerUserId")
    Customer customer;

    @OneToMany(mappedBy = "order")
    List<OrderProduct> orderProduct;

    private Double amountPaid;
    private String paymentMethod;
    private String customerAddressCity;
    private String customerAddressState;
    private String customerAddressCountry;
    private Long customerAddressAddressLine;
    private Long customerAddressZipCode;
    private String customerAddressLabel;
}
