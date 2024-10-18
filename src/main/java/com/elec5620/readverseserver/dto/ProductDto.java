package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String title;
    private Double price;
    private String coverImg;
    private Date uploadDate;
}
