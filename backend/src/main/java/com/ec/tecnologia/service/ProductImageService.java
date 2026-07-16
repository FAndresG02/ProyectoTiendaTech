package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.controller.ProductImageController;
import com.ec.tecnologia.entity.ProductEntity;
import com.ec.tecnologia.entity.ProductImageEntity;
import com.ec.tecnologia.repository.ProductImageRepository;
import com.ec.tecnologia.repository.ProductRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.TecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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


    public ResponseEntity<?> addImage(Long id, MultipartFile picture){

        try {

            if (jwtAuthenticationFilter.isAdmin()){


                ProductEntity productEntity = productRepository.findById(id).orElse(null);

                if (productEntity == null){
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
                // Construye la ruta completa donde se guardará el archivo.
                Path path = uploadDir.resolve(filename);
                // Copia el contenido del archivo subido hacia la ruta de destino.
                Files.copy(
                        // Obtiene el flujo de datos (InputStream) del archivo subido.
                        picture.getInputStream(),
                        // Especifica la ruta donde se almacenará el archivo.
                        path,
                        // Si ya existe un archivo con ese nombre, lo reemplaza.
                        StandardCopyOption.REPLACE_EXISTING
                );

                // Cuenta cuántas imágenes tiene ya el producto (para orden y saber si es la primera)
                int currentCount = productImageRepository.findByProductEntityId(id).size();

                ProductImageEntity productImageEntity = new ProductImageEntity();
                productImageEntity.setUrl("/uploads/images/" + filename); // o URL completa si sirves estático desde otro dominio
                productImageEntity.setOrderImage(currentCount + 1);
                productImageEntity.setIsPrincipal(currentCount == 0); // la primera que se sube es la principal por defecto
                productImageEntity.setProductEntity(productEntity);

                productImageRepository.save(productImageEntity);

                return TecUtils.getResponseEntity("Imagen agregada correctamente", HttpStatus.CREATED);

            }else {
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            log.error("Error al agregar la imagen del producto", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }







}
