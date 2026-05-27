package com.ec.tecnologia.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserDto {

    @NotNull(message = "El id no puede ser nulo")
    @Positive(message = "El id debe ser mayor a 0")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 3, max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @NotBlank(message = "El numero de contacto no puede estar vacio")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "El numero debe tener 10 digitos"
    )
    private String contactNumber;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es valido")
    private String email;


}
