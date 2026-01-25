package com.project.springpract.mapper;

import com.project.springpract.dto.AddressDTO;
import com.project.springpract.dto.UserDTO;
import com.project.springpract.dto.UserRequest;
import com.project.springpract.dto.UserResponse;
import com.project.springpract.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserMapper {

    // 1. Map Request -> Entity
    public User toUserEntity(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }

        User user = new User();
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setPassword(userRequest.password());
        user.setPhoneNumber(userRequest.phoneNumber());
        return user;
    }
    // 2. Map Entity -> Response
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getUserStatus(),
                user.getAddresses()

        );
    }




}

