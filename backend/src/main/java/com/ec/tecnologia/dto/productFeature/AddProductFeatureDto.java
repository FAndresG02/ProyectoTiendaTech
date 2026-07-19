package com.ec.tecnologia.dto.productFeature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddProductFeatureDto {
    @NotNull(message = "El id del producto no puede ser nulo")
    @Positive(message = "El id debe ser mayor a 0")
    private Long productId;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;
}
