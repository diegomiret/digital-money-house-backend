package com.dh.digitalMoneyHouse.usersservice.entities.dto;

public record AuthResponseDTO(
        UserDTO user,
        String accessToken
) {}