package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.LoginDto;
import com.elec5620.readverseserver.dto.UserDto;
import com.elec5620.readverseserver.dto.subdto.UserData;
import com.elec5620.readverseserver.models.Customer;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.repositories.CustomerRepository;
import com.elec5620.readverseserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private CustomerRepository customerRepository;
    private UserRepository userRepository;
    @Autowired
    public AuthService(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
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

    public FormalDto register(User user){
        FormalDto response = FormalDto.builder().build();
        if(user == null){
            response.setStatus("400");
            response.setMessage("User can not be empty.");
        } else if (user.getRole() == null) {
            response.setStatus("400");
            response.setMessage("user [role] can not be Null.");
        } else if (user.getPassword() == null) {
            response.setStatus("400");
            response.setMessage("user [password] can not be Null.");
        } else if (user.getEmail() == null) {
            response.setStatus("400");
            response.setMessage("user [email] can not be Null.");
        } else {
            user.setVerified(null);
            user.setId(null);
            User result = userRepository.save(user);
            if(result.getId() != null){
                response.setStatus("200");
                response.setMessage("User register success.");
                response.setData(user);
            }
        }
        return response;
    }
}
