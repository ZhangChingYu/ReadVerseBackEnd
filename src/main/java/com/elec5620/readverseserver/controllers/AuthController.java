package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.LoginDto;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.services.AuthService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final Gson gson = new Gson();
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("login")
    public String Login(@RequestBody LoginDto data){
        FormalDto respond = authService.login(data);
        return gson.toJson(respond);
    }

    @PostMapping("register")
    public String Register(@RequestBody User user){
        FormalDto respond = authService.register(user);
        return gson.toJson(respond);
    }

    @PutMapping("profile/settings")
    public String ProfileSetting(@RequestBody User user){
        return "";
    }

    @PutMapping("profile/change_password")
    public String ChangePassword(@RequestBody Long id, String password){
        return "";
    }
}
