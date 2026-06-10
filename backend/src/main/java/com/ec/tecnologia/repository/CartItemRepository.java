package com.ec.tecnologia.repository;

import com.ec.tecnologia.entity.CartEntity;
import com.ec.tecnologia.entity.CartItemEntity;
import com.ec.tecnologia.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    //Busca items
    CartItemEntity findByCartEntityAndProductEntity(
            CartEntity cartEntity,
            ProductEntity productEntity
    );

}
