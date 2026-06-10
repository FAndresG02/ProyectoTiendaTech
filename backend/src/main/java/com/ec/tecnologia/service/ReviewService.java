package com.ec.tecnologia.service;

import com.ec.tecnologia.config.TecConstants;
import com.ec.tecnologia.dto.review.AddReviewDto;
import com.ec.tecnologia.dto.review.GetReviewsDto;
import com.ec.tecnologia.dto.review.GetReviewsProductIdDto;
import com.ec.tecnologia.dto.review.UpdateReviewDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            reviewEntity.setUserName(userEntity.getName());
            reviewEntity.setCreatedAt(LocalDateTime.now());
            reviewRepository.save(reviewEntity);

            return TecUtils.getResponseEntity("Review creada correctamente",  HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error al agregar el review", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //----------------------------------------------------------------------------------------------------------------

    public ResponseEntity<?> getReviewsByProduct(Long id){

        try {

            List<GetReviewsDto> reviews = reviewRepository.getReviewsByProductId(id);
            Double averageRating = reviewRepository.getAverageRatingByProductId(id);
            Long totalReviews = reviewRepository.countReviewsByProductId(id);

            return new ResponseEntity<>(new GetReviewsProductIdDto(averageRating, totalReviews, reviews), HttpStatus.OK);

        }catch (Exception e){
            log.error("Error al obtener las Reviews", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<List<GetReviewsDto>> getReviewsByUser(@PathVariable Long id){

        try {

            UserEntity userEntity = userRepository.findById(id).orElse(null);

            if (userEntity == null){
                return new ResponseEntity<>(new ArrayList<>(),  HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(reviewRepository.getAllReviewsById(id), HttpStatus.OK);

        }catch (Exception e){
            log.error("Error al obtener las Reviews", e);
            return new ResponseEntity<>(new ArrayList<>(),  HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //-------------------------------------------------------------------------------------------------------------

    public ResponseEntity<?> updateReview(UpdateReviewDto updateReviewDto){

        try {

            ReviewEntity reviewEntity = reviewRepository.findById(updateReviewDto.getId()).orElse(null);

            if (reviewEntity == null){

                return TecUtils.getResponseEntity("La review no existe", HttpStatus.NOT_FOUND);
            }

            reviewEntity.setComment(updateReviewDto.getComment());
            reviewEntity.setRating(updateReviewDto.getRating());
            reviewEntity.setCreatedAt(LocalDateTime.now());
            reviewRepository.save(reviewEntity);

            return TecUtils.getResponseEntity("La review ha sido actualizada", HttpStatus.OK);

        }catch (Exception e){
            log.error("Error al obtener las Reviews", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //-----------------------------------------------------------------------------------------------------------------

    public ResponseEntity<?> deleteReview(@PathVariable Long id){
        try {

            ReviewEntity reviewEntity = reviewRepository.findById(id).orElse(null);

            if (reviewEntity == null){
                return TecUtils.getResponseEntity("La review no existe", HttpStatus.NOT_FOUND);
            }

            reviewRepository.deleteById(id);

            return TecUtils.getResponseEntity("La review ha sido eliminada correctamente", HttpStatus.OK);

        }catch (Exception e){
            log.error("Error al eliminar la review", e);
            return TecUtils.getResponseEntity(TecConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
