package com.ec.tecnologia.dto.product;

import com.ec.tecnologia.dto.image.GetImageDto;
import com.ec.tecnologia.dto.productFeature.GetProductFeatureDto;
import com.ec.tecnologia.dto.review.GetReviewsDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetProductByIdDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean status;
    private Integer discountPercentage;
    private Boolean featured;
    private Long categoryId;
    private String categoryName;
    // se llena después, no en el constructor JPQL
    private List<GetImageDto> images;
    private List<GetReviewsDto> reviews;
    private Double averageRating;
    private Long totalReviews;
    private List<GetProductFeatureDto> features;


    // Constructor SOLO con los campos planos (sin imágenes)
    public GetProductByIdDto(Long id,
                             String name,
                             String description,
                             Double price,
                             Boolean status,
                             Integer discountPercentage,
                             Boolean featured,
                             Long categoryId,
                             String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.discountPercentage = discountPercentage;
        this.featured = featured;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
