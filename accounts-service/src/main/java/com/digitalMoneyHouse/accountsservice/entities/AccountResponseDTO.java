package com.digitalMoneyHouse.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AccountResponseDTO {

    private Long id;
    private String userId;
    private Double balance;
    private String cvu;
    private String alias;

    public AccountResponseDTO(){

    }

}
