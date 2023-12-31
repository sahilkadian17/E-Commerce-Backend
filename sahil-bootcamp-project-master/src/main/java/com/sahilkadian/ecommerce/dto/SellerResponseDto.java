package com.sahilkadian.ecommerce.dto;

import com.sahilkadian.ecommerce.entities.Address;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
public class SellerResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Boolean isActive;
    private String companyName;
    private Address companyAddress;
    private String companyContact;
    private String created_by;
    private Date date_created;
    private String updated_by;
    private Date last_updated;
}
