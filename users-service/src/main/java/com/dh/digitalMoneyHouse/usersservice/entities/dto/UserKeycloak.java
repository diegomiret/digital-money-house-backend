package com.dh.digitalMoneyHouse.usersservice.entities.dto;

public record UserKeycloak(
        String name,
        String lastName,
        String username,
        String email,
        String password
) {
}
