package com.elec5620.readverseserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChapterIdDto {
    private int id;
    private String chapter;
}
