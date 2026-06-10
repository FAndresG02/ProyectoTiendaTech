package com.ec.tecnologia.repository;

import com.ec.tecnologia.dto.review.GetReviewsDto;
import com.ec.tecnologia.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("""
    select avg(r.rating)
    from ReviewEntity r
    where r.product.id = :productId
    """)
    Double getAverageRatingByProductId(Long productId);

    @Query("""
    select count(r)
    from ReviewEntity r
    where r.product.id = :productId
    """)
    Long countReviewsByProductId(Long productId);

    @Query("""
    select new com.ec.tecnologia.dto.review.GetReviewsDto(
        r.userName,
        r.comment,
        r.rating,
        r.createdAt
    )
    from ReviewEntity r
    where r.product.id = :productId
    """)
    List<GetReviewsDto> getReviewsByProductId(
            @Param("productId") Long productId
    );

    @Query("""
    select new com.ec.tecnologia.dto.review.GetReviewsDto(
        r.userName,
        r.comment,
        r.rating,
        r.createdAt
    )
    from ReviewEntity r
    where r.user.id = :id
    """)
    List<GetReviewsDto> getAllReviewsById(@Param("id") Long id);



}
