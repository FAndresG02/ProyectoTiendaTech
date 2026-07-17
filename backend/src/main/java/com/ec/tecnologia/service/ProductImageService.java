package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.entity.ProductEntity;
import com.ec.tecnologia.entity.ProductImageEntity;
import com.ec.tecnologia.repository.ProductImageRepository;
import com.ec.tecnologia.repository.ProductRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.TecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ProductImageService {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    ProductImageRepository productImageRepository;

    @Autowired
    ProductRepository productRepository;

    @Value("${server.url}")
    private String serverUrl;


    public ResponseEntity<?> addImage(Long id, MultipartFile picture) {

        try {

            if (jwtAuthenticationFilter.isAdmin()) {

                ProductEntity productEntity = productRepository.findById(id).orElse(null);

                if (productEntity == null) {
                    return TecUtils.getResponseEntity("El producto no existe", HttpStatus.NOT_FOUND);
                }
                // Validación básica: que sea una imagen
                if (picture.isEmpty() || picture.getContentType() == null || !picture.getContentType().startsWith("image/")) {
                    return TecUtils.getResponseEntity("El archivo debe ser una imagen válida", HttpStatus.BAD_REQUEST);
                }

                // Define la ruta donde se guardarán las imágenes.
                Path uploadDir = Paths.get("uploads/images");
                // Crea la carpeta si no existe (y también las carpetas padre si hacen falta).
                Files.createDirectories(uploadDir);
                // Genera un nombre único para evitar que dos archivos tengan el mismo nombre.
                String filename = UUID.randomUUID() + "_" + picture.getOriginalFilename();
                Path path = uploadDir.resolve(filename);
                Files.copy(
                        picture.getInputStream(),
                        path,
                        StandardCopyOption.REPLACE_EXISTING
                );

                int currentCount = productImageRepository.findByProductEntityId(id).size();

                boolean yaExistePrincipal = productImageRepository.existsByProductEntityIdAndIsPrincipalTrue(id);

                ProductImageEntity productImageEntity = new ProductImageEntity();
                String imageUrl = "/uploads/images/" + filename;
                productImageEntity.setUrl(imageUrl);
                productImageEntity.setOrderImage(currentCount + 1);
                productImageEntity.setIsPrincipal(!yaExistePrincipal);
                productImageEntity.setProductEntity(productEntity);

                productImageRepository.save(productImageEntity);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Imagen agregada correctamente");
                response.put("url", serverUrl + imageUrl);

                return new ResponseEntity<>(response, HttpStatus.CREATED);

            } else {
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("Error al agregar la imagen del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Map<String, Object>>> getImagesByProduct(Long productId) {

        try {

            if (jwtAuthenticationFilter.isAdmin()) {

                List<ProductImageEntity> images = productImageRepository.findByProductEntityId(productId);
                List<Map<String, Object>> result = new ArrayList<>();

                for (ProductImageEntity img : images) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", img.getId());
                    item.put("url", serverUrl + img.getUrl());
                    item.put("orderImage", img.getOrderImage());
                    item.put("isPrincipal", img.getIsPrincipal());
                    result.add(item);
                }

                return new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("Error al obtener las imagenes del producto", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteImage(Long imageId) {

        try {

            if (jwtAuthenticationFilter.isAdmin()) {

                ProductImageEntity image = productImageRepository.findById(imageId).orElse(null);

                if (image == null) {
                    return TecUtils.getResponseEntity("La imagen no existe", HttpStatus.NOT_FOUND);
                }

                boolean eraPrincipal = image.getIsPrincipal();
                Long productId = image.getProductEntity().getId();

                Path path = Paths.get(image.getUrl().substring(1));
                Files.deleteIfExists(path);

                productImageRepository.delete(image);

                if (eraPrincipal) {
                    List<ProductImageEntity> restantes = productImageRepository.findByProductEntityIdOrderByOrderImageAsc(productId);
                    if (!restantes.isEmpty()) {
                        ProductImageEntity nuevaPrincipal = restantes.get(0);
                        nuevaPrincipal.setIsPrincipal(true);
                        productImageRepository.save(nuevaPrincipal);
                    }
                }

                return TecUtils.getResponseEntity("Imagen eliminada correctamente", HttpStatus.OK);

            } else {
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("Error al eliminar la imagen", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> setPrincipalImage(Long imageId) {

        try {

            if (jwtAuthenticationFilter.isAdmin()) {

                ProductImageEntity newPrincipalImage = productImageRepository.findById(imageId).orElse(null);

                if (newPrincipalImage == null) {
                    return TecUtils.getResponseEntity("La imagen no existe", HttpStatus.NOT_FOUND);
                }

                Long productId = newPrincipalImage.getProductEntity().getId();

                List<ProductImageEntity> currentPrincipalImages = productImageRepository.findByProductEntityIdAndIsPrincipalTrue(productId);

                for (ProductImageEntity img : currentPrincipalImages) {
                    img.setIsPrincipal(false);
                    productImageRepository.save(img);
                }

                newPrincipalImage.setIsPrincipal(true);
                productImageRepository.save(newPrincipalImage);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Imagen principal actualizada correctamente");
                response.put("url", serverUrl + newPrincipalImage.getUrl());

                return new ResponseEntity<>(response, HttpStatus.OK);

            } else {
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            log.error("Error al actualizar la imagen principal", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

