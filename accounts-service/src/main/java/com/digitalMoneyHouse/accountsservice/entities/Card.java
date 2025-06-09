package com.digitalMoneyHouse.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {


    private Long id;
    private Long accountId;
    private String name;
    private String number;
    private String expiration;
    private String cvc;

    public Card(Long accountId, String name, String number, String expiration, String cvc) {
        this.accountId = accountId;
        this.name = name;
        this.number = number;
        this.expiration = expiration;
        this.cvc = cvc;
    }

}



