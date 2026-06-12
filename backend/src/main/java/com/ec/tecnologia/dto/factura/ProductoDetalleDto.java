package com.ec.tecnologia.dto.factura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDetalleDto {

    @NotBlank(message = "El nombre del producto es requerido")
    private String name;

    @NotBlank(message = "La categoría es requerida")
    private String category;

    @NotBlank(message = "La cantidad es requerida")
    private String quantity;

    @NotNull(message = "El precio es requerido")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    @NotNull(message = "El total es requerido")
    @Positive(message = "El total debe ser mayor a 0")
    private Double total;
}
