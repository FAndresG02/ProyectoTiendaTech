package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.review.AddReviewDto;
import com.ec.tecnologia.dto.review.GetReviewsDto;
import com.ec.tecnologia.dto.review.UpdateReviewDto;
import com.ec.tecnologia.service.ReviewService;
import com.ec.tecnologia.utils.TecUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping(path = "/addReview")
    public ResponseEntity<?> addReview(@Valid @RequestBody AddReviewDto addReviewDto) {

        try {

            return reviewService.addReview(addReviewDto);

        }catch (Exception e){
            log.error("Error al agregar el review", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getReviewsByProduct/{id}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable Long id){

        try {

            return reviewService.getReviewsByProduct(id);

        }catch (Exception e){
            log.error("Error al obtener las Reviews", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path = "/getReviewsByUser/{id}")
    public ResponseEntity<List<GetReviewsDto>> getReviewsByUser(@PathVariable Long id){

        try {

            return reviewService.getReviewsByUser(id);

        }catch (Exception e){
            log.error("Error al obtener las Reviews", e);
            return new ResponseEntity<>(new ArrayList<>(),  HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping(path = "/updateReview")
    public ResponseEntity<?> updateReview(@Valid @RequestBody UpdateReviewDto updateReviewDto){

        try {

            return reviewService.updateReview(updateReviewDto);

        }catch (Exception e){
            log.error("Error al obtener las Reviews", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(path = "/deleteReview/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id){
        try {

            return reviewService.deleteReview(id);

        }catch (Exception e){
            log.error("Error al eliminar la review", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
