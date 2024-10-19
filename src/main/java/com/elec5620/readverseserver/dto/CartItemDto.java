package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDto {
    private Long id;
    private Long bookId;
    private Long publisherId;
    private String title;
    private Double price;
    private String coverImage;
}
