package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderStatus extends Auditing{

    @Id
    @OneToOne
    @JoinColumn(name = "orderProductId")
    private OrderProduct orderProduct;

    private String fromStatus;
    private String toStatus;
    private String transitionNotesComments;
    private Date transitionDate;
}
