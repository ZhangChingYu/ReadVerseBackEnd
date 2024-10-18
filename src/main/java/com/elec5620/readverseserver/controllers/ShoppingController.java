package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.AddCartDto;
import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.services.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingController {
    private final ShoppingService service;
    @Autowired
    public ShoppingController(ShoppingService service) {
        this.service = service;
    }

    @PostMapping("add_cart")
    public ResponseEntity<FormalDto> addToCart(@RequestBody AddCartDto cartItem){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        FormalDto response = service.addCart(cartItem);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
