package com.digitalMoneyHouse.accountsservice.entities;

import lombok.Data;

@Data
public class AccountRequest {
    private Long userId;
    private String alias;
    private String cvu;
}
