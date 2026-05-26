package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.product.GetProductDto;
import com.ec.tecnologia.dto.product.ProductDto;
import com.ec.tecnologia.service.ProductService;
import com.ec.tecnologia.utils.TecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(path = "/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto){

        try {

            return productService.addProduct(productDto);

        }catch (Exception e){
            log.error("Error al agregar el producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getProducts")
    public ResponseEntity<List<GetProductDto>> getProducts() {

        try {

            return productService.getProducts();

        } catch (Exception e) {

            log.error("Error al obtener los productos", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){

        try {

            return productService.updateProduct(id, productDto);

        }catch (Exception e){
            log.error("Error al actualizar el producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
