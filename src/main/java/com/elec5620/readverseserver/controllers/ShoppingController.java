package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.AddCartDto;
import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.PlaceOrderDto;
import com.elec5620.readverseserver.services.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("delete_cart")
    public ResponseEntity<FormalDto> removeCartItem(@RequestBody Long cartId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        FormalDto response = service.removeCart(cartId);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @GetMapping("all_cart_items")
    public ResponseEntity<FormalDto> showAllCartItems(@RequestBody Long customerId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        FormalDto response = service.getAllCartItems(customerId);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PostMapping("place_orders")
    public ResponseEntity<FormalDto> placeOrders(@RequestBody List<PlaceOrderDto> newOrders){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        FormalDto response = service.placeOrder(newOrders);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
