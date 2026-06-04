package com.ec.tecnologia.dto.product;

import lombok.Data;

@Data
public class GetProductByCategory {
    private Long id;
    private String name;

    //Este se usa para extraer un producto por id
    public GetProductByCategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
