package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.service.ProductImageService;
import com.ec.tecnologia.utils.TecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/image")
public class ProductImageController {

    @Autowired
    ProductImageService productImageService;

    @PostMapping(path = "/addImage/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addImage(@PathVariable Long id, @RequestPart MultipartFile picture){
        try {
            return productImageService.addImage(id, picture);
        }catch (Exception e){
            log.error("Error al agregar la imagen del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path = "/setPrincipal/{imageId}")
    public ResponseEntity<?> setPrincipalImage(@PathVariable Long imageId){
        try {
            return productImageService.setPrincipalImage(imageId);
        }catch (Exception e){
            log.error("Error al actualizar la imagen principal", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/byProduct/{productId}")
    public ResponseEntity<List<Map<String, Object>>> getImagesByProduct(@PathVariable Long productId){
        try {
            return productImageService.getImagesByProduct(productId);
        }catch (Exception e){
            log.error("Error al obtener las imagenes del producto", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/deleteImage/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId){
        try {
            return productImageService.deleteImage(imageId);
        }catch (Exception e){
            log.error("Error al eliminar la imagen", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
