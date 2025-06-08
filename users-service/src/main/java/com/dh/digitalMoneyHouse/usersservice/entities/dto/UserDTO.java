package com.dh.digitalMoneyHouse.usersservice.entities.dto;

public record UserDTO(
        String firstName,
        String lastName,
        String username,
        String email,
        String phoneNumber,
        String cvu,
        String alias,
        String id
) {
}
