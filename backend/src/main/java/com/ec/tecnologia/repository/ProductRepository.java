package com.ec.tecnologia.repository;

import com.ec.tecnologia.dto.product.GetProductDto;
import com.ec.tecnologia.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    //Buscar un producto por nombre
    ProductEntity findByName(String name);

    //Verficar si existe y no traer todo el producto
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query("select new com.ec.tecnologia.dto.product.GetProductDto(p.id, " +
            "p.name, " +
            "p.description, " +
            "p.price, " +
            "p.status, " +
            "p.category.id, " +
            "p.category.name)" +
            "from ProductEntity p")
    List<GetProductDto> getAllProduct();
}
