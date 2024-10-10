package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.LoginDto;
import com.elec5620.readverseserver.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public UserDto login(LoginDto data);
}
