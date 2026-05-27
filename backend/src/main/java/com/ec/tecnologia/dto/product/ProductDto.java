package com.ec.tecnologia.dto.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(
            min = 10,
            max = 1000,
            message = "La descripción debe tener entre 10 y 1000 caracteres"
    )
    private String description;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor a 0")
    @Digits(
            integer = 10,
            fraction = 2,
            message = "El precio solo puede tener 2 decimales"
    )
    private Double price;

    @NotNull(message = "El id no puede ser nulo")
    @Positive(message = "El id debe ser mayor a 0")
    private Long categoryId;

}
