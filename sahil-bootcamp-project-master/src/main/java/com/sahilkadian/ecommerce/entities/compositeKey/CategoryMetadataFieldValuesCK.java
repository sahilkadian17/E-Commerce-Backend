package com.sahilkadian.ecommerce.entities.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CategoryMetadataFieldValuesCK implements Serializable{

    private Long categoryId;
    private Long categoryMetadataFieldId;
}