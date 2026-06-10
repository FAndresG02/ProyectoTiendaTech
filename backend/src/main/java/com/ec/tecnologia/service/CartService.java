package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.cart.AddCartItemDto;
import com.ec.tecnologia.entity.CartEntity;
import com.ec.tecnologia.entity.CartItemEntity;
import com.ec.tecnologia.entity.ProductEntity;
import com.ec.tecnologia.entity.UserEntity;
import com.ec.tecnologia.repository.CartItemRepository;
import com.ec.tecnologia.repository.CartRepository;
import com.ec.tecnologia.repository.ProductRepository;
import com.ec.tecnologia.repository.UserRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.TecUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> addCart(AddCartItemDto addCartItemDto){

        try {

            //Buscamos el usuario quien es duenio del carrito
            UserEntity userEntity = userRepository.findById(addCartItemDto.getUserId()).orElse(null);

            if (userEntity == null){
                return TecUtils.getResponseEntity("El usuario no Existe", HttpStatus.NOT_FOUND);
            }

            //Buscar el carrito si pertenece a un usuario
            CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);

            //si no existe un carrtio relacionado a un usuario creamos uno nuevo
            if (cartEntity == null){

                cartEntity = new CartEntity();

                cartEntity.setUserEntity(userEntity);

                cartEntity = cartRepository.save(cartEntity);
            }

            //Se busca el producto para ver si existe en la bd
            ProductEntity productEntity = productRepository.findById(addCartItemDto.getProductId()).orElse(null);

            //Si no existe no agrega nada
            if (productEntity == null){
                return TecUtils.getResponseEntity("El producto no Existe", HttpStatus.NOT_FOUND);
            }

            //Verificamos si en el carrito existe productos en la bd
            CartItemEntity cartItemEntity =
                    cartItemRepository.findByCartEntityAndProductEntity(
                            cartEntity,
                            productEntity
                    );

            //Si existen productos en el carrito
            if (cartItemEntity != null){
                //Actualizamos la cantidad de productos que existen
                cartItemEntity.setQuantity(cartItemEntity.getQuantity() + addCartItemDto.getQuantity());
                //Guardamos en la bd
                cartItemRepository.save(cartItemEntity);
                return TecUtils.getResponseEntity("Carrito Actualizado", HttpStatus.OK);
            }else{
                //si no existe creamos una neuva entidad de CartItemEntity
                CartItemEntity newItem = new CartItemEntity();

                //le asignamos u nuevo cartEntity y le agregamos el producto
                newItem.setCartEntity(cartEntity);
                newItem.setProductEntity(productEntity);
                //le asignamos la cantidad
                newItem.setQuantity(addCartItemDto.getQuantity());
                cartItemRepository.save(newItem);
                return TecUtils.getResponseEntity("Carrito Actualizado", HttpStatus.OK);
            }


        }catch (Exception e){
            log.error("Error al agregar el review", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
