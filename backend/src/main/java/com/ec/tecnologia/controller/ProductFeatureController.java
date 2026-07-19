package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.productFeature.AddProductFeatureDto;
import com.ec.tecnologia.dto.productFeature.UpdateProductFeatureDto;
import com.ec.tecnologia.service.ProductFeatureService;
import com.ec.tecnologia.utils.TecUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feature")
@Slf4j
public class ProductFeatureController {

    @Autowired
    private ProductFeatureService productFeatureService;

    @PostMapping("/addFeature")
    public ResponseEntity<?> addFeature(@Valid @RequestBody AddProductFeatureDto dto) {
        try {
            return productFeatureService.addFeature(dto);
        } catch (Exception e) {
            log.error("Error al agregar la característica", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/updateFeature/{id}")
    public ResponseEntity<?> updateFeature(@PathVariable Long id, @Valid @RequestBody UpdateProductFeatureDto dto) {
        try {
            return productFeatureService.updateFeature(id, dto);
        } catch (Exception e) {
            log.error("Error al actualizar la característica", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteFeature/{id}")
    public ResponseEntity<?> deleteFeature(@PathVariable Long id) {
        try {
            return productFeatureService.deleteFeature(id);
        } catch (Exception e) {
            log.error("Error al eliminar la característica", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}