package com.ec.tecnologia.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupDto {

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

    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "El email no es valido")
    private String email;

    @Size(min = 4, message = "Mínimo 4 caracteres")
    private String password;
}
