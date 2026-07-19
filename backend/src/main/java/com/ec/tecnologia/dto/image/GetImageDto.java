package com.ec.tecnologia.dto.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetImageDto {
    private Long id;
    private String url;
    private Integer orderImage;
    private Boolean isPrincipal;


}
