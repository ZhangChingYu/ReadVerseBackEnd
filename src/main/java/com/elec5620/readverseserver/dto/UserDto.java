package com.elec5620.readverseserver.dto;

import com.elec5620.readverseserver.dto.subdto.UserData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String status;
    private String message;
    private UserData data;
}

