package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class OrderDetailDto {
    private Long orderId;
    private String orderNumber;
    private Long bookId;
    private String title;
    private Date date;
    private Double price;
    private String status;
    private String paymentMethod;
}
