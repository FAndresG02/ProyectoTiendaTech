package com.ec.tecnologia.dto.review;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateReviewDto {

    @NotNull(message = "El id no puede ser nulo")
    @Positive(message = "El id debe ser mayor a 0")
    private Long id;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    private String comment;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer rating;

}
