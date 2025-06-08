package com.dh.digitalMoneyHouse.usersservice.entities;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class Login {

    @Column(name = "Email", nullable = false)
    private String email;
    @Column(name = "Password", nullable = false)
    private String password;

}
