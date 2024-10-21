package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestBookDto {
    private Long publisherId;
    private Long bookId;
}
