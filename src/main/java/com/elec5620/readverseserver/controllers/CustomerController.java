package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.CustomerDto;
import com.elec5620.readverseserver.models.Customer;
import com.elec5620.readverseserver.services.CustomerService;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
    private CustomerService service;
    private Gson gson = new Gson();

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public String sayHello(){
        return "hello";
    }

    @GetMapping("customers")
    public String findAllCustomers(){
        List<CustomerDto> customers = service.findAllCustomer();

        return gson.toJson(customers);
    }


}
