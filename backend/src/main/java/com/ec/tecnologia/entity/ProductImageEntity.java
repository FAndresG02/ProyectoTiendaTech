package com.ec.tecnologia.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data // Lombok: genera automáticamente getters, setters, toString, equals y hashCode
@Entity // JPA: indica que esta clase es una entidad que representa una tabla en la BD
@DynamicUpdate // Hibernate: solo actualiza en SQL las columnas que cambiaron
public class ProductImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private Integer order;
    private Boolean isPrincipal;

    //Muchos ProductImage pertencen a un solo ProductEntity
    @ManyToOne(fetch = FetchType.LAZY)
    //product_id apunta al id del ProductEntity
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;
}
