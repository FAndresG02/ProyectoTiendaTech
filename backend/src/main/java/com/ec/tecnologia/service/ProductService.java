package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.product.GetProductDto;
import com.ec.tecnologia.dto.product.ProductDto;
import com.ec.tecnologia.entity.CategoryEntity;
import com.ec.tecnologia.entity.ProductEntity;
import com.ec.tecnologia.repository.CategoryRepository;
import com.ec.tecnologia.repository.ProductRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.TecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //Metodo para agregar un producto
    public ResponseEntity<?> addProduct( ProductDto productDto){

        try {

            if (jwtAuthenticationFilter.isAdmin()){

                boolean exists = productRepository.existsByNameIgnoreCase(productDto.getName());

                if (exists){

                    return new ResponseEntity<>("El producto ya existe en el sistema", HttpStatus.BAD_REQUEST);

                }else{

                    CategoryEntity categoryEntity = getCategory(productDto);

                    if (categoryEntity == null){
                        return new ResponseEntity<>("La Categoria no existe", HttpStatus.BAD_REQUEST);
                    }else{
                        ProductEntity newProductEntity = new ProductEntity();
                        newProductEntity.setName(productDto.getName());
                        newProductEntity.setDescription(productDto.getDescription());
                        newProductEntity.setPrice(productDto.getPrice());
                        newProductEntity.setStatus(true);
                        newProductEntity.setCategory(categoryEntity);

                        productRepository.save(newProductEntity);

                        return new ResponseEntity<>("Producto agregado correctamente", HttpStatus.CREATED);
                    }
                }
            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al agregar el producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CategoryEntity getCategory(ProductDto productDto){

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(productDto.getCategoryId());

        if (categoryEntity.isPresent()){
            return categoryEntity.get();
        }else{
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------------------------

    //Metodo para obtener todos los productos
    public ResponseEntity<List<GetProductDto>> getProducts(){

        try {

            if (jwtAuthenticationFilter.isAdmin()){

                return new ResponseEntity<>(productRepository.getAllProduct(), HttpStatus.OK);

            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al obtener los productos", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    //Metodo actualizar producto
    public ResponseEntity<?> updateProduct(Long id, ProductDto productDto){

        try {

           if (jwtAuthenticationFilter.isAdmin()){

               ProductEntity productEntity = validateProduct(id, productDto);

               if (productEntity == null){
                   return TecUtils.getResponseEntity("No existen cambios por hacerse",
                           HttpStatus.BAD_REQUEST);
               }else{

                   CategoryEntity categoryEntity = getCategory(productDto);

                   if (categoryEntity == null){

                       return new ResponseEntity<>("La Categoria no existe", HttpStatus.BAD_REQUEST);

                   }else{

                       productEntity.setName(productDto.getName());
                       productEntity.setDescription(productDto.getDescription());
                       productEntity.setPrice(productDto.getPrice());
                       productEntity.setCategory(categoryEntity);
                       productRepository.save(productEntity);

                       return TecUtils.getResponseEntity("Producto actualizado correctamente", HttpStatus.OK);
                   }

               }

           }else {
               return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
           }

        }catch (RuntimeException e){

            log.error("Error de validación al actualizar producto", e);
            return TecUtils.getResponseEntity(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e){

            log.error("Error al actualizar el producto", e);
            return TecUtils.getResponseEntity(
                    TecConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ProductEntity validateProduct(Long id, ProductDto productDto){

        ProductEntity productEntity = productRepository.findById(id)
                .orElse(null);

        // no existe
        if (productEntity == null){
            throw new RuntimeException("Producto no existe");
        }

        boolean noChanges =
                productEntity.getName().equals(productDto.getName()) &&
                        productEntity.getDescription().equals(productDto.getDescription()) &&
                        productEntity.getPrice().equals(productDto.getPrice()) &&
                        productEntity.getCategory().getId().equals(productDto.getCategoryId());

        // no hay cambios
        if (noChanges){
            return null;
        }

        return productEntity;
    }

    //----------------------------------------------------------------------------------------------------------------

    //Metodo para eliminar el producto
    public ResponseEntity<?> deleteProduct(Long id){

        try {

            if (jwtAuthenticationFilter.isAdmin()){

                Optional<ProductEntity> productEntity = productRepository.findById(id);

                if (productEntity.isPresent()){

                    productRepository.deleteById(id);
                    return TecUtils.getResponseEntity("Producto eliminado correctamente", HttpStatus.OK);
                }else {
                    return TecUtils.getResponseEntity("Producto no existe", HttpStatus.NOT_FOUND);
                }
            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al eliminar el producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
