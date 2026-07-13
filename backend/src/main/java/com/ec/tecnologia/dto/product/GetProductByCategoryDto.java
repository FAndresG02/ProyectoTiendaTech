package com.ec.tecnologia.dto.product;

import lombok.Data;

@Data
public class GetProductByCategoryDto {
    private Long id;
    private String name;

    //Este se usa para extraer un producto por id
    public GetProductByCategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
