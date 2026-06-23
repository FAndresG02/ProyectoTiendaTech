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
    private String picture;
    private Boolean status;
    private Integer discountPercentage;
    private Boolean featured;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String categoryName;

    //Este se usa para extraer todos los productos
    public GetProductDto(Long id,
                         String name,
                         String description,
                         Double price,
                         String picture,
                         Boolean status,
                         Integer discountPercentage,
                         Boolean featured,
                         LocalDateTime createdAt,
                         Long categoryId,
                         String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.status = status;
        this.discountPercentage = discountPercentage;
        this.featured = featured;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

}
