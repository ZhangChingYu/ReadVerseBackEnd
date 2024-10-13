package com.elec5620.readverseserver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Book")
public class Book {
    @Id
    private Long id;
    private String title;
    private String author;
    private String summary;
    private Long publisherId;
    private Double price;
    private String filePath;
    private Boolean status;
}
