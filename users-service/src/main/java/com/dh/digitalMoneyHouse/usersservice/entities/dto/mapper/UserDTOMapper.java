package com.dh.digitalMoneyHouse.usersservice.entities.dto.mapper;

import com.dh.digitalMoneyHouse.usersservice.entities.User;
import com.dh.digitalMoneyHouse.usersservice.entities.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCvu(),
                user.getAlias(),
                user.getKeycloakId()
        );
    }
}
