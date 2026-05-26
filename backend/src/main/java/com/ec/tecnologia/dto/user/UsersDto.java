package com.ec.tecnologia.dto.user;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class UsersDto {

    @NotNull(message = "El id no puede ser nulo")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es valido")
    private String email;

    @NotBlank(message = "El numero de contacto no puede estar vacío")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "El numero debe tener 10 dígitos"
    )
    private String contactNumber;

    @NotNull(message = "El status no puede ser nulo")
    private Boolean status;

    public UsersDto(Long id, String name, String email,
                    String contactNumber, Boolean status) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.status = status;
    }
}
