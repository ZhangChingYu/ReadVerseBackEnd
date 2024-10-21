package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceOrderDto {
    private Long customerId;
    private Long bookId;
    private Double price;
    private String paymentMethod;
}
