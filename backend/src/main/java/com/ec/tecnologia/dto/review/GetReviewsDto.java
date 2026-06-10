package com.ec.tecnologia.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetReviewsDto {

    private String userName;

    private String comment;

    private Integer rating;

    private LocalDateTime createdAt;

    public GetReviewsDto(String userName, String comment, Integer rating, LocalDateTime createdAt) {
        this.userName = userName;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = createdAt;
    }
}
