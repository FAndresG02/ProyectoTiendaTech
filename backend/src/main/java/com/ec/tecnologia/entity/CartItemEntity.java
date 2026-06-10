package com.ec.tecnologia.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data //construye getter y setters
@DynamicUpdate //actualiza en sql la columnas que cambiaron
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    //Muchos CartItemEntity pertenecen a un solo carrito
    //un carrito tiene varios items
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;

    //Muchos CartItemEntity pertenecen a un solo ProductEntity.
    //Un producto puede estar presente en muchos ítems de carrito.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    private Integer quantity;
}
