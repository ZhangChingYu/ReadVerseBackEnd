package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.LoginDto;
import com.elec5620.readverseserver.dto.UserDto;
import com.elec5620.readverseserver.dto.subdto.UserData;
import com.elec5620.readverseserver.models.Customer;
import com.elec5620.readverseserver.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private CustomerRepository customerRepository;
    @Autowired
    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UserDto login(LoginDto data){
        if (data.getRole().equals("Customer")) {
            Optional<Customer> customer = customerRepository.findCustomerByEmail(data.getEmail());
            if (customer.isPresent()) {
                // find the customer
                if (data.getPassword().equals(customer.get().getPassword())) {
                    UserDto user = UserDto.builder()
                            .status("200")
                            .message("Login Success")
                            .data(UserData.builder()
                                    .email(customer.get().getEmail())
                                    .role(data.getRole())
                                    .build())
                            .build();
                    return user;
                }
                // wrong password
                else {
                    UserDto user = UserDto.builder()
                            .status("400")
                            .message("Wrong Password")
                            .data(null)
                            .build();
                    return user;
                }
            }
            // user not found
            else {
                UserDto user = UserDto.builder()
                        .status("404")
                        .message("User Not Found")
                        .data(null)
                        .build();
                return user;
            }
        }
        return null;
    }
}
