package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FormalDto {
    private int status;
    private String message;
    private Object data;
}
