package com.ec.tecnologia.controller;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.review.AddReviewDto;
import com.ec.tecnologia.service.ReviewService;
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
}
