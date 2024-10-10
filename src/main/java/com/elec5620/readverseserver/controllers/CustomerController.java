package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.dto.CustomerDto;
import com.elec5620.readverseserver.services.CustomerService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {
    private Gson gson = new Gson();
    private CustomerService customerService;
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/test")
    public String sayHello(){
        return "hello";
    }

    @GetMapping("customers")
    public String findAllCustomers(){
        List<CustomerDto> customers = customerService.findAllCustomer();
        return gson.toJson(customers);
    }


}
