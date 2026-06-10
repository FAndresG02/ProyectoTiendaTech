package com.ec.tecnologia.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewsProductIdDto {

    private Double averageRating;

    private Long totalReviews;

    private List<GetReviewsDto> reviews;
}
