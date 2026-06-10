package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.cart.AddCartItemDto;
import com.ec.tecnologia.service.CartService;
import com.ec.tecnologia.utils.TecUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController //manejara peticiones HTTP
@RequestMapping(path = "/cart") //define la ruta de las peticiones
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping(path = "/addCart")
    public ResponseEntity<?> addCart(@Valid @RequestBody AddCartItemDto addCartItemDto){

        try {

            return cartService.addCart(addCartItemDto);

        }catch (Exception e){
            log.error("Error al agregar el review", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
