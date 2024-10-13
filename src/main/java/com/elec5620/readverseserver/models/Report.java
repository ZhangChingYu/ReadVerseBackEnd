package com.elec5620.readverseserver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Report")
public class Report {
    @Id
    private Long id;
    private Long reviewId;
    private String description;
    private Boolean status;
    private Date date;
    private String comment;
}
