package com.ec.tecnologia.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Data //construye getter y setters
@DynamicUpdate //actualiza en sql la columnas que cambiaron
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String comment;

    private Integer rating;

    private LocalDateTime createdAt;

    //Muchos ReviewEntity pertenecen a una sola UserEntity
    //un usuario puede tener varios reviews
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    //Muchos ReviewEntity pertenecen a un ProductEntity
    //un producto puede tener muchos reviews
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
