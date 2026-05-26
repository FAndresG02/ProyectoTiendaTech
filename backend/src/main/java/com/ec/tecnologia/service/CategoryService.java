package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.entity.CategoryEntity;
import com.ec.tecnologia.repository.CategoryRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.utils.TecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    public ResponseEntity<?> addCategory(CategoryEntity categoryEntity){
        try {
            if (jwtAuthenticationFilter.isAdmin()){
                // Validación
                if (categoryEntity.getName() == null || categoryEntity.getName().trim().isEmpty()) {
                    return TecUtils.getResponseEntity("El nombre es requerido", HttpStatus.BAD_REQUEST);
                }
                categoryRepository.save(categoryEntity);
                return TecUtils.getResponseEntity("Categoria agregada correctamente", HttpStatus.CREATED);
            } else {
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //---------------------------------------------------------------------------------------------------------------

    //Metodo para obtner todas las categorias
    public ResponseEntity<List<CategoryEntity>> getCategories(){

        try {

            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //----------------------------------------------------------------------------------------------------------------

    //Metodo para actualizar una categoria
    public ResponseEntity<?> updateCategory(CategoryEntity categoryEntity){

        try {

            if (jwtAuthenticationFilter.isAdmin()){

                Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryEntity.getId());

                if(categoryEntityOptional.isPresent()){

                    categoryRepository.updateCategory(categoryEntity.getName(), categoryEntity.getId());

                    return TecUtils.getResponseEntity("Categoria actualizada correctamente",
                            HttpStatus.OK);
                }else {
                    return TecUtils.getResponseEntity("Categoria no actualizada",
                            HttpStatus.OK);
                }

            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //----------------------------------------------------------------------------------------------------------------

    //Metodo para eliminar unca categoria

    public ResponseEntity<?> deleteCategory(Long id){
        try {

            if (jwtAuthenticationFilter.isAdmin()){

                Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);

                if(categoryEntityOptional.isPresent()){

                    categoryRepository.deleteById(id);
                    return  TecUtils.getResponseEntity("Categoria eliminada correctamente", HttpStatus.OK);

                }else{
                    return TecUtils.getResponseEntity("La categoria no existe", HttpStatus.NOT_FOUND);
                }
            }else{
                return TecUtils.getResponseEntity(TecConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
