package com.ec.tecnologia.repository;

import com.ec.tecnologia.dto.product.GetProductByCategory;
import com.ec.tecnologia.dto.product.GetProductById;
import com.ec.tecnologia.dto.product.GetProductDto;
import com.ec.tecnologia.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    //Selecciona todos los productos que pertenezcan a una categoría específica y estén activos status='true'
    @Query("select new com.ec.tecnologia.dto.product.GetProductByCategory(" +
            "p.id, " +
            "p.name) " +
            "from ProductEntity p " +
            "where p.category.id = :categoryId and p.status = true")
    List<GetProductByCategory> getProductsByCategory(@Param("categoryId") Long categoryId);


    @Query("select new com.ec.tecnologia.dto.product.GetProductById(" +
            "p.id, " +
            "p.name, " +
            "p.description, " +
            "p.price) " +
            "from ProductEntity p " +
            "where p.id= :id")
    GetProductById getProductById(@Param("id") Long id);


}
