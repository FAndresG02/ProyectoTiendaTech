package com.ec.tecnologia.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserChangePasswordDto {

    @Size(min = 4, message = "Mínimo 4 caracteres")
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String oldPassword;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 4, message = "Mínimo 4 caracteres")
    private String newPassword;

    @NotBlank(message = "Necesita confirmar la nueva contraseña")
    @Size(min = 4, message = "Mínimo 4 caracteres")
    private String confirmNewPassword;

}
