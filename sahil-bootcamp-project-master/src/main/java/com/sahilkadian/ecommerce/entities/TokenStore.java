package com.sahilkadian.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@Entity
public class TokenStore{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    private User user;

    private String token;
    private String email;
    private Date dateCreated;
}
