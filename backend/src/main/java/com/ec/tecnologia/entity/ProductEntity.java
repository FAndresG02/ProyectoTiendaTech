package com.ec.tecnologia.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data // Lombok: genera automáticamente getters, setters, toString, equals y hashCode
@Entity // JPA: indica que esta clase es una entidad que representa una tabla en la BD
@DynamicUpdate // Hibernate: solo actualiza en SQL las columnas que cambiaron
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private String picture;
    private boolean status;

    //Muchos ProductEntity pertenecen a una sola categoria (CategoryEntity)
    @ManyToOne(fetch = FetchType.LAZY)
    //category_id apunta a category.id de la entidad
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
