package com.ec.tecnologia.repository;

import com.ec.tecnologia.dto.product.GetProductByCategoryDto;
import com.ec.tecnologia.dto.product.GetProductByIdDto;
import com.ec.tecnologia.dto.product.GetProductDto;
import com.ec.tecnologia.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    //Buscar un producto por nombre
    ProductEntity findByName(String name);

    //Verficar si existe y no traer todo el producto
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query("select new com.ec.tecnologia.dto.product.GetProductDto(" +
            "p.id, " +
            "p.name, " +
            "p.description, " +
            "p.price, " +
            "p.status, " +
            "p.discountPercentage, " +
            "p.featured, " +
            "p.category.id, " +
            "p.category.name, " +
            "(select pi.url from ProductImageEntity pi where pi.productEntity.id = p.id and pi.isPrincipal = true)) " +
            "from ProductEntity p " +
            "where (:categoryId is null or p.category.id = :categoryId)")
    List<GetProductDto> getProducts(@Param("categoryId") Long categoryId);

    @Query("select new com.ec.tecnologia.dto.product.GetProductDto(" +
            "p.id, " +
            "p.name, " +
            "p.description, " +
            "p.price, " +
            "p.status, " +
            "p.discountPercentage, " +
            "p.featured, " +
            "p.category.id, " +
            "p.category.name, " +
            "(select pi.url from ProductImageEntity pi where pi.productEntity.id = p.id and pi.isPrincipal = true)) " +
            "from ProductEntity p " +
            "where lower(p.name) like lower(concat('%', :name, '%'))")
    List<GetProductDto> getProductByName(@Param("name") String name);

    @Query("select new com.ec.tecnologia.dto.product.GetProductByIdDto(" +
            "p.id, p.name, p.description, p.price, p.status, " +
            "p.discountPercentage, p.featured, p.category.id, p.category.name) " +
            "from ProductEntity p " +
            "where p.id = :id")
    GetProductByIdDto getProductById(@Param("id") Long id);

    //Actualizar status
    @Transactional
    @Modifying
    @Query("update ProductEntity u set u.status = :status where u.id = :id")
    int updateStatusProduct(@Param("status") Boolean status,
                     @Param("id") Long id);

    //Actualizar descuento
    @Transactional
    @Modifying
    @Query("update ProductEntity u set u.discountPercentage = :discountPercentage where u.id = :id")
    int updateDiscountPercentage(@Param("discountPercentage") Integer discountPercentage,
                            @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update ProductEntity u set u.featured = :featured where u.id = :id")
    int updateFeatured(@Param("featured") Boolean featured,
                                 @Param("id") Long id);


}
