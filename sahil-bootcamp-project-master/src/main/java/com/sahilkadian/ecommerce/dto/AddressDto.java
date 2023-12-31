package com.sahilkadian.ecommerce.dto;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;
    private String city;
    private String state;
    private String country;
    private Long address_line;
    private Long zip_code;
    private String label;
}
