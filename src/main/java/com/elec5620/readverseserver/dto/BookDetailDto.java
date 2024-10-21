package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class BookDetailDto {
    private Long id;
    private Long publisherId;
    private String title;
    private String author;
    private Double price;
    private String summary;
    private Date uploadDate;
    private String coverImage;
    private List<ChapterIdDto> chapters;
    private Boolean onShelf;
}
