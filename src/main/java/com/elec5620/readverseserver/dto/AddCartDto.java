package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCartDto {
    private Long bookId;
    private Long userId;
}
