package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.CustomerDto;
import com.elec5620.readverseserver.models.Customer;
import com.elec5620.readverseserver.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public List<CustomerDto> findAllCustomer(){
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map((customer) -> mapToCustomerDto(customer)).collect(Collectors.toList());
    }

    private CustomerDto mapToCustomerDto(Customer customer){
        CustomerDto customerDto = CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .password(customer.getPassword())
                .email(customer.getEmail())
                .build();
        return customerDto;
    }
}
