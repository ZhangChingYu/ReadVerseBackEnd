package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private Long parent;
}
