package com.ec.tecnologia.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "El email no es valido")
    private String email;

    @Size(min = 4, message = "Mínimo 4 caracteres")
    private String password;
}
