package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.productFeature.AddProductFeatureDto;
import com.ec.tecnologia.dto.productFeature.UpdateProductFeatureDto;
import com.ec.tecnologia.entity.ProductEntity;
import com.ec.tecnologia.entity.ProductFeatureEntity;
import com.ec.tecnologia.repository.ProductFeatureRepository;
import com.ec.tecnologia.repository.ProductRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.TecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductFeatureService {

    @Autowired
    private ProductFeatureRepository productFeatureRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // POST: agregar una característica
    public ResponseEntity<?> addFeature(AddProductFeatureDto dto) {
        if (!jwtAuthenticationFilter.isAdmin()) {
            return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }

        ProductEntity product = productRepository.findById(dto.getProductId()).orElse(null);
        if (product == null) {
            return TecUtils.getResponseEntity("El producto no existe", HttpStatus.NOT_FOUND);
        }

        ProductFeatureEntity feature = new ProductFeatureEntity();
        feature.setDescription(dto.getDescription());
        feature.setProduct(product);
        productFeatureRepository.save(feature);

        return new ResponseEntity<>("Característica agregada correctamente", HttpStatus.CREATED);
    }

    // PATCH: actualizar el texto de una característica
    public ResponseEntity<?> updateFeature(Long featureId, UpdateProductFeatureDto dto) {
        if (!jwtAuthenticationFilter.isAdmin()) {
            return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }

        ProductFeatureEntity feature = productFeatureRepository.findById(featureId).orElse(null);
        if (feature == null) {
            return TecUtils.getResponseEntity("La característica no existe", HttpStatus.NOT_FOUND);
        }

        feature.setDescription(dto.getDescription());
        productFeatureRepository.save(feature);

        return new ResponseEntity<>("Característica actualizada correctamente", HttpStatus.OK);
    }

    // DELETE: eliminar una característica
    public ResponseEntity<?> deleteFeature(Long featureId) {
        if (!jwtAuthenticationFilter.isAdmin()) {
            return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }

        ProductFeatureEntity feature = productFeatureRepository.findById(featureId).orElse(null);
        if (feature == null) {
            return TecUtils.getResponseEntity("La característica no existe", HttpStatus.NOT_FOUND);
        }

        productFeatureRepository.delete(feature);

        return new ResponseEntity<>("Característica eliminada correctamente", HttpStatus.OK);
    }
}
