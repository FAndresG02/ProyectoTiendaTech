package com.ec.tecnologia.repository;


import com.ec.tecnologia.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {

    List<ProductImageEntity> findByProductEntityId(Long productId);

    boolean existsByProductEntityIdAndIsPrincipalTrue(Long productId);

    List<ProductImageEntity> findByProductEntityIdAndIsPrincipalTrue(Long productId);

    List<ProductImageEntity> findByProductEntityIdOrderByOrderImageAsc(Long productId);

}
