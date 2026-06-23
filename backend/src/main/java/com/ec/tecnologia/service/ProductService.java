package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.product.*;
import com.ec.tecnologia.entity.CategoryEntity;
import com.ec.tecnologia.entity.ProductEntity;
import com.ec.tecnologia.repository.CategoryRepository;
import com.ec.tecnologia.repository.ProductRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.TecUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${server.url}")
    private String serverUrl;

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
                        newProductEntity.setPicture("defaultImage.png");
                        newProductEntity.setDiscountPercentage(productDto.getDiscountPercentage());
                        newProductEntity.setFeatured(false);
                        newProductEntity.setCreatedAt(LocalDateTime.now());
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

                List<GetProductDto> getProductDtos = productRepository.getAllProduct();

                for(GetProductDto getProductDto : getProductDtos){

                    getProductDto.setPicture(serverUrl + "/images/" + getProductDto.getPicture());
                }

                return new ResponseEntity<>(getProductDtos, HttpStatus.OK);


        }catch (Exception e){
            log.error("Error al obtener los productos", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Metodo para obtener todos los productos de una categoria
    public ResponseEntity<List<GetProductByCategory>> getProductByCategory(@RequestParam Long categoryId) {

        try {

            return new ResponseEntity<>(productRepository.getProductsByCategory(categoryId), HttpStatus.OK);

        }catch (Exception e){
            log.error("Error al obtener los productos por categoria", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Metodo para obtener un producto por su id
    public ResponseEntity<GetProductById> getProductById(@PathVariable Long id) {

        try {

            GetProductById getProductByIds = productRepository.getProductById(id);

            getProductByIds.setPicture(serverUrl + "/images/" + getProductByIds.getPicture());

            return new ResponseEntity<>(getProductByIds, HttpStatus.OK);


        }catch (Exception e){
            log.error("Error al obtener el producto", e);
            return new ResponseEntity<>(new GetProductById(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    //Metodo para actualizar la imagen del producto
    public ResponseEntity<?> updatePictureProduct(Long id, MultipartFile picture) {
        try {
            if (jwtAuthenticationFilter.isAdmin()) {

                ProductEntity product = productRepository.findById(id).orElse(null);

                if (product == null) {
                    return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
                }

                Path uploadDir = Paths.get("uploads/images");
                Files.createDirectories(uploadDir);

                String filename = UUID.randomUUID() + "_" + picture.getOriginalFilename();
                Path path = uploadDir.resolve(filename);

                Files.copy(
                        picture.getInputStream(),
                        path,
                        StandardCopyOption.REPLACE_EXISTING
                );

                product.setPicture(filename);
                productRepository.save(product);

                return new ResponseEntity<>("Imagen actualizada correctamente", HttpStatus.OK);

            } else {
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Error al actualizar la imagen del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Metodo para actualizar el status del product
    public ResponseEntity<?> updateStatusProduct(UpdateStatusProductDto updateStatusProductDto){
        try {

            if (jwtAuthenticationFilter.isAdmin()) {

                ProductEntity product = productRepository.findById(updateStatusProductDto.getId()).orElse(null);

                if (product == null) {
                    return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
                }

                productRepository.updateStatusProduct(updateStatusProductDto.getStatus(),
                        updateStatusProductDto.getId());

                return TecUtils.getResponseEntity("Status del producto actualizado correctamente",
                        HttpStatus.OK);

            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al actualizar el status del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateDiscount(UpdateDiscountDto updateDiscountDto){
        try {

            if (jwtAuthenticationFilter.isAdmin()){

                ProductEntity productEntity = productRepository.findById(updateDiscountDto.getId()).orElse(null);

                if (productEntity == null){
                    return TecUtils.getResponseEntity("El producto no existe", HttpStatus.NOT_FOUND);
                }

                productRepository.updateDiscountPercentage(updateDiscountDto.getDiscountPercentage(),
                        updateDiscountDto.getId());

                return TecUtils.getResponseEntity("Descuento del producto actualizado correctamente",
                        HttpStatus.OK);

            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al actualizar el descuento del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateFeatured(UpdateFeaturedDto updateFeaturedDto){
        try {

            if (jwtAuthenticationFilter.isAdmin()){

                ProductEntity productEntity = productRepository.findById(updateFeaturedDto.getId()).orElse(null);

                if (productEntity == null){
                    return TecUtils.getResponseEntity("El producto no existe", HttpStatus.NOT_FOUND);
                }

                productRepository.updateFeatured(updateFeaturedDto.getFeatured(), updateFeaturedDto.getId());

                return TecUtils.getResponseEntity("Producto destacado actualizado correctamente",
                        HttpStatus.OK);
            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al actualizar el status del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Metodo para validar el producto
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

    //Metodo para eliminar un imagen
    public ResponseEntity<?> deletePictureProduct(@PathVariable Long id) {
        try {

            if (jwtAuthenticationFilter.isAdmin()){

                //Buscamos el producto por id
                ProductEntity productEntity = productRepository.findById(id).orElse(null);

                //Si el producto no existe devuelve not found
                if (productEntity == null){
                    return new ResponseEntity<>("Producto no existe", HttpStatus.NOT_FOUND);
                }

                //Obtiene la imagen del producto
                String picture = productEntity.getPicture();

                //si la imagen no es nula o que no este vacio o que no tenga solo espacios
                if(picture != null && !picture.isBlank()){
                    //crea la url donde se encuentra la imgen para borrar por ejm uploads/images/(productEntity image)
                    Path imagePath = Paths.get("uploads/images/" + picture);
                    //Elimina la imagen en esa ruta
                    Files.deleteIfExists(imagePath);
                }

                //Despues de eliminar setea la imagen por defecto
                productEntity.setPicture("defaultImage.png");
                //Guarda el productEntity
                productRepository.save(productEntity);

                return TecUtils.getResponseEntity("Imagen eliminado correctamente", HttpStatus.OK);
            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("Error al eliminar la imagen del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
