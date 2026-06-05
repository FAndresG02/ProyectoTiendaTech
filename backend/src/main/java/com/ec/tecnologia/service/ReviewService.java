package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.review.AddReviewDto;
import com.ec.tecnologia.entity.ProductEntity;
import com.ec.tecnologia.entity.ReviewEntity;
import com.ec.tecnologia.entity.UserEntity;
import com.ec.tecnologia.repository.ProductRepository;
import com.ec.tecnologia.repository.ReviewRepository;
import com.ec.tecnologia.repository.UserRepository;
import com.ec.tecnologia.security.JwtAuthenticationFilter;
import com.ec.tecnologia.security.JwtUtil;
import com.ec.tecnologia.utils.TecUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> addReview(AddReviewDto addReviewDto) {

        try {


            UserEntity userEntity = userRepository.findById(addReviewDto.getUserId()).orElse(null);
            ProductEntity productEntity = productRepository.findById(addReviewDto.getProductId()).orElse(null);

            if (userEntity == null  || productEntity == null) {
                return new ResponseEntity<>("No se puede crear la review ", HttpStatus.NOT_FOUND);
            }

            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setUser(userEntity);
            reviewEntity.setProduct(productEntity);
            reviewEntity.setComment(addReviewDto.getComment());
            reviewEntity.setRating(addReviewDto.getRating());
            reviewEntity.setCreatedAt(LocalDateTime.now());
            reviewRepository.save(reviewEntity);

            return TecUtils.getResponseEntity("Review creada correctamente",  HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error al agregar el review", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
