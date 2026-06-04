package com.ec.tecnologia.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetProductById {
    private Long id;
    private String name;
    private String description;
    private Double price;

    //Este se usa para extraer un producto por id
    public GetProductById(Long id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
