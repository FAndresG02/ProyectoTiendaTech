package com.ec.tecnologia.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateStatusProductDto {

    @NotNull(message = "El id no puede ser nulo")
    @Positive(message = "El id debe ser mayor a 0")
    private Long id;

    @NotNull(message = "El status no puede ser nulo")
    private Boolean status;
}
