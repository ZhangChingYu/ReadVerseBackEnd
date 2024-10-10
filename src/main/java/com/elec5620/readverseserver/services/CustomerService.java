package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> findAllCustomer();
}
