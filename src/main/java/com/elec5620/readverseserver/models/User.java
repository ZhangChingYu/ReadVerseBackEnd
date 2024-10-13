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
@Table(name = "_User")
public class User {
    @Id
    private Long id;
    private String password;
    private String email;
    private String role;
    private Boolean verified;
}
