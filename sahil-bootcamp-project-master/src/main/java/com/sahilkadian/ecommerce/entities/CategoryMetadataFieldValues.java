package com.sahilkadian.ecommerce.entities;

import com.sahilkadian.ecommerce.entities.compositeKey.CategoryMetadataFieldValuesCK;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CategoryMetadataFieldValues {

    @EmbeddedId
    private CategoryMetadataFieldValuesCK categoryMetadataFieldValues;

    @ManyToOne
    @MapsId("categoryId")
    private Category category;

    @ManyToOne
    @MapsId("categoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;

    private String fieldValues;
}
