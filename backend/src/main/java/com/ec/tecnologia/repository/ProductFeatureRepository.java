package com.ec.tecnologia.repository;

import com.ec.tecnologia.entity.ProductFeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductFeatureRepository extends JpaRepository<ProductFeatureEntity, Long> {

    List<ProductFeatureEntity> findByProductId(Long productId);
}
