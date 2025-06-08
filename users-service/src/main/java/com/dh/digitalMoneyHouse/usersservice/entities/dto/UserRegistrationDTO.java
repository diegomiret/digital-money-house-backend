package com.dh.digitalMoneyHouse.usersservice.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegistrationDTO(
        @JsonProperty("firstName")
        String firstName,
        String lastName,
        //String username,
        String email,
        @JsonProperty("phone")
        String phoneNumber,
        String dni,
        String password
        //String id
) {
}
