package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.LoginDto;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.services.AuthService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("login")
    public ResponseEntity<FormalDto> Login(@RequestBody LoginDto data){
        FormalDto respond = authService.login(data);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @PostMapping("register")
    public ResponseEntity<FormalDto> Register(@RequestBody User user){
        FormalDto respond = authService.register(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(respond, headers, respond.getStatus());
    }

    @PutMapping("profile/settings")
    public ResponseEntity<FormalDto> ProfileSetting(@RequestBody User user){
        return null;
    }

    @PutMapping("profile/change_password")
    public ResponseEntity<FormalDto> ChangePassword(@RequestBody Long id, String password){
        return null;
    }
}
