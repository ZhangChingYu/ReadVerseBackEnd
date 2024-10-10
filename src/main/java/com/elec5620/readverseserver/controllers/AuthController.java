package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.LoginDto;
import com.elec5620.readverseserver.dto.UserDto;
import com.elec5620.readverseserver.services.AuthService;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private Gson gson = new Gson();
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("login")
    public String Login(@RequestBody LoginDto data){
        UserDto user = authService.login(data);
        return gson.toJson(user);
    }
}
