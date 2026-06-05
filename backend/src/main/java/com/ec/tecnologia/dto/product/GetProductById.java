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
    private String picture;


    //Este se usa para extraer un producto por id
    public GetProductById(Long id, String name, String description, Double price , String picture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.picture = picture;
    }
}
