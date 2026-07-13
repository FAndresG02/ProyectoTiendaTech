package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.product.*;
import com.ec.tecnologia.service.ProductService;
import com.ec.tecnologia.utils.TecUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "product")
public class ProductController {

    @Autowired
    private ProductService productService;

    //admin
    @PostMapping(path = "/addProduct")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDto productDto){

        try {

            return productService.addProduct(productDto);

        }catch (Exception e){
            log.error("Error al agregar el producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin/user
    //Metodo para obtner la lista de productos
    @GetMapping(path = "/getProducts")
    public ResponseEntity<List<GetProductDto>> getProducts() {

        try {

            return productService.getProducts();

        } catch (Exception e) {

            log.error("Error al obtener los productos", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin/user
    //Metodo para obtener productos por categoria
    @GetMapping(path = "/getProductByCategory/{categoryId}")
    public ResponseEntity<List<GetProductByCategoryDto>> getProductByCategory(@PathVariable Long categoryId) {

        try {

            return productService.getProductByCategory(categoryId);

        }catch (Exception e){
            log.error("Error al obtener los productos por categoria", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin/user
    //Metodo para obtener un producto por su id
    @GetMapping(path = "/getProductById/{id}")
    public ResponseEntity<GetProductByIdDto> getProductById(@PathVariable Long id) {

        try {

            return productService.getProductById(id);

        }catch (Exception e){
            log.error("Error al obtener el producto", e);
            return new ResponseEntity<>(new GetProductByIdDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin
    @PutMapping(path = "/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductDto productDto){

        try {

            return productService.updateProduct(id, productDto);

        }catch (Exception e){
            log.error("Error al actualizar el producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //admin
    @PatchMapping(path = "/updatePictureProduct/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?>  updatePictureProduct(@PathVariable Long id, @RequestPart MultipartFile picture){
        try {

            return productService.updatePictureProduct(id, picture);

        }catch (Exception e){
            log.error("Error al actualizar la imágen del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin
    @PatchMapping(path = "/updateStatusProduct")
    public ResponseEntity<?> updateStatusProduct(@Valid @RequestBody UpdateStatusProductDto updateStatusProductDto){
        try {

            return productService.updateStatusProduct(updateStatusProductDto);

        }catch (Exception e){
            log.error("Error al actualizar el status del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin
    @PatchMapping(path = "/updateDiscount")
    public ResponseEntity<?> updateDiscount(@Valid @RequestBody UpdateDiscountDto updateDiscountDto){
        try {

            return productService.updateDiscount(updateDiscountDto);

        }catch (Exception e){
            log.error("Error al actualizar el descuento del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin
    @PatchMapping(path = "/updateFeatured")
    public ResponseEntity<?> updateFeatured(@Valid @RequestBody UpdateFeaturedDto updateFeaturedDto){
        try {

            return productService.updateFeatured(updateFeaturedDto);

        }catch (Exception e){
            log.error("Error al actualizar el status del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin
    //Metodo para eliminar el producto
    @DeleteMapping(path = "/deleteProduct/{id}")
    public ResponseEntity<?> deleteProduct(@Valid @PathVariable Long id){

        try {

            return productService.deleteProduct(id);

        }catch (Exception e){
            log.error("Error al eliminar el producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //admin
    @DeleteMapping("/deletePictureProduct/{id}")
    public ResponseEntity<?> deletePictureProduct(@PathVariable Long id) {
        try {

            return productService.deletePictureProduct(id);

        } catch (Exception e) {
            log.error("Error al eliminar la imagen del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
