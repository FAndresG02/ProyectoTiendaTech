package com.ec.tecnologia.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRoleDto {

    @NotNull(message = "El id no puede ser nulo")
    @Positive(message = "El id debe ser mayor a 0")
    private Long id;

    @NotNull(message = "El rol no puede ser nulo")
    @Size(min = 2, max = 50, message = "El rol debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)$",
            message = "El rol debe ser: ROLE_ADMIN o ROLE_USER")
    private String role;
}
