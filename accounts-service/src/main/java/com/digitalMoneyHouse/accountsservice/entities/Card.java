package com.digitalMoneyHouse.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Card {
    private Long accountId;
    private String holder;
    private String number;
    private LocalDate expirationDate;
    private String cvv;
}

