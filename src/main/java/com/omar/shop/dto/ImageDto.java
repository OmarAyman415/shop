package com.omar.shop.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data

public class ImageDto {
    private Long imageId;
    private String imageName;
    private String downloadUrl;
}
