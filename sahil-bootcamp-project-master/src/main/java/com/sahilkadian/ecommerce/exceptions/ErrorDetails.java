package com.sahilkadian.ecommerce.exceptions;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ErrorDetails {

    private Integer statusCode;
    private List<String> message;
    private Date timeStamp;
}
