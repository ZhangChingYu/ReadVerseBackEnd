package com.elec5620.readverseserver.services;

import com.elec5620.readverseserver.dto.FormalDto;
import com.elec5620.readverseserver.dto.LoginDto;
import com.elec5620.readverseserver.models.User;
import com.elec5620.readverseserver.repositories.UserRepository;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public FormalDto login(LoginDto data){
        Optional<User> user = userRepository.findUserByEmailAndRole(data.getEmail(), data.getRole());
        FormalDto respond = FormalDto.builder().build();
        if (user.isPresent()) {
            // find the user
            if (data.getPassword().equals(user.get().getPassword())) {
                respond.setStatus(200);
                respond.setMessage("Login Success");
                respond.setData(
                        UserData.builder()
                                .id(user.get().getId())
                                .email(user.get().getEmail())
                                .role(user.get().getRole())
                                .build()
                );
            }
            // wrong password
            else {
                respond.setStatus(401);
                respond.setMessage("Wrong Password");
            }
        }
        // user not found
        else {
            respond.setStatus(404);
            respond.setMessage("User Not Found");
        }
        return respond;
    }

    public FormalDto register(User user){
        FormalDto response = FormalDto.builder().build();
        if(user == null){
            response.setStatus(400);
            response.setMessage("User can not be empty.");
        } else if (user.getRole() == null) {
            response.setStatus(400);
            response.setMessage("user [role] can not be Null.");
        } else if (user.getPassword() == null) {
            response.setStatus(400);
            response.setMessage("user [password] can not be Null.");
        } else if (user.getEmail() == null) {
            response.setStatus(400);
            response.setMessage("user [email] can not be Null.");
        } else {
            // check if the account already exist
            Optional<User> check = userRepository.findUserByEmailAndRole(user.getEmail(), user.getRole());
            if(check.isPresent()){
                response.setStatus(409);
                response.setMessage("User already exists.");
            }else{
                user.setVerified(null);
                user.setId(null);
                User result = userRepository.save(user);
                if(result.getId() != null){
                    response.setStatus(200);
                    response.setMessage("User register success.");
                    response.setData(user);
                } else {
                    response.setStatus(500);
                    response.setMessage("Server Error.");
                }
            }
        }
        return response;
    }
    @Data
    @Builder
    static class UserData {
        private Long id;
        private String email;
        private String role;
    }
}
