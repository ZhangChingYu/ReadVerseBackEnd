package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class PostBookDto {
    private Long publisherId;
    private String author;
    private Double price;
    private String title;
    private MultipartFile file;
}
