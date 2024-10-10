package com.elec5620.readverseserver.dto.subdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserData {
    private String email;
    private String role;
}
