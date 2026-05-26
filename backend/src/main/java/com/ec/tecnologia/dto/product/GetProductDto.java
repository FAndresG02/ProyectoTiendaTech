package com.ec.tecnologia.dto.product;

import jakarta.validation.Valid;
import lombok.Data;

@Data
@Valid
public class GetProductDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private boolean status;
    private Long categoryId;
    private String categoryName;

    public GetProductDto(Long id,
                         String name,
                         String description,
                         Double price,
                         boolean status,
                         Long categoryId,
                         String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

}
