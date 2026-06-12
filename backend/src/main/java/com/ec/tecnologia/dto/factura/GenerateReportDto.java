package com.ec.tecnologia.dto.factura;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateReportDto {

    // Campos obligatorios (los del validateRequestMap)
    @NotBlank(message = "El nombre es requerido")
    private String name;

    @NotBlank(message = "El email es requerido")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "El número de contacto es requerido")
    private String contactNumber;

    @NotBlank(message = "El método de pago es requerido")
    private String paymentMethod;

    @NotEmpty(message = "Los detalles del producto son requeridos")
    @Valid
    private List<ProductoDetalleDto> productDetails;  // JSON como String

    @NotNull(message = "El total es requerido")
    private Double totalAmount;

    // Campos opcionales para el manejo del PDF
    private Boolean isGenerate;
    private String uuid;
}
