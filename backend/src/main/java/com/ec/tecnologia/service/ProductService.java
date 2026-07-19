package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.image.GetImageDto;
import com.ec.tecnologia.dto.product.*;
import com.ec.tecnologia.dto.productFeature.GetProductFeatureDto;
import com.ec.tecnologia.dto.review.GetReviewsDto;
import com.ec.tecnologia.entity.*;
import com.ec.tecnologia.repository.*;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.TecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private ProductImageRepository productImageRepository;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private ProductFeatureRepository productFeatureRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Value("${server.url}")
    private String serverUrl;

    //Panel de Admins
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
                        newProductEntity.setDiscountPercentage(productDto.getDiscountPercentage());
                        newProductEntity.setFeatured(false);
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

    ////Panel de Users y Admins
    //Metodo para obtener todos los productos
    //Este metodo sirve para rellenar cartas en la vista general del ecommerce como cartas de productos, Destacados
    //en oferta, y con status = true;
    public ResponseEntity<List<GetProductDto>> getProducts(){

        try {
                List<GetProductDto> getProductDtos = productRepository.getAllProduct();

                for (GetProductDto dto : getProductDtos) {
                    if (dto.getPrincipalImageUrl() != null) {
                        dto.setPrincipalImageUrl(serverUrl + dto.getPrincipalImageUrl());
                    }
                }

                return new ResponseEntity<>(getProductDtos, HttpStatus.OK);

        }catch (Exception e){
            log.error("Error al obtener los productos", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //!!!!!!!!Arreglar esto poner un foco por ahora solo
    //Panel de Admins
    //Metodo para obtener todos los productos de una categoria
    public ResponseEntity<List<GetProductByCategoryDto>> getProductByCategory(@RequestParam Long categoryId) {

        try {

            return new ResponseEntity<>(productRepository.getProductsByCategory(categoryId), HttpStatus.OK);

        }catch (Exception e){
            log.error("Error al obtener los productos por categoria", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    //Metodo para obtener un producto por su id
    //En este caso sirve para mostrar en le pnael de los users por ahora quedaria asi
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            GetProductByIdDto getProductByIdsDto = productRepository.getProductById(id);
            if (getProductByIdsDto == null) {
                return TecUtils.getResponseEntity("El producto no existe", HttpStatus.NOT_FOUND);
            }

            // Imágenes (ya lo tienes)
            List<ProductImageEntity> imagenes = productImageRepository.findByProductEntityIdOrderByOrderImageAsc(id);
            List<GetImageDto> imagesDto = imagenes.stream()
                    .map(img -> new GetImageDto(
                            img.getId(), serverUrl + img.getUrl(), img.getOrderImage(), img.getIsPrincipal()
                    ))
                    .toList();
            getProductByIdsDto.setImages(imagesDto);

            // Reviews: reutiliza los mismos 3 métodos que ya usas en getReviewsByProduct
            List<GetReviewsDto> reviews = reviewRepository.getReviewsByProductId(id);
            Double averageRating = reviewRepository.getAverageRatingByProductId(id);
            Long totalReviews = reviewRepository.countReviewsByProductId(id);

            getProductByIdsDto.setReviews(reviews);
            getProductByIdsDto.setAverageRating(averageRating);
            getProductByIdsDto.setTotalReviews(totalReviews);

            // En getProductById, agrega:
            List<ProductFeatureEntity> featureEntities = productFeatureRepository.findByProductId(id);
            List<GetProductFeatureDto> featuresDto = featureEntities.stream()
                    .map(f -> new GetProductFeatureDto(f.getId(), f.getDescription()))
                    .toList();
            getProductByIdsDto.setFeatures(featuresDto);

            return new ResponseEntity<>(getProductByIdsDto, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error al obtener el producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
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
}
