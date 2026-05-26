package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.entity.CategoryEntity;
import com.ec.tecnologia.service.CategoryService;
import com.ec.tecnologia.utils.TecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController //manejara peticiones HTTP
@RequestMapping(path = "/category") //define la ruta de las peticiones
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(path = "/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody CategoryEntity categoryEntity){

        try {

            return categoryService.addCategory(categoryEntity);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/getCategories")
    public ResponseEntity<List<CategoryEntity>> getCategories(){

        try {

            return categoryService.getCategories();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "updateCategory")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryEntity categoryEntity){

        try {

            return categoryService.updateCategory(categoryEntity);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        try {

            return categoryService.deleteCategory(id);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }






}
