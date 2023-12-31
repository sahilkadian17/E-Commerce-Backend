package com.sahilkadian.ecommerce.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Boolean isActive;
    private String contact;
    private String created_by;
    private Date date_created;
    private String updated_by;
    private Date last_updated;
}
