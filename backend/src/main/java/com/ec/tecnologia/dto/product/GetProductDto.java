package com.ec.tecnologia.dto.product;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetProductDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean status;
    private Integer discountPercentage;
    private Boolean featured;
    private Long categoryId;
    private String categoryName;
    private String principalImageUrl;

    //Este se usa para extraer todos los productos
    public GetProductDto(Long id,
                         String name,
                         String description,
                         Double price,
                         Boolean status,
                         Integer discountPercentage,
                         Boolean featured,
                         Long categoryId,
                         String categoryName,
                         String principalImageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.discountPercentage = discountPercentage;
        this.featured = featured;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.principalImageUrl = principalImageUrl;
    }

}
