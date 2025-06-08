package com.dh.digitalMoneyHouse.usersservice.entities;

import lombok.Data;

@Data
public class AccountRequest {
    private Long userId;
    private String alias;
    private String cvu;

    public AccountRequest(Long userId, String alias, String cvu) {
        this.userId = userId;
        this.alias = alias;
        this.cvu = cvu;
    }


}
