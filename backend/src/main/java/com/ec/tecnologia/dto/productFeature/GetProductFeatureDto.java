package com.ec.tecnologia.dto.productFeature;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetProductFeatureDto {
    private Long id;
    private String description;
}
